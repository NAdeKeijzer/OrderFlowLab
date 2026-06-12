package org.nikita.orderflowlab.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.StringSerializer
import org.nikita.orderflowlab.inventory.event.InventoryReservationFailedEvent
import org.nikita.orderflowlab.inventory.event.InventoryReservedEvent
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.payment.event.PaymentRequestedEvent
import org.springframework.boot.kafka.autoconfigure.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
@Profile("postgres")
class KafkaProducerConfig {

    @Bean
    fun orderCreatedEventProducerFactory(
        kafkaProperties: KafkaProperties,
        objectMapper: ObjectMapper
    ): ProducerFactory<String, OrderCreatedEvent> {

        val props = kafkaProperties.buildProducerProperties()

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

    @Bean
    fun inventoryReservedEventProducerFactory(
        kafkaProperties: KafkaProperties,
        objectMapper: ObjectMapper
    ): ProducerFactory<String, InventoryReservedEvent> {

        val props = kafkaProperties.buildProducerProperties()

        return DefaultKafkaProducerFactory(
            props,
            StringSerializer(),
            JsonSerializer(objectMapper)
        )
    }

    @Bean
    fun inventoryReservedEventKafkaTemplate(
        producerFactory: ProducerFactory<String, InventoryReservedEvent>
    ): KafkaTemplate<String, InventoryReservedEvent> =
        KafkaTemplate(producerFactory)

    @Bean
    fun inventoryReservationFailedEventProducerFactory(
        kafkaProperties: KafkaProperties,
        objectMapper: ObjectMapper
    ): ProducerFactory<String, InventoryReservationFailedEvent> {

        val props = kafkaProperties.buildProducerProperties()

        return DefaultKafkaProducerFactory(
            props,
            StringSerializer(),
            JsonSerializer(objectMapper)
        )
    }

    @Bean
    fun inventoryReservationFailedEventKafkaTemplate(
        producerFactory: ProducerFactory<String, InventoryReservationFailedEvent>
    ): KafkaTemplate<String, InventoryReservationFailedEvent> =
        KafkaTemplate(producerFactory)

    @Bean
    fun paymentRequestedEventProducerFactory(
        kafkaProperties: KafkaProperties,
        objectMapper: ObjectMapper
    ): ProducerFactory<String, PaymentRequestedEvent> {

        val props = kafkaProperties.buildProducerProperties()

        return DefaultKafkaProducerFactory(
            props,
            StringSerializer(),
            JsonSerializer(objectMapper)
        )
    }

    @Bean
    fun paymentRequestedEventKafkaTemplate(
        producerFactory: ProducerFactory<String, PaymentRequestedEvent>
    ): KafkaTemplate<String, PaymentRequestedEvent> =
        KafkaTemplate(producerFactory)
}