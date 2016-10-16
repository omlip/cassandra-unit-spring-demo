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
