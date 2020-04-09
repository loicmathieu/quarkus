package io.quarkus.elasticsearch.panache.quarkuselasticsearch.deployment;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;

import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.BeanContainerBuildItem;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.elasticsearch.panache.quarkuselasticsearch.ElasticsearchPanacheRecorder;
import io.quarkus.elasticsearch.panache.quarkuselasticsearch.SearchMappingProducer;

class ElasticsearchPanacheProcessor {

    private static final String FEATURE = "elasticsearch-panache";

    private static final DotName DOTNAME_INDEXED = DotName.createSimple(Indexed.class.getName());

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem build() {
        return AdditionalBeanBuildItem.unremovableOf(SearchMappingProducer.class);
    }

    @BuildStep
    IndexedClassBuildItem listIndexedClasses(CombinedIndexBuildItem index) {
        Set<String> indexedClasses = new HashSet<>();
        for (AnnotationInstance annotationInstance : index.getIndex().getAnnotations(DOTNAME_INDEXED)) {
            ClassInfo info = annotationInstance.target().asClass();
            indexedClasses.add(info.name().toString());
        }

        return new IndexedClassBuildItem(indexedClasses);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void configureDriverProducer(ElasticsearchPanacheRecorder elasticsearchPanacheRecorder,
            BeanContainerBuildItem beanContainerBuildItem,
            IndexedClassBuildItem indexedClassBuildItem) {

        //TODO shutdown ?
        elasticsearchPanacheRecorder.configureSearch(indexedClassBuildItem.getIndexedClasses(),
                beanContainerBuildItem.getValue());
    }

}
