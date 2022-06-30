package io.quarkus.it.panache;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class CatProjectionBean {

    private String name;

    private String ownerName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
