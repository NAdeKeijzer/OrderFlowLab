package org.nikita.orderflowlab.order.workflow

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.inventory.event.InventoryReservedEvent
import org.nikita.orderflowlab.inventory.event.InventoryReservedEventHandler
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
import org.nikita.orderflowlab.payment.event.PaymentRequestedEvent
import org.nikita.orderflowlab.payment.event.PaymentRequestedEventHandler
import org.nikita.orderflowlab.payment.event.PaymentSucceededEvent
import org.nikita.orderflowlab.payment.event.PaymentSucceededEventHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@SpringBootTest
@ActiveProfiles("test")
@Import(
    NoOpOrderEventPublisher::class,
    NoOpInventoryEventPublisher::class,
    NoOpPaymentEventPublisher::class
)
class OrderPaymentSuccessFlowTest @Autowired constructor(
    private val orderService: OrderService,
    private val orderCreatedEventHandler: OrderCreatedEventHandler,
    private val inventoryReservedEventHandler: InventoryReservedEventHandler,
    private val paymentRequestedEventHandler: PaymentRequestedEventHandler,
    private val paymentSucceededEventHandler: PaymentSucceededEventHandler,
    private val inventoryItemRepository: InventoryItemRepository
) {

    @Test
    fun `confirms order after inventory reservation and successful payment`() {
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

        inventoryReservedEventHandler.handle(
            InventoryReservedEvent(
                orderId = order.id,
                customerId = order.customerId,
                totalPrice = order.total(),
                reservedAt = Instant.now()
            )
        )

        paymentRequestedEventHandler.handle(
            PaymentRequestedEvent(
                orderId = order.id,
                customerId = order.customerId,
                amount = order.total(),
                requestedAt = Instant.now()
            )
        )

        paymentSucceededEventHandler.handle(
            PaymentSucceededEvent(
                orderId = order.id,
                paidAt = Instant.now()
            )
        )

        val confirmedOrder = orderService.getOrder(order.id)

        assertThat(confirmedOrder.status).isEqualTo(OrderStatus.CONFIRMED)
    }
}