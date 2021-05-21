package io.quarkus.mongodb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.literal.NamedLiteral;
import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.arc.Arc;
import io.quarkus.arc.runtime.ClientProxyUnwrapper;
import io.quarkus.mongodb.health.MongoHealthCheck;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.test.QuarkusUnitTest;

public class DefaultAndNamedMongoClientConfigTest extends MongoWithReplicasTestBase {

    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class).addClasses(MongoTestBase.class))
            .withConfigurationResource("application-default-and-named-mongoclient.properties");

    @Inject
    ReactiveMongoClient client;

    @Inject
    @MongoClientName("cluster2")
    ReactiveMongoClient client2;

    @Inject
    @Any
    MongoHealthCheck health;

    private final ClientProxyUnwrapper unwrapper = new ClientProxyUnwrapper();

    @AfterEach
    void cleanup() {
        if (client != null) {
            client.close();
        }
        if (client2 != null) {
            client2.close();
        }
    }

    @Test
    public void testNamedDataSourceInjection() {
        assertThat(client.listDatabases().collect().first().await().indefinitely()).isNotEmpty();
        assertThat(client2.listDatabases().collect().first().await().indefinitely()).isNotEmpty();

        assertThat(Arc.container().instance(ReactiveMongoClient.class).get()).isNotNull();
        assertThat(Arc.container().instance(ReactiveMongoClient.class, Default.Literal.INSTANCE).get()).isNotNull();
        assertThat(Arc.container().instance(ReactiveMongoClient.class, NamedLiteral.of("cluster2")).get()).isNotNull();
        assertThat(Arc.container().instance(ReactiveMongoClient.class, NamedLiteral.of("cluster3")).get()).isNull();

        org.eclipse.microprofile.health.HealthCheckResponse response = health.call();
        assertThat(response.getStatus()).isEqualTo(HealthCheckResponse.Status.UP);
        assertThat(response.getData()).isNotEmpty();
        assertThat(response.getData().get()).hasSize(2).contains(
                entry("<default>", "OK"),
                entry("cluster2", "OK"));

    }

}
