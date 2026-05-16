package org.nikita.orderflowlab.order.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.order.dto.CreateOrderLineRequest
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.order.event.OrderEventPublisher
import org.nikita.orderflowlab.order.exception.EmptyOrderException
import org.nikita.orderflowlab.order.exception.InvalidOrderLineQuantityException
import org.nikita.orderflowlab.order.exception.OrderAlreadyPaidException
import org.nikita.orderflowlab.order.exception.OrderNotFoundException
import org.nikita.orderflowlab.order.exception.PaidOrderCannotBeCancelledException
import org.nikita.orderflowlab.order.model.OrderStatus
import org.nikita.orderflowlab.order.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import java.math.BigDecimal
import java.util.UUID

@DataJpaTest
class OrderServiceTest @Autowired constructor(
    private val orderRepository: OrderRepository
) {

    private val orderService = OrderService(
        orderRepository = orderRepository,
        orderEventPublisher = object : OrderEventPublisher {
            override fun publishOrderCreated(event: OrderCreatedEvent) {
                // no-op for service tests
            }
        }
    )

    @Test
    fun `creates order with CREATED status`() {
        val order = orderService.createOrder(
            customerId = UUID.randomUUID(),
            items = listOf(validOrderLineRequest())
        )

        assertThat(order.status).isEqualTo(OrderStatus.CREATED)
    }

    @Test
    fun `creates order with order lines`() {
        val productId = UUID.randomUUID()

        val order = orderService.createOrder(
            customerId = UUID.randomUUID(),
            items = listOf(validOrderLineRequest(productId = productId))
        )

        assertThat(order.lines).hasSize(1)
        assertThat(order.lines.first().productId).isEqualTo(productId)
        assertThat(order.lines.first().quantity).isEqualTo(2)
    }

    @Test
    fun `can retrieve created order`() {
        val order = orderService.createOrder(
            customerId = UUID.randomUUID(),
            items = listOf(validOrderLineRequest())
        )

        val foundOrder = orderService.getOrder(order.id)

        assertThat(foundOrder).isNotNull
        assertThat(foundOrder!!.id).isEqualTo(order.id)
    }

    @Test
    fun `throws when order does not exist`() {
        assertThatThrownBy {
            orderService.getOrder(UUID.randomUUID())
        }.isInstanceOf(OrderNotFoundException::class.java)
    }

    @Test
    fun `throws when creating empty order`() {
        assertThatThrownBy {
            orderService.createOrder(
                customerId = UUID.randomUUID(),
                items = emptyList()
            )
        }.isInstanceOf(EmptyOrderException::class.java)
    }

    @Test
    fun `throws when creating order with invalid quantity`() {
        assertThatThrownBy {
            orderService.createOrder(
                customerId = UUID.randomUUID(),
                items = listOf(validOrderLineRequest(quantity = 0))
            )
        }.isInstanceOf(InvalidOrderLineQuantityException::class.java)
    }

    @Test
    fun `can mark order as paid`() {
        val order = orderService.createOrder(
            customerId = UUID.randomUUID(),
            items = listOf(validOrderLineRequest())
        )

        val paidOrder = orderService.pay(order.id)

        assertThat(paidOrder.status).isEqualTo(OrderStatus.PAID)
    }

    @Test
    fun `cannot pay an order twice`() {
        val order = orderService.createOrder(
            customerId = UUID.randomUUID(),
            items = listOf(validOrderLineRequest())
        )

        orderService.pay(order.id)

        assertThatThrownBy {
            orderService.pay(order.id)
        }.isInstanceOf(OrderAlreadyPaidException::class.java)
    }

    @Test
    fun `can cancel order`() {
        val order = orderService.createOrder(
            customerId = UUID.randomUUID(),
            items = listOf(validOrderLineRequest())
        )

        val cancelledOrder = orderService.cancel(order.id)

        assertThat(cancelledOrder.status).isEqualTo(OrderStatus.CANCELLED)
    }

    @Test
    fun `cannot cancel paid order`() {
        val order = orderService.createOrder(
            customerId = UUID.randomUUID(),
            items = listOf(validOrderLineRequest())
        )

        orderService.pay(order.id)

        assertThatThrownBy {
            orderService.cancel(order.id)
        }.isInstanceOf(PaidOrderCannotBeCancelledException::class.java)
    }

    private fun validOrderLineRequest(
        productId: UUID = UUID.randomUUID(),
        quantity: Int = 2,
        unitPrice: BigDecimal = BigDecimal("9.99")
    ): CreateOrderLineRequest =
        CreateOrderLineRequest(
            productId = productId,
            quantity = quantity,
            unitPrice = unitPrice
        )
}