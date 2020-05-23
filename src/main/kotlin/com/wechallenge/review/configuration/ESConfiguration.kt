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
                .put("path.home", "/usr/local/Cellar/elasticsearch/7.7.0")
                .put("cluster.name", "docker-cluster").build()
        val client = PreBuiltTransportClient(settings)
        client.addTransportAddress(TransportAddress(InetAddress.getByName("localhost"), 9300))
        return client
    }

    @Bean
    fun elasticsearchTemplate(): ElasticsearchOperations {
        return ElasticsearchTemplate(client())
    }
}