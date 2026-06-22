package org.nikita.orderflowlab.kafka

import org.junit.jupiter.api.Test
import org.testcontainers.kafka.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
class KafkaContainerSmokeTest {

    companion object {
        @Container
        val kafka = KafkaContainer(
            DockerImageName.parse("apache/kafka-native:3.8.0")
        )
    }

    @Test
    fun `starts kafka container`() {
        assert(kafka.isRunning)
        println("Kafka bootstrap servers: ${kafka.bootstrapServers}")
    }
}