package be.arexo.demos.cassandra;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
@EnableCassandraRepositories(basePackages = { "be.arexo.demos.cassandra.repository" })
public class DemoApplication {

}

