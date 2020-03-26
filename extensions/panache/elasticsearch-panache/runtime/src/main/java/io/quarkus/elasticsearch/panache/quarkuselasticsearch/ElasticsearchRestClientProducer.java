package io.quarkus.elasticsearch.panache.quarkuselasticsearch;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

@ApplicationScoped
public class ElasticsearchRestClientProducer {
    private volatile RestClient restClient;
    private volatile RestHighLevelClient highLevelRestClient;

    void initialize(RestClient restClient, RestHighLevelClient highLevelRestClient) {
        this.restClient = restClient;
        this.highLevelRestClient = highLevelRestClient;
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
}
