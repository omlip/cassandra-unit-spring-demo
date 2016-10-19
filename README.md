# Write integration tests with Spring Boot and Cassandra in 2 minutes


### Context
The following document will describe an how-to write integration tests with Spring-Boot against an embedded Cassandra database with the use of cassandra-unit framework.
To achieve this, I wrote a little and basic log management application. This mainly focused on writing a test and validated what is retrieved from the database.

### Cassandra-unit
Cassandra-unit, as indicated by its name, is a unit testing library wich add to your test the ability to start/stop a Cassandra database server and also inject a CQL dataset into it.

The project provides two modules:
- **cassandra-unit** : the core, contains everything to start the server, injection of dataset etc...
- **cassandra-unit-spring** : A library which fill the gap between Spring-Boot dependency injection and the Cassandra related stuff. The second ones include the first.

More info on the project [GitHub's page](https://github.com/jsevellec/cassandra-unit)

### Configure your project
For this example, I used Gradle as building management system, add the following depedencies to your build.gradle file
```
ext {
    springBootVersion = '1.3.5.RELEASE'
}

dependencies {
    compile("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")

    compile("org.springframework.boot:spring-boot-starter-data-cassandra:${springBootVersion}")

    compile("com.datastax.cassandra:cassandra-driver-core:2.1.7.1")
    compile("com.datastax.cassandra:cassandra-driver-dse:2.1.7.1")

    testCompile("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")
    testCompile('org.cassandraunit:cassandra-unit-spring:2.2.2.1')
}
```


### Write a dataset
Create a dataset file, the CQL instructions present in that file will be played against the database which is loaded within your test


The "dataset.cql" file
```
CREATE KEYSPACE IF NOT EXISTS mykeyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

DROP TABLE IF EXISTS mykeyspace.logs;

CREATE TABLE IF NOT EXISTS mykeyspace.logs (
    id text,
    query text,
    PRIMARY KEY (id)
);


INSERT into mykeyspace.logs(id, query) values ('1','cinema');
```

### Add cassandra properties to application.properies
```
test.url=http://localhost

spring.data.cassandra.keyspace-name=mykeyspace
spring.data.cassandra.contact-points=localhost
spring.data.cassandra.port=9142
```

There is nothing magic here, juste tell the Spring Boot Cassandra auto-configuration to connect on localhost and port 9142
Attention!!! cassandra-unit start by default cassandra on port 9142 instead of 9042

### Write a test
```java
package be.arexo.demos.cassandra.controller;
 
import be.arexo.demos.cassandra.test.AbstractEmbeddedCassandraTest;
import org.cassandraunit.spring.CassandraDataSet;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
 
@CassandraDataSet(keyspace = "mykeyspace", value = {"dataset.cql"})
public class LogControllerTest extends AbstractEmbeddedCassandraTest {
 
    @Test
    public void testFindOne() throws Exception {
 
        ResponseEntity<Log> response = client.getForEntity("/logs/{id}", Log.class, 1);
 
        assertThat(response.getStatusCode()     , is(HttpStatus.OK));
        assertThat(response.getBody().getQuery(), is("cinema"));
    }
}
```

The annotation @CassandraDataSet is used to define the keyspace to use and also the sql requests to load into the database

### Go further and create an abstract test class
```java
package be.arexo.demos.cassandra.test;

import be.arexo.demos.cassandra.DemoApplication;
import org.cassandraunit.spring.CassandraUnitDependencyInjectionTestExecutionListener;
import org.cassandraunit.spring.EmbeddedCassandra;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;

import javax.annotation.PostConstruct;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@WebIntegrationTest(randomPort = true) // Pick a random port for Tomcat
@TestExecutionListeners(listeners = {
        CassandraUnitDependencyInjectionTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class}
)
@EmbeddedCassandra(timeout = 60000)
public class AbstractEmbeddedCassandraTest {

    @Value("${local.server.port}")
    protected int port;

    @Value("${test.url}")
    protected String url;

    protected RestTemplate client;

    @PostConstruct
    public void init() {
        DefaultUriTemplateHandler handler = new DefaultUriTemplateHandler();
        handler.setBaseUrl(url + ":" + port);
        handler.setParsePath(true);

        client = new TestRestTemplate();
        client.setUriTemplateHandler(handler);
    }
}
```

Then execute it and you should see something like this as output
```console
016-10-19 21:21:28.415  INFO 40099 --- [           main] b.a.d.c.controller.LogControllerTest     : Started LogControllerTest in 3.461 seconds (JVM running for 14.008)
2016-10-19 21:21:28.627  INFO 40099 --- [o-auto-1-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring FrameworkServlet 'dispatcherServlet'
2016-10-19 21:21:28.628  INFO 40099 --- [o-auto-1-exec-1] o.s.web.servlet.DispatcherServlet        : FrameworkServlet 'dispatcherServlet': initialization started
2016-10-19 21:21:28.641  INFO 40099 --- [o-auto-1-exec-1] o.s.web.servlet.DispatcherServlet        : FrameworkServlet 'dispatcherServlet': initialization completed in 13 ms
2016-10-19 21:21:28.854  INFO 40099 --- [           main] c.d.d.c.p.DCAwareRoundRobinPolicy        : Using data-center name 'datacenter1' for DCAwareRoundRobinPolicy (if this is incorrect, please provide the correct datacenter name with DCAwareRoundRobinPolicy constructor)
2016-10-19 21:21:28.854  INFO 40099 --- [           main] com.datastax.driver.core.Cluster         : New Cassandra host localhost/127.0.0.1:9142 added
2016-10-19 21:21:28.889  INFO 40099 --- [edPool-Worker-2] o.a.cassandra.service.MigrationManager   : Drop Keyspace 'system_distributed'
2016-10-19 21:21:29.196  INFO 40099 --- [edPool-Worker-3] o.a.cassandra.service.MigrationManager   : Drop Keyspace 'mykeyspace'
2016-10-19 21:21:29.489  INFO 40099 --- [iceShutdownHook] o.apache.cassandra.thrift.ThriftServer   : Stop listening to thrift clients
2016-10-19 21:21:29.489  INFO 40099 --- [       Thread-3] ationConfigEmbeddedWebApplicationContext : Closing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@4372b9b6: startup date [Wed Oct 19 21:21:25 CEST 2016]; root of context hierarchy
2016-10-19 21:21:29.498  INFO 40099 --- [iceShutdownHook] org.apache.cassandra.transport.Server    : Stop listening for CQL clients
2016-10-19 21:21:29.498  INFO 40099 --- [iceShutdownHook] org.apache.cassandra.gms.Gossiper        : Announcing shutdown
2016-10-19 21:21:29.499  INFO 40099 --- [iceShutdownHook] o.a.cassandra.service.StorageService     : Node /127.0.0.1 state jump to normal
2016-10-19 21:21:29.506 ERROR 40099 --- [-reconnection-0] c.d.driver.core.ControlConnection        : [Control connection] Cannot connect to any host, scheduling retry in 1000 milliseconds
2016-10-19 21:21:30.509 ERROR 40099 --- [-reconnection-0] c.d.driver.core.ControlConnection        : [Control connection] Cannot connect to any host, scheduling retry in 2000 milliseconds
2016-10-19 21:21:31.502  INFO 40099 --- [iceShutdownHook] o.apache.cassandra.net.MessagingService  : Waiting for messaging service to quiesce
2016-10-19 21:21:31.503  INFO 40099 --- [CEPT-/127.0.0.1] o.apache.cassandra.net.MessagingService  : MessagingService has terminated the accept() thread
```

### Conclusion
That 's all, as you can see, writing integration test with an embedded cassandra database is not so difficult.
With this code, you have and ready to go example. 

I hope you enjoy this article. If you have any remarks, please feel free to contact me at olivier.antoine@arexo.be :-)


