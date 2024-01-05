package io.quarkus.logging.opentelemetry.deployment;

import io.quarkus.deployment.Feature;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LogHandlerBuildItem;
import io.quarkus.logging.opentelemetry.runtime.OpenTelemetryLogConfig;
import io.quarkus.logging.opentelemetry.runtime.OpenTelemetryLogRecorder;

class OpenTelemetryLogHandlerProcessor {

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(Feature.LOGGING_OPENTELEMETRY);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    LogHandlerBuildItem build(OpenTelemetryLogRecorder recorder, OpenTelemetryLogConfig config) {
        return new LogHandlerBuildItem(recorder.initializeHandler(config));
    }
}
