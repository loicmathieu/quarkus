package io.quarkus.elasticsearch.panache.quarkuselasticsearch;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.hibernate.search.mapper.javabean.mapping.SearchMapping;

@ApplicationScoped
public class ElasticsearchRestClientProducer {
    private volatile RestClient restClient;
    private volatile RestHighLevelClient highLevelRestClient;
    private volatile SearchMapping searchMapping;

    void initialize(RestClient restClient, RestHighLevelClient highLevelRestClient) {
        this.restClient = restClient;
        this.highLevelRestClient = highLevelRestClient;
    }

    void initializeMapping(SearchMapping searchMapping) {
        this.searchMapping = searchMapping;
    }

    @Produces
    @Dependent
    @Default
    public RestClient restClient() {
        return this.restClient;
    }

    @Produces
    @Dependent
    @Default
    public RestHighLevelClient highLevelRestClient() {
        return this.highLevelRestClient;
    }

    @Produces
    @Dependent
    @Default
    public SearchMapping searchMapping() {
        return this.searchMapping;
    }
}
