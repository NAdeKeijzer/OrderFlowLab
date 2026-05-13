package org.nikita.orderflowlab.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.StringDeserializer
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.springframework.boot.kafka.autoconfigure.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
@Profile("postgres")
class KafkaConsumerConfig {

    @Bean
    fun orderCreatedEventConsumerFactory(
        kafkaProperties: KafkaProperties,
        objectMapper: ObjectMapper
    ): ConsumerFactory<String, OrderCreatedEvent> {

        val props = kafkaProperties.buildConsumerProperties()

        val deserializer = JsonDeserializer(
            OrderCreatedEvent::class.java,
            objectMapper
        )

        deserializer.addTrustedPackages(
            "org.nikita.orderflowlab.order.event"
        )

        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            deserializer
        )
    }

    @Bean
    fun orderCreatedEventKafkaListenerContainerFactory(
        orderCreatedEventConsumerFactory: ConsumerFactory<String, OrderCreatedEvent>
    ): ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> {

        val factory =
            ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent>()

        factory.setConsumerFactory(orderCreatedEventConsumerFactory)

        return factory
    }
}