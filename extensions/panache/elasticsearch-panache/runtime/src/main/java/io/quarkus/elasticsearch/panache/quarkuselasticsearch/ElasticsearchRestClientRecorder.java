package io.quarkus.elasticsearch.panache.quarkuselasticsearch;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class ElasticsearchRestClientRecorder {

    public void configureRestClient(BeanContainer beanContainer, ElasticsearchConfig config) {
        List<HttpHost> hosts = config.hosts.stream().map(s -> new HttpHost(s.substring(0, s.indexOf(":")),
                Integer.valueOf(s.substring(s.indexOf(":") + 1)), config.protocol)).collect(Collectors.toList());
        RestClientBuilder builder = RestClient.builder(hosts.toArray(new HttpHost[0]));

        //TODO sniffer

        ElasticsearchRestClientProducer restClientProducer = beanContainer.instance(ElasticsearchRestClientProducer.class);
        restClientProducer.initialize(builder.build());
    }
}