### troubleshooting

if you got this ...
```java
java.lang.NoSuchMethodError: com.codahale.metrics.Snapshot: method <init>()V not found
	at com.codahale.metrics.UniformSnapshot.<init>(UniformSnapshot.java:39) ~[metrics-core-3.1.0.jar:3.0.2]
	at org.apache.cassandra.metrics.EstimatedHistogramReservoir$HistogramSnapshot.<init>(EstimatedHistogramReservoir.java:77) ~[cassandra-all-2.2.2.jar:2.2.2]
	at org.apache.cassandra.metrics.EstimatedHistogramReservoir.getSnapshot(EstimatedHistogramReservoir.java:62) ~[cassandra-all-2.2.2.jar:2.2.2]
	at com.codahale.metrics.Histogram.getSnapshot(Histogram.java:54) ~[metrics-core-3.0.2.jar:3.0.2]
	at com.codahale.metrics.Timer.getSnapshot(Timer.java:142) ~[metrics-core-3.0.2.jar:3.0.2]
	at org.apache.cassandra.db.ColumnFamilyStore$3.run(ColumnFamilyStore.java:435) ~[cassandra-all-2.2.2.jar:2.2.2]
	at org.apache.cassandra.concurrent.DebuggableScheduledThreadPoolExecutor$UncomplainingRunnable.run(DebuggableScheduledThreadPoolExecutor.java:118) ~[cassandra-all-2.2.2.jar:2.2.2]
	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511) [na:1.8.0_25]
	at java.util.concurrent.FutureTask.runAndReset(FutureTask.java:308) [na:1.8.0_25]
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.access$301(ScheduledThreadPoolExecutor.java:180) [na:1.8.0_25]
	at java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:294) [na:1.8.0_25]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) [na:1.8.0_25]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) [na:1.8.0_25]
	at java.lang.Thread.run(Thread.java:745) [na:1.8.0_25]

```

Exclude from the configuration de *com.codahale.metrics* dependency
```gradle
configurations {
	all*.exclude group: 'com.codahale.metrics'
}
```
