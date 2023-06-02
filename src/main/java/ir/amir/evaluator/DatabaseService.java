package ir.amir.evaluator;

import ir.amir.evaluator.config.DatabaseSaverConfig;
import ir.amir.rest.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

/**
 * this service receives alerts from queue and saves them to database.
 */
public class DatabaseService extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    private boolean shouldEnd;
    private final String databaseURL;
    private final String databaseUser;
    private final String databasePassword;
    private final BlockingQueue<Alert> shareAlert;

    public DatabaseService(DatabaseSaverConfig config, BlockingQueue<Alert> shareAlert) {
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
                logger.warn("Thread is interrupted.", e);
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
                logger.info("Alert saved to database.");
            } catch (SQLException e) {
                logger.error("Could not insert to database.");
                throw new RuntimeException(e);
            }
        }
    }

    public int getAlertsCount() {
        int count = 0;
        try {
            Connection connection = DriverManager.getConnection(this.databaseURL, this.databaseUser, this.databasePassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select count(*) from alert;");
            if (resultSet.next())
                count = resultSet.getInt(1);
            connection.close();
            logger.info("Alert saved to database.");
        } catch (SQLException e) {
            logger.error("Could not get count of alerts from database.");
            throw new RuntimeException(e);
        }
        return count;
    }
}
