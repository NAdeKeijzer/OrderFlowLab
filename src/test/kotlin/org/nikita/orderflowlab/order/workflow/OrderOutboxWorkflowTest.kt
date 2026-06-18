package org.nikita.orderflowlab.order.workflow

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.inventory.event.NoOpInventoryEventPublisher
import org.nikita.orderflowlab.inventory.model.InventoryItem
import org.nikita.orderflowlab.inventory.repository.InventoryItemRepository
import org.nikita.orderflowlab.order.dto.CreateOrderLineRequest
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.order.event.OrderCreatedEventHandler
import org.nikita.orderflowlab.order.event.OrderEventPublisher
import org.nikita.orderflowlab.order.model.OrderStatus
import org.nikita.orderflowlab.order.service.OrderService
import org.nikita.orderflowlab.outbox.repository.OutboxEventRepository
import org.nikita.orderflowlab.outbox.service.OutboxPublisherService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@Import(
    NoOpInventoryEventPublisher::class,
    OrderOutboxWorkflowTest.TestOrderEventPublisherConfig::class
)
class OrderOutboxWorkflowTest @Autowired constructor(
    private val orderService: OrderService,
    private val outboxPublisherService: OutboxPublisherService,
    private val outboxEventRepository: OutboxEventRepository,
    private val inventoryItemRepository: InventoryItemRepository
) {

    @Test
    fun `publishes order created event from outbox and starts inventory reservation workflow`() {
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

        val pendingOutboxEvents = outboxEventRepository.findAllByPublishedAtIsNull()
            .filter { it.aggregateId == order.id }

        assertThat(order.status).isEqualTo(OrderStatus.CREATED)
        assertThat(pendingOutboxEvents).hasSize(1)
        assertThat(pendingOutboxEvents.first().eventType).isEqualTo("ORDER_CREATED")

        outboxPublisherService.publishPendingEvents()

        val updatedOrder = orderService.getOrder(order.id)
        val updatedInventoryItem = inventoryItemRepository.findById(productId).orElseThrow()
        val publishedOutboxEvent = outboxEventRepository.findById(pendingOutboxEvents.first().id).orElseThrow()

        assertThat(updatedOrder.status).isEqualTo(OrderStatus.INVENTORY_RESERVED)
        assertThat(updatedInventoryItem.availableQuantity).isEqualTo(8)
        assertThat(publishedOutboxEvent.publishedAt).isNotNull()
    }

    @TestConfiguration
    class TestOrderEventPublisherConfig {

        @Bean
        @Primary
        fun orderEventPublisher(
            orderCreatedEventHandler: OrderCreatedEventHandler
        ): OrderEventPublisher =
            object : OrderEventPublisher {
                override fun publishOrderCreated(event: OrderCreatedEvent) {
                    orderCreatedEventHandler.handle(event)
                }
            }
    }
}