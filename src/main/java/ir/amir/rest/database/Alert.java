package ir.amir.rest.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;

@Table(name = "alert")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//public class Alert {
public class Alert implements Comparable<Alert>{

    @Id
    private int alertID;
    private String ruleName;
    private String componentName;
    private String description;
    private Timestamp creationTime;

    public Alert(int id, String roleName, String componentName, String description) {
        this.alertID = id;
        this.ruleName = roleName;
        this.componentName = componentName;
        this.description = description;
        this.creationTime = Timestamp.from(Instant.now());
    }

    @Override
    public int compareTo(Alert alert) {
        return this.creationTime.compareTo(alert.creationTime);
    }
}
