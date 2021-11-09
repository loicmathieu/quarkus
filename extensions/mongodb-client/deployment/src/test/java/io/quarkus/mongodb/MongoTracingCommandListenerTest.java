package io.quarkus.mongodb;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.mongodb.client.MongoClient;

import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.GlobalTracer;
import io.opentracing.util.GlobalTracerTestUtil;
import io.quarkus.mongodb.tracing.MongoTracingCommandListener;
import io.quarkus.test.QuarkusUnitTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.mongodb.MongoTestResource;

/**
 * Test the inclusion and config of the {@link MongoTracingCommandListener}.
 * 
 * @see io.quarkus.smallrye.opentracing.deployment.TracingTest
 */
@QuarkusTestResource(MongoTestResource.class)
public class MongoTracingCommandListenerTest {

    @Inject
    MongoClient client;

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .withConfigurationResource("application-tracing-mongo.properties");

    static MockTracer mockTracer = new MockTracer();
    static {
        GlobalTracer.register(mockTracer);
    }

    @BeforeEach
    public void before() {
        mockTracer.reset();
    }

    @AfterAll
    public static void afterAll() {
        GlobalTracerTestUtil.resetGlobalTracer();
    }

    @AfterEach
    void cleanup() {
        if (client != null) {
            client.close();
        }
    }

    @Test
    void testClientInitialization() {
        assertThat(mockTracer.finishedSpans()).isEmpty();

        assertThat(client.listDatabaseNames().first()).isNotEmpty();

        assertThat(mockTracer.finishedSpans()).hasSize(1);
        MockSpan span = mockTracer.finishedSpans().get(0);
        assertThat(span.operationName()).isEqualTo("listDatabases");
    }

}
