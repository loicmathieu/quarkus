package io.quarkus.mongodb.deployment;

import io.quarkus.builder.item.MultiBuildItem;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.runtime.RuntimeValue;

/**
 * Provide the MongoDB clients as RuntimeValue's.
 */
public final class MongoReactiveClientBuildItem extends MultiBuildItem {
    private final RuntimeValue<ReactiveMongoClient> reactive;
    private final String name;

    public MongoReactiveClientBuildItem(RuntimeValue<ReactiveMongoClient> reactiveClient, String name) {
        this.reactive = reactiveClient;
        this.name = name;
    }

    public RuntimeValue<ReactiveMongoClient> getReactive() {
        return reactive;
    }

    public String getName() {
        return name;
    }
}
