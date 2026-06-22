package org.nikita.orderflowlab.config

import org.apache.kafka.common.serialization.StringDeserializer
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
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer

@Configuration
@Profile("postgres")
class KafkaConsumerConfig {

    @Bean
    fun orderCreatedEventConsumerFactory(
        kafkaProperties: KafkaProperties
    ): ConsumerFactory<String, OrderCreatedEvent> {
        val props = kafkaProperties.buildConsumerProperties()

        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            JacksonJsonDeserializer<OrderCreatedEvent>(
                OrderCreatedEvent::class.java
            )
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
        kafkaProperties: KafkaProperties
    ): ConsumerFactory<String, InventoryReservedEvent> {
        val props = kafkaProperties.buildConsumerProperties()

        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            JacksonJsonDeserializer<InventoryReservedEvent>(
                InventoryReservedEvent::class.java
            )
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
        kafkaProperties: KafkaProperties
    ): ConsumerFactory<String, InventoryReservationFailedEvent> {
        val props = kafkaProperties.buildConsumerProperties()

        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            JacksonJsonDeserializer<InventoryReservationFailedEvent>(
                InventoryReservationFailedEvent::class.java
            )
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
        kafkaProperties: KafkaProperties
    ): ConsumerFactory<String, PaymentRequestedEvent> {
        val props = kafkaProperties.buildConsumerProperties()

        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            JacksonJsonDeserializer<PaymentRequestedEvent>(
                PaymentRequestedEvent::class.java
            )
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
        kafkaProperties: KafkaProperties
    ): ConsumerFactory<String, PaymentSucceededEvent> {
        val props = kafkaProperties.buildConsumerProperties()

        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            JacksonJsonDeserializer<PaymentSucceededEvent>(
                PaymentSucceededEvent::class.java
            )
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

    @Bean
    fun paymentFailedEventConsumerFactory(
        kafkaProperties: KafkaProperties
    ): ConsumerFactory<String, PaymentFailedEvent> {

        val props = kafkaProperties.buildConsumerProperties()

        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            JacksonJsonDeserializer<PaymentFailedEvent>(
                PaymentFailedEvent::class.java
            )
        )
    }

    @Bean
    fun paymentFailedEventKafkaListenerContainerFactory(
        paymentFailedEventConsumerFactory: ConsumerFactory<String, PaymentFailedEvent>
    ): ConcurrentKafkaListenerContainerFactory<String, PaymentFailedEvent> {

        val factory =
            ConcurrentKafkaListenerContainerFactory<String, PaymentFailedEvent>()

        factory.setConsumerFactory(paymentFailedEventConsumerFactory)

        return factory
    }
}