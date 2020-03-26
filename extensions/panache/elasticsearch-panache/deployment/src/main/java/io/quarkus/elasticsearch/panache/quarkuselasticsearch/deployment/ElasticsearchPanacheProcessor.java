package io.quarkus.elasticsearch.panache.quarkuselasticsearch.deployment;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BeanContainerBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.elasticsearch.panache.quarkuselasticsearch.ElasticsearchConfig;
import io.quarkus.elasticsearch.panache.quarkuselasticsearch.ElasticsearchRestClientProducer;
import io.quarkus.elasticsearch.panache.quarkuselasticsearch.ElasticsearchRestClientRecorder;

class ElasticsearchPanacheProcessor {

    private static final String FEATURE = "elasticsearch-panache";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep()
    AdditionalBeanBuildItem build() {
        return AdditionalBeanBuildItem.unremovableOf(ElasticsearchRestClientProducer.class);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void configureDriverProducer(ElasticsearchRestClientRecorder restClientRecorder,
            BeanContainerBuildItem beanContainerBuildItem,
            ElasticsearchConfig configuration) {

        //TODO shutdown ?
        restClientRecorder.configureRestClient(beanContainerBuildItem.getValue(), configuration);
    }

}
