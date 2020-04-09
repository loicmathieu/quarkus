package org.acme.rest.json;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Indexed
public class Fruit {
    public String id;
    public String name;
    public String color;

    @DocumentId
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @FullTextField(analyzer = "default")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @FullTextField(analyzer = "default")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
