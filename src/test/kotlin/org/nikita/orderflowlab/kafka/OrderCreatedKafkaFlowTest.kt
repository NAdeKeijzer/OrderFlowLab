package org.nikita.orderflowlab.order.workflow

import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.inventory.event.NoOpInventoryEventPublisher
import org.nikita.orderflowlab.inventory.model.InventoryItem
import org.nikita.orderflowlab.inventory.repository.InventoryItemRepository
import org.nikita.orderflowlab.order.dto.CreateOrderLineRequest
import org.nikita.orderflowlab.order.model.OrderStatus
import org.nikita.orderflowlab.order.service.OrderService
import org.nikita.orderflowlab.outbox.service.OutboxPublisherService
import org.nikita.orderflowlab.payment.event.NoOpPaymentEventPublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.kafka.KafkaContainer
import org.testcontainers.utility.DockerImageName
import java.math.BigDecimal
import java.time.Duration
import java.util.UUID

@Testcontainers
@SpringBootTest
@ActiveProfiles("test", "postgres")
@Import(
    NoOpInventoryEventPublisher::class,
    NoOpPaymentEventPublisher::class
)
class OrderCreatedKafkaFlowTest @Autowired constructor(
    private val orderService: OrderService,
    private val outboxPublisherService: OutboxPublisherService,
    private val inventoryItemRepository: InventoryItemRepository
) {

    companion object {
        @Container
        @ServiceConnection
        val kafka = KafkaContainer(
            DockerImageName.parse("apache/kafka-native:3.8.0")
        )

        @Container
        @ServiceConnection
        val postgres = PostgreSQLContainer("postgres:16-alpine")
    }

    @Test
    fun `publishes order created event through Kafka and reserves inventory`() {
        val productId = UUID.randomUUID()

        inventoryItemRepository.save(
            InventoryItem(
                productId = productId,
                availableQuantity = 10
            )
        )

        val order = orderService.createOrder(
            customerId = UUID.randomUUID(),
            items = listOf(
                CreateOrderLineRequest(
                    productId = productId,
                    quantity = 2,
                    unitPrice = BigDecimal("9.99")
                )
            )
        )

        outboxPublisherService.publishPendingEvents()

        await()
            .atMost(Duration.ofSeconds(10))
            .untilAsserted {
                val updatedOrder = orderService.getOrder(order.id)
                val updatedInventoryItem = inventoryItemRepository.findById(productId).orElseThrow()

                assertThat(updatedOrder.status).isEqualTo(OrderStatus.INVENTORY_RESERVED)
                assertThat(updatedInventoryItem.availableQuantity).isEqualTo(8)
            }
    }
}