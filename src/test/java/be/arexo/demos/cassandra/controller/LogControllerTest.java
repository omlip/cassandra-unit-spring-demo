package be.arexo.demos.cassandra.controller;

import be.arexo.demos.cassandra.DemoApplication;
import be.arexo.demos.cassandra.test.AbstractEmbeddedCassandraTest;
import org.cassandraunit.spring.CassandraDataSet;
import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@SpringApplicationConfiguration(classes = DemoApplication.class)
@CassandraDataSet(keyspace = "mykeyspace", value = {"dataset.cql"})
public class LogControllerTest extends AbstractEmbeddedCassandraTest {

    @Test
    public void testFindOne() throws Exception {

        ResponseEntity<Log> response = client.getForEntity("/logs/{id}", Log.class, 1);

        assertThat(response.getStatusCode()     , is(HttpStatus.OK));
        assertThat(response.getBody().getQuery(), is("cinema"));
    }
}