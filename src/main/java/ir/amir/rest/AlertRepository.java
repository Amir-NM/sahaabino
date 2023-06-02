package ir.amir.rest;

import ir.amir.rest.database.Alert;
import org.springframework.data.repository.CrudRepository;

public interface AlertRepository extends CrudRepository<Alert, Integer> {}
