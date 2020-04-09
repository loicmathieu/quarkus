package io.quarkus.elasticsearch.panache.quarkuselasticsearch;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.hibernate.search.mapper.javabean.mapping.CloseableSearchMapping;
import org.hibernate.search.mapper.javabean.mapping.SearchMapping;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class ElasticsearchRestClientRecorder {

    public void configureRestClient(BeanContainer beanContainer, ElasticsearchConfig config) {
        List<HttpHost> hosts = config.hosts.stream().map(s -> new HttpHost(s.substring(0, s.indexOf(":")),
                Integer.valueOf(s.substring(s.indexOf(":") + 1)), config.protocol)).collect(Collectors.toList());
        RestClientBuilder builder = RestClient.builder(hosts.toArray(new HttpHost[0]));
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);

        //TODO sniffer

        ElasticsearchRestClientProducer restClientProducer = beanContainer.instance(ElasticsearchRestClientProducer.class);
        restClientProducer.initialize(restHighLevelClient.getLowLevelClient(), restHighLevelClient);
    }

    public void configureSearch(Set<String> entityTypes, BeanContainer beanContainer) {
        Set<Class<?>> classes = entityTypes.stream().map(s -> {
            try {
                return Class.forName(s, true, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        })
                .collect(Collectors.toSet());

        CloseableSearchMapping mapping = SearchMapping.builder()
                .addEntityTypes(classes)
                .setProperty("hibernate.search.default_backend", "elasticsearch")
                .setProperty("hibernate.search.backends.elasticsearch.type", "elasticsearch")
                .build();

        // TODO close the mapping

        ElasticsearchRestClientProducer restClientProducer = beanContainer.instance(ElasticsearchRestClientProducer.class);
        restClientProducer.initializeMapping(mapping);
    }
}
