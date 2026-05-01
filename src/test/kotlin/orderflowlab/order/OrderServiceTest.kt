package org.nikita.orderflowlab.order

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
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

        val order = orderService.createOrder(customerId)

        assertThat(order.id).isNotNull()
        assertThat(order.customerId).isEqualTo(customerId)
        assertThat(order.status).isEqualTo(OrderStatus.CREATED)
        assertThat(order.createdAt).isNotNull()
    }

    @Test
    fun `can retrieve created order`() {
        val customerId = UUID.randomUUID()
        val createdOrder = orderService.createOrder(customerId)

        val foundOrder = orderService.getOrder(createdOrder.id)

        assertThat(foundOrder).isNotNull()
        assertThat(foundOrder?.id).isEqualTo(createdOrder.id)
    }
}