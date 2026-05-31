package org.nikita.orderflowlab.order

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.inventory.model.InventoryItem
import org.nikita.orderflowlab.inventory.repository.InventoryItemRepository
import org.nikita.orderflowlab.order.dto.CreateOrderLineRequest
import org.nikita.orderflowlab.order.event.NoOpOrderEventPublisher
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.order.event.OrderCreatedEventHandler
import org.nikita.orderflowlab.order.event.OrderCreatedLineEvent
import org.nikita.orderflowlab.order.model.OrderStatus
import org.nikita.orderflowlab.order.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@Import(NoOpOrderEventPublisher::class)
class OrderInventorySuccessFlowTest @Autowired constructor(
    private val orderService: OrderService,
    private val orderCreatedEventHandler: OrderCreatedEventHandler,
    private val inventoryItemRepository: InventoryItemRepository
) {

    @Test
    fun `confirms order and reduces inventory when reservation succeeds`() {
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

        val event = OrderCreatedEvent(
            orderId = order.id,
            customerId = order.customerId,
            totalPrice = order.total(),
            createdAt = Instant.now(),
            lines = listOf(
                OrderCreatedLineEvent(
                    productId = productId,
                    quantity = 2
                )
            )
        )

        orderCreatedEventHandler.handle(event)

        val updatedOrder = orderService.getOrder(order.id)
        val updatedInventoryItem = inventoryItemRepository.findById(productId).orElseThrow()

        assertThat(updatedOrder.status).isEqualTo(OrderStatus.CONFIRMED)
        assertThat(updatedInventoryItem.availableQuantity).isEqualTo(8)
    }

    @Test
    fun `releases inventory when confirmed order is cancelled`() {
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

        val event = OrderCreatedEvent(
            orderId = order.id,
            customerId = order.customerId,
            totalPrice = order.total(),
            createdAt = Instant.now(),
            lines = listOf(
                OrderCreatedLineEvent(
                    productId = productId,
                    quantity = 2
                )
            )
        )

        orderCreatedEventHandler.handle(event)

        val inventoryAfterReservation = inventoryItemRepository.findById(productId).orElseThrow()
        assertThat(inventoryAfterReservation.availableQuantity).isEqualTo(8)

        val cancelledOrder = orderService.cancel(order.id)

        val inventoryAfterCancellation = inventoryItemRepository.findById(productId).orElseThrow()

        assertThat(cancelledOrder.status).isEqualTo(OrderStatus.CANCELLED)
        assertThat(inventoryAfterCancellation.availableQuantity).isEqualTo(10)
    }
}