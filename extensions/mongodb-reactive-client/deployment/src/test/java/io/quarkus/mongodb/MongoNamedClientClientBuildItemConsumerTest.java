package io.quarkus.mongodb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.literal.NamedLiteral;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.arc.Arc;
import io.quarkus.builder.BuildChainBuilder;
import io.quarkus.deployment.builditem.ApplicationArchivesBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.mongodb.deployment.MongoClientNameBuildItem;
import io.quarkus.mongodb.deployment.MongoReactiveClientBuildItem;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.test.QuarkusUnitTest;

public class MongoNamedClientClientBuildItemConsumerTest {

    @RegisterExtension
    static QuarkusUnitTest runner = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class).addClasses(MongoTestBase.class))
            .withConfigurationResource("named-mongoclient.properties")
            .addBuildChainCustomizer(buildCustomizer());

    @Test
    public void testContainerHasBeans() {
        assertThat(Arc.container().instance(ReactiveMongoClient.class, Default.Literal.INSTANCE).get()).isNotNull();
        assertThat(Arc.container().instance(ReactiveMongoClient.class, NamedLiteral.of("secondreactive")).get()).isNotNull();
    }

    protected static Consumer<BuildChainBuilder> buildCustomizer() {
        return new Consumer<BuildChainBuilder>() {
            // This represents the extension.
            @Override
            public void accept(BuildChainBuilder builder) {
                builder.addBuildStep(context -> {
                    ApplicationArchivesBuildItem archive = context.consume(ApplicationArchivesBuildItem.class);
                    context.produce(Collections.singletonList(new MongoClientNameBuildItem("second")));
                }).consumes(ApplicationArchivesBuildItem.class)
                        .produces(MongoClientNameBuildItem.class)
                        .build();

                builder.addBuildStep(context -> {
                    List<MongoReactiveClientBuildItem> mongoClientBuildItems = context
                            .consumeMulti(MongoReactiveClientBuildItem.class);
                    context.produce(new FeatureBuildItem("dummy"));
                }).consumes(MongoReactiveClientBuildItem.class)
                        .produces(FeatureBuildItem.class)
                        .build();
            }
        };
    }
}
