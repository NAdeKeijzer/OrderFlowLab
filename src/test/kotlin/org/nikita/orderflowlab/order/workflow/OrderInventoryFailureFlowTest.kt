package org.nikita.orderflowlab.order.workflow

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.inventory.event.NoOpInventoryEventPublisher
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
@Import(
    NoOpOrderEventPublisher::class,
    NoOpInventoryEventPublisher::class
)
class OrderInventoryFailureFlowTest @Autowired constructor(
    private val orderService: OrderService,
    private val orderCreatedEventHandler: OrderCreatedEventHandler,
) {

    @Test
    fun `marks order as inventory failed when reservation fails`() {
        val productId = UUID.randomUUID()

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

        Assertions.assertThat(updatedOrder.status).isEqualTo(OrderStatus.INVENTORY_FAILED)
    }
}