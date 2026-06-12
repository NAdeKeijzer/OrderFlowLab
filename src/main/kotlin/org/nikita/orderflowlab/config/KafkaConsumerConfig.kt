package org.nikita.orderflowlab.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.StringDeserializer
import org.nikita.orderflowlab.inventory.event.InventoryReservationFailedEvent
import org.nikita.orderflowlab.inventory.event.InventoryReservedEvent
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.payment.event.PaymentRequestedEvent
import org.nikita.orderflowlab.payment.event.PaymentSucceededEvent
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

        deserializer.addTrustedPackages("org.nikita.orderflowlab.order.event")

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
        val factory = ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent>()
        factory.setConsumerFactory(orderCreatedEventConsumerFactory)
        return factory
    }

    @Bean
    fun inventoryReservedEventConsumerFactory(
        kafkaProperties: KafkaProperties,
        objectMapper: ObjectMapper
    ): ConsumerFactory<String, InventoryReservedEvent> {
        val props = kafkaProperties.buildConsumerProperties()

        val deserializer = JsonDeserializer(
            InventoryReservedEvent::class.java,
            objectMapper
        )

        deserializer.addTrustedPackages("org.nikita.orderflowlab.inventory.event")

        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            deserializer
        )
    }

    @Bean
    fun inventoryReservedEventKafkaListenerContainerFactory(
        inventoryReservedEventConsumerFactory: ConsumerFactory<String, InventoryReservedEvent>
    ): ConcurrentKafkaListenerContainerFactory<String, InventoryReservedEvent> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, InventoryReservedEvent>()
        factory.setConsumerFactory(inventoryReservedEventConsumerFactory)
        return factory
    }

    @Bean
    fun inventoryReservationFailedEventConsumerFactory(
        kafkaProperties: KafkaProperties,
        objectMapper: ObjectMapper
    ): ConsumerFactory<String, InventoryReservationFailedEvent> {
        val props = kafkaProperties.buildConsumerProperties()

        val deserializer = JsonDeserializer(
            InventoryReservationFailedEvent::class.java,
            objectMapper
        )

        deserializer.addTrustedPackages("org.nikita.orderflowlab.inventory.event")

        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            deserializer
        )
    }

    @Bean
    fun inventoryReservationFailedEventKafkaListenerContainerFactory(
        inventoryReservationFailedEventConsumerFactory: ConsumerFactory<String, InventoryReservationFailedEvent>
    ): ConcurrentKafkaListenerContainerFactory<String, InventoryReservationFailedEvent> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, InventoryReservationFailedEvent>()
        factory.setConsumerFactory(inventoryReservationFailedEventConsumerFactory)
        return factory
    }

    @Bean
    fun paymentRequestedEventConsumerFactory(
        kafkaProperties: KafkaProperties,
        objectMapper: ObjectMapper
    ): ConsumerFactory<String, PaymentRequestedEvent> {
        val props = kafkaProperties.buildConsumerProperties()

        val deserializer = JsonDeserializer(
            PaymentRequestedEvent::class.java,
            objectMapper
        )

        deserializer.addTrustedPackages("org.nikita.orderflowlab.payment.event")

        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            deserializer
        )
    }

    @Bean
    fun paymentRequestedEventKafkaListenerContainerFactory(
        paymentRequestedEventConsumerFactory: ConsumerFactory<String, PaymentRequestedEvent>
    ): ConcurrentKafkaListenerContainerFactory<String, PaymentRequestedEvent> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, PaymentRequestedEvent>()
        factory.setConsumerFactory(paymentRequestedEventConsumerFactory)
        return factory
    }

    @Bean
    fun paymentSucceededEventConsumerFactory(
        kafkaProperties: KafkaProperties,
        objectMapper: ObjectMapper
    ): ConsumerFactory<String, PaymentSucceededEvent> {
        val props = kafkaProperties.buildConsumerProperties()

        val deserializer = JsonDeserializer(
            PaymentSucceededEvent::class.java,
            objectMapper
        )

        deserializer.addTrustedPackages("org.nikita.orderflowlab.payment.event")

        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            deserializer
        )
    }

    @Bean
    fun paymentSucceededEventKafkaListenerContainerFactory(
        paymentSucceededEventConsumerFactory: ConsumerFactory<String, PaymentSucceededEvent>
    ): ConcurrentKafkaListenerContainerFactory<String, PaymentSucceededEvent> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, PaymentSucceededEvent>()
        factory.setConsumerFactory(paymentSucceededEventConsumerFactory)
        return factory
    }
}