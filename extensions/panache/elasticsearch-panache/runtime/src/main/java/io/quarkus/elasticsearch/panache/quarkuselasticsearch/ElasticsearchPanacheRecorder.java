package io.quarkus.elasticsearch.panache.quarkuselasticsearch;

import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.search.mapper.pojo.standalone.mapping.CloseableSearchMapping;
import org.hibernate.search.mapper.pojo.standalone.mapping.SearchMapping;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class ElasticsearchPanacheRecorder {

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
                .property("hibernate.search.default_backend", "elasticsearch")
                .property("hibernate.search.backends.elasticsearch.type", "elasticsearch")
                .build();

        // TODO close the mapping

        SearchMappingProducer searchMappingProducer = beanContainer.instance(SearchMappingProducer.class);
        searchMappingProducer.initializeMapping(mapping);
    }
}
