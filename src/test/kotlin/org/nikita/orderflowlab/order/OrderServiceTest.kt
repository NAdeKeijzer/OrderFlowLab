package org.nikita.orderflowlab.order

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.order.dto.CreateOrderLineRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest
class OrderServiceTest @Autowired constructor(
    private val orderService: OrderService
) {

    @Test
    fun `creates order with CREATED status`() {
        val customerId = UUID.randomUUID()
        val items = listOf(
            CreateOrderLineRequest(
                productId = UUID.randomUUID(),
                quantity = 2
            )
        )

        val order = orderService.createOrder(customerId, items)

        assertThat(order.id).isNotNull()
        assertThat(order.customerId).isEqualTo(customerId)
        assertThat(order.status).isEqualTo(OrderStatus.CREATED)
        assertThat(order.createdAt).isNotNull()
    }

    @Test
    fun `can retrieve created order`() {
        val customerId = UUID.randomUUID()
        val items = listOf(
            CreateOrderLineRequest(
                productId = UUID.randomUUID(),
                quantity = 2
            )
        )

        val createdOrder = orderService.createOrder(customerId, items)

        val foundOrder = orderService.getOrder(createdOrder.id)

        assertThat(foundOrder).isNotNull()
        assertThat(foundOrder?.id).isEqualTo(createdOrder.id)
    }

    @Test
    fun `creates order with order lines`() {
        val customerId = UUID.randomUUID()
        val productId = UUID.randomUUID()

        val items = listOf(
            CreateOrderLineRequest(productId = productId, quantity = 2)
        )

        val order = orderService.createOrder(customerId, items)

        assertThat(order.lines).hasSize(1)
        assertThat(order.lines.first().productId).isEqualTo(productId)
        assertThat(order.lines.first().quantity).isEqualTo(2)
    }
}