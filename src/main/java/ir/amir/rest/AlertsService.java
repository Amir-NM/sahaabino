package ir.amir.rest;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AlertsService {
    private final AlertRepository alertRepository;

    public AlertsService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<Alert> getAllAlerts() {
        Iterable<Alert> alerts = this.alertRepository.findAll();
        List<Alert> alertList = new ArrayList<>();
        alerts.forEach(alertList::add);
        Collections.sort(alertList);
        return alertList;
    }
}
