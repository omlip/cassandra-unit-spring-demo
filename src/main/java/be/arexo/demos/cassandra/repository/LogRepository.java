package be.arexo.demos.cassandra.repository;

import be.arexo.demos.cassandra.repository.entities.LogEntityCass;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface LogRepository extends CassandraRepository<LogEntityCass> {

}