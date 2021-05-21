package io.quarkus.mongodb.deployment;

import com.mongodb.client.MongoClient;

import io.quarkus.builder.item.MultiBuildItem;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.runtime.RuntimeValue;

/**
 * Provide the MongoDB clients as RuntimeValue's.
 */
public final class MongoClientBuildItem extends MultiBuildItem {
    private final RuntimeValue<MongoClient> client;
    private final String name;

    public MongoClientBuildItem(RuntimeValue<MongoClient> client, String name) {
        this.client = client;
        this.name = name;
    }

    public RuntimeValue<MongoClient> getClient() {
        return client;
    }

    public String getName() {
        return name;
    }
}
