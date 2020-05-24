@file:Suppress("DEPRECATION")

package com.wechallenge.review.configuration

import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import java.net.InetAddress

@Configuration
@EnableElasticsearchRepositories(basePackages = ["com.wechallenge.review.repository"])
@ComponentScan
class ESConfiguration {

    @Bean
    fun client(): Client {
        val settings = Settings.builder()
                .put("cluster.name", "docker-cluster")
                .put("node.name", "es01").build()
        val client = PreBuiltTransportClient(settings)
        client.addTransportAddress(TransportAddress(InetAddress.getByName("elasticsearch"), 9300))
        return client
    }

    @Bean
    fun elasticsearchTemplate(): ElasticsearchOperations {
        return ElasticsearchTemplate(client())
    }
}