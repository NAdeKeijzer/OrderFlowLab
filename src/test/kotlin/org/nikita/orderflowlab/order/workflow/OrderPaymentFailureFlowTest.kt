package org.nikita.orderflowlab.order.workflow

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.inventory.event.NoOpInventoryEventPublisher
import org.nikita.orderflowlab.inventory.model.InventoryItem
import org.nikita.orderflowlab.inventory.repository.InventoryItemRepository
import org.nikita.orderflowlab.order.dto.CreateOrderLineRequest
import org.nikita.orderflowlab.order.event.NoOpOrderEventPublisher
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.order.event.OrderCreatedEventHandler
import org.nikita.orderflowlab.order.event.OrderCreatedLineEvent
import org.nikita.orderflowlab.order.model.OrderStatus
import org.nikita.orderflowlab.order.service.OrderService
import org.nikita.orderflowlab.payment.event.NoOpPaymentEventPublisher
import org.nikita.orderflowlab.payment.event.PaymentFailedEvent
import org.nikita.orderflowlab.payment.event.PaymentFailedEventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@Import(
    NoOpOrderEventPublisher::class,
    NoOpInventoryEventPublisher::class,
    NoOpPaymentEventPublisher::class
)
class OrderPaymentFailureFlowTest @Autowired constructor(
    private val orderService: OrderService,
    private val orderCreatedEventHandler: OrderCreatedEventHandler,
    private val paymentFailedEventHandler: PaymentFailedEventHandler,
    private val inventoryItemRepository: InventoryItemRepository
) {

    @Test
    fun `releases inventory and marks order as payment failed when payment fails`() {
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

        orderCreatedEventHandler.handle(
            OrderCreatedEvent(
                orderId = order.id,
                customerId = order.customerId,
                totalPrice = order.total(),
                createdAt = order.createdAt,
                lines = listOf(
                    OrderCreatedLineEvent(
                        productId = productId,
                        quantity = 2
                    )
                )
            )
        )

        val inventoryReservedOrder = orderService.getOrder(order.id)
        val inventoryAfterReservation = inventoryItemRepository.findById(productId).orElseThrow()

        assertThat(inventoryReservedOrder.status).isEqualTo(OrderStatus.INVENTORY_RESERVED)
        assertThat(inventoryAfterReservation.availableQuantity).isEqualTo(8)

        paymentFailedEventHandler.handle(
            PaymentFailedEvent(
                orderId = order.id,
                failedAt = Instant.now(),
                reason = "Card declined"
            )
        )

        val paymentFailedOrder = orderService.getOrder(order.id)
        val inventoryAfterPaymentFailure = inventoryItemRepository.findById(productId).orElseThrow()

        assertThat(paymentFailedOrder.status).isEqualTo(OrderStatus.PAYMENT_FAILED)
        assertThat(inventoryAfterPaymentFailure.availableQuantity).isEqualTo(10)
    }
}