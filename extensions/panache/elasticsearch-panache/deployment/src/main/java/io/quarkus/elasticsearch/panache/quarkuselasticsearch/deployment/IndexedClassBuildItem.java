package io.quarkus.elasticsearch.panache.quarkuselasticsearch.deployment;

import java.util.Set;

import io.quarkus.builder.item.SimpleBuildItem;

final class IndexedClassBuildItem extends SimpleBuildItem {
    private Set<String> indexedClasses;

    public IndexedClassBuildItem(Set<String> indexedClasses) {
        this.indexedClasses = indexedClasses;
    }

    public Set<String> getIndexedClasses() {
        return indexedClasses;
    }
}
