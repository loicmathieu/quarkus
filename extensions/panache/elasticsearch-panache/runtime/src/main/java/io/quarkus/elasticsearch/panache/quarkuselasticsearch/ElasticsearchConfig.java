package io.quarkus.elasticsearch.panache.quarkuselasticsearch;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public class ElasticsearchConfig {
    /**
     * The list of hosts of the Elasticsearch servers.
     */
    @ConfigItem(defaultValue = "localhost:9200")
    List<String> hosts;

    /**
     * The protocol to use when contacting Elasticsearch servers.
     * Set to "https" to enable SSL/TLS.
     */
    @ConfigItem(defaultValue = "http")
    String protocol;

    /**
     * The username used for authentication.
     */
    @ConfigItem
    Optional<String> username;

    /**
     * The password used for authentication.
     */
    @ConfigItem
    Optional<String> password;

    /**
     * The connection timeout.
     */
    @ConfigItem(defaultValue = "3S")
    Duration connectionTimeout;

    /**
     * The maximum number of connections to all the Elasticsearch servers.
     */
    @ConfigItem(defaultValue = "20")
    int maxConnections;

    /**
     * The maximum number of connections per Elasticsearch server.
     */
    @ConfigItem(defaultValue = "10")
    int maxConnectionsPerRoute;

    /**
     * Configuration for the automatic discovery of new Elasticsearch nodes.
     */
    @ConfigItem
    DiscoveryConfig discovery;

    @ConfigGroup
    public static class DiscoveryConfig {

        /**
         * Defines if automatic discovery is enabled.
         */
        @ConfigItem(defaultValue = "false")
        boolean enabled;

        /**
         * Refresh interval of the node list.
         */
        @ConfigItem(defaultValue = "10S")
        Duration refreshInterval;

    }
}
