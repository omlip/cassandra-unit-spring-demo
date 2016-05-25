package be.arexo.demos.cassandra.controller;

import be.arexo.demos.cassandra.repository.entities.LogEntityCass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.MapId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.data.cassandra.repository.support.BasicMapId.id;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private CrudRepository<LogEntityCass, MapId> repository;

    @Autowired
    private LogMapper mapper;

    @Override
    public List<Log> findAll() {

        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Override
    public Log findOne(String id) {

        return mapper.apply(
                repository.findOne(
                        id().with("id", id)
                )
        );

    }
}
