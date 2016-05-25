package be.arexo.demos.cassandra.controller;

import be.arexo.demos.cassandra.repository.entities.LogEntityCass;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class LogMapper implements Function<LogEntityCass, Log> {

    @Override
    public Log apply(LogEntityCass logEntityCass) {
        if (logEntityCass == null) {
            return null;
        }

        Log log = new Log();

        log.setId(logEntityCass.getId());
        log.setQuery(logEntityCass.getQuery());
        log.setDate(logEntityCass.getDate());

        return log;
    }
}
