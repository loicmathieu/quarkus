package io.quarkus.it.mongodb.panache.bugs;

import org.bson.codecs.pojo.annotations.BsonId;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;

public class Bug19840ReactiveEntity extends PanacheMongoEntityBase {
    @BsonId
    public String customId;
    public String anOtherField;
}
