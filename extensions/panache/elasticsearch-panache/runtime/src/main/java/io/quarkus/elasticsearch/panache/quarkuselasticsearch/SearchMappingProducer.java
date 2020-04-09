package io.quarkus.elasticsearch.panache.quarkuselasticsearch;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.hibernate.search.mapper.pojo.standalone.mapping.SearchMapping;

@ApplicationScoped
public class SearchMappingProducer {
    private volatile SearchMapping searchMapping;

    void initializeMapping(SearchMapping searchMapping) {
        this.searchMapping = searchMapping;
    }

    @Produces
    @Singleton
    @Default
    public SearchMapping searchMapping() {
        return this.searchMapping;
    }
}
