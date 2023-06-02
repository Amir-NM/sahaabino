package ir.amir.evaluator;

import ir.amir.evaluator.config.DatabaseSaverConfig;
import ir.amir.rest.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

public class DatabaseSaverService extends Thread {
    private final Logger logger;
    private boolean shouldEnd;
    private final String databaseURL;
    private final String databaseUser;
    private final String databasePassword;
    private final BlockingQueue<Alert> shareAlert;

    public DatabaseSaverService(DatabaseSaverConfig config, BlockingQueue<Alert> shareAlert) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.shareAlert = shareAlert;
        this.databaseURL = config.getDatabaseURL();
        this.databaseUser = config.getDatabaseUsername();
        this.databasePassword = config.getDatabasePassword();
        this.shouldEnd = false;
    }

    public void run() {
        while (!this.shouldEnd || !this.shareAlert.isEmpty()) {
            Alert alert = null;
            try {
                alert = this.shareAlert.take();
            } catch (InterruptedException e) {
                this.logger.warn("Thread is interrupted.", e);
                this.shouldEnd = true;
                Thread.currentThread().interrupt();
            }

            try {
                Connection connection = DriverManager.getConnection(this.databaseURL, this.databaseUser, this.databasePassword);
                PreparedStatement preparedStatement = connection.prepareStatement("insert into alert values (?, ?, ?, ?, ?);");
                preparedStatement.setInt(1, Objects.requireNonNull(alert).getAlertID());
                preparedStatement.setString(2, alert.getRuleName());
                preparedStatement.setString(3, alert.getComponentName());
                preparedStatement.setString(4, alert.getDescription());
                preparedStatement.setTimestamp(5, alert.getCreationTime());
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();
                this.logger.info("Alert saved to database.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}