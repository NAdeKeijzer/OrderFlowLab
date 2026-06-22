package org.nikita.orderflowlab.config

import org.apache.kafka.common.serialization.StringSerializer
import org.nikita.orderflowlab.inventory.event.InventoryReservationFailedEvent
import org.nikita.orderflowlab.inventory.event.InventoryReservedEvent
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.payment.event.PaymentFailedEvent
import org.nikita.orderflowlab.payment.event.PaymentRequestedEvent
import org.nikita.orderflowlab.payment.event.PaymentSucceededEvent
import org.springframework.boot.kafka.autoconfigure.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JacksonJsonSerializer

@Configuration
@Profile("postgres")
class KafkaProducerConfig {

    @Bean
    fun orderCreatedEventProducerFactory(
        kafkaProperties: KafkaProperties,
    ): ProducerFactory<String, OrderCreatedEvent> {

        val props = kafkaProperties.buildProducerProperties()

        return DefaultKafkaProducerFactory(
            props,
            StringSerializer(),
            JacksonJsonSerializer()
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
    ): ProducerFactory<String, InventoryReservedEvent> {

        val props = kafkaProperties.buildProducerProperties()

        return DefaultKafkaProducerFactory(
            props,
            StringSerializer(),
            JacksonJsonSerializer()
        )
    }

    @Bean
    fun inventoryReservedEventKafkaTemplate(
        producerFactory: ProducerFactory<String, InventoryReservedEvent>
    ): KafkaTemplate<String, InventoryReservedEvent> =
        KafkaTemplate(producerFactory)

    @Bean
    fun inventoryReservationFailedEventProducerFactory(
        kafkaProperties: KafkaProperties
    ): ProducerFactory<String, InventoryReservationFailedEvent> {

        val props = kafkaProperties.buildProducerProperties()

        return DefaultKafkaProducerFactory(
            props,
            StringSerializer(),
            JacksonJsonSerializer()
        )
    }

    @Bean
    fun inventoryReservationFailedEventKafkaTemplate(
        producerFactory: ProducerFactory<String, InventoryReservationFailedEvent>
    ): KafkaTemplate<String, InventoryReservationFailedEvent> =
        KafkaTemplate(producerFactory)

    @Bean
    fun paymentRequestedEventProducerFactory(
        kafkaProperties: KafkaProperties
    ): ProducerFactory<String, PaymentRequestedEvent> {

        val props = kafkaProperties.buildProducerProperties()

        return DefaultKafkaProducerFactory(
            props,
            StringSerializer(),
            JacksonJsonSerializer()
        )
    }

    @Bean
    fun paymentRequestedEventKafkaTemplate(
        producerFactory: ProducerFactory<String, PaymentRequestedEvent>
    ): KafkaTemplate<String, PaymentRequestedEvent> =
        KafkaTemplate(producerFactory)

    @Bean
    fun paymentSucceededEventProducerFactory(
        kafkaProperties: KafkaProperties
    ): ProducerFactory<String, PaymentSucceededEvent> {

        val props = kafkaProperties.buildProducerProperties()

        return DefaultKafkaProducerFactory(
            props,
            StringSerializer(),
            JacksonJsonSerializer()
        )
    }

    @Bean
    fun paymentSucceededEventKafkaTemplate(
        producerFactory: ProducerFactory<String, PaymentSucceededEvent>
    ): KafkaTemplate<String, PaymentSucceededEvent> =
        KafkaTemplate(producerFactory)

    @Bean
    fun paymentFailedEventProducerFactory(
        kafkaProperties: KafkaProperties
    ): ProducerFactory<String, PaymentFailedEvent> {

        val props = kafkaProperties.buildProducerProperties()

        return DefaultKafkaProducerFactory(
            props,
            StringSerializer(),
            JacksonJsonSerializer()
        )
    }

    @Bean
    fun paymentFailedEventKafkaTemplate(
        producerFactory: ProducerFactory<String, PaymentFailedEvent>
    ): KafkaTemplate<String, PaymentFailedEvent> =
        KafkaTemplate(producerFactory)
}