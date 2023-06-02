package ir.amir.rest.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AlertSaver {
    private final String databaseURL; // jdbc:mysql://localhost:3306/alerts_db
    private final String databaseUser;
    private final String databasePassword;

    public AlertSaver(String databaseURL, String databaseUser, String databasePassword) {
        this.databaseURL = databaseURL;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
    }

    public void saveAlert(Alert alert) throws ClassNotFoundException, SQLException {
        Connection connection;

        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(this.databaseURL, this.databaseUser, this.databasePassword);

        PreparedStatement pstm = connection.prepareStatement("insert into alert values (?, ?, ?, ?, ?);");

        pstm.setInt(1, alert.getAlertID());
        pstm.setString(2, alert.getRuleName());
        pstm.setString(3, alert.getComponentName());
        pstm.setString(4, alert.getDescription());
        pstm.setTimestamp(5, alert.getCreationTime());

        pstm.execute();

        pstm.close();
        connection.close();
    }
}