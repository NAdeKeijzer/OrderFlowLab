package org.nikita.orderflowlab.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.kafka.KafkaContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.util.*

@Testcontainers
class KafkaContainerProducerConsumerTest {

    companion object {
        @Container
        val kafka = KafkaContainer(
            DockerImageName.parse("apache/kafka-native:3.8.0")
        )
    }

    @Test
    fun `produces and consumes message`() {
        val topic = "test-topic-${UUID.randomUUID()}"

        val producer = KafkaProducer<String, String>(
            Properties().apply {
                put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.bootstrapServers)
                put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
                put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
            }
        )

        val consumer = KafkaConsumer<String, String>(
            Properties().apply {
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.bootstrapServers)
                put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-${UUID.randomUUID()}")
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
                put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
            }
        )

        producer.use { p ->
            consumer.use { c ->
                c.subscribe(listOf(topic))

                p.send(
                    ProducerRecord(
                        topic,
                        "test-key",
                        "hello kafka"
                    )
                ).get()

                val records = c.poll(Duration.ofSeconds(10))

                assertThat(records.map { it.value() })
                    .contains("hello kafka")
            }
        }
    }
}