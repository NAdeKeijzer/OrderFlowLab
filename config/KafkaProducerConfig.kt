package org.nikita.orderflowlab.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.StringSerializer
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig {

    @Bean
    fun orderCreatedEventProducerFactory(
        kafkaProperties: KafkaProperties,
        objectMapper: ObjectMapper
    ): ProducerFactory<String, OrderCreatedEvent> {
        val props = kafkaProperties.buildProducerProperties(null)

        return DefaultKafkaProducerFactory(
            props,
            StringSerializer(),
            JsonSerializer(objectMapper)
        )
    }

    @Bean
    fun orderCreatedEventKafkaTemplate(
        producerFactory: ProducerFactory<String, OrderCreatedEvent>
    ): KafkaTemplate<String, OrderCreatedEvent> =
        KafkaTemplate(producerFactory)
}