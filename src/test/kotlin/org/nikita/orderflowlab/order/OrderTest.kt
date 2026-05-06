package org.nikita.orderflowlab.order

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.order.exception.EmptyOrderException
import org.nikita.orderflowlab.order.exception.InvalidOrderLineQuantityException
import org.nikita.orderflowlab.order.exception.OrderAlreadyPaidException
import org.nikita.orderflowlab.order.exception.PaidOrderCannotBeCancelledException
import java.math.BigDecimal
import java.util.UUID

class OrderTest {

    @Test
    fun `creates order with lines`() {
        val customerId = UUID.randomUUID()
        val productId = UUID.randomUUID()

        val order = Order.create(
            customerId = customerId,
            items = listOf(
                OrderLineInput(
                    productId = productId,
                    quantity = 2,
                    unitPrice = BigDecimal("9.99"),
                )
            )
        )

        assertThat(order.customerId).isEqualTo(customerId)
        assertThat(order.status).isEqualTo(OrderStatus.CREATED)
        assertThat(order.lines).hasSize(1)
        assertThat(order.lines.first().productId).isEqualTo(productId)
        assertThat(order.lines.first().quantity).isEqualTo(2)
    }

    @Test
    fun `rejects empty order`() {
        assertThatThrownBy {
            Order.create(
                customerId = UUID.randomUUID(),
                items = emptyList()
            )
        }.isInstanceOf(EmptyOrderException::class.java)
    }

    @Test
    fun `rejects order line with invalid quantity`() {
        assertThatThrownBy {
            Order.create(
                customerId = UUID.randomUUID(),
                items = listOf(
                    OrderLineInput(
                        productId = UUID.randomUUID(),
                        quantity = 0,
                        unitPrice = BigDecimal("9.99"),
                    )
                )
            )
        }.isInstanceOf(InvalidOrderLineQuantityException::class.java)
    }

    @Test
    fun `marks order as paid`() {
        val order = validOrder()

        order.markAsPaid()

        assertThat(order.status).isEqualTo(OrderStatus.PAID)
    }

    @Test
    fun `cannot pay order twice`() {
        val order = validOrder()
        order.markAsPaid()

        assertThatThrownBy {
            order.markAsPaid()
        }.isInstanceOf(OrderAlreadyPaidException::class.java)
    }

    @Test
    fun `cancels order`() {
        val order = validOrder()

        order.cancel()

        assertThat(order.status).isEqualTo(OrderStatus.CANCELLED)
    }

    @Test
    fun `cannot cancel paid order`() {
        val order = validOrder()
        order.markAsPaid()

        assertThatThrownBy {
            order.cancel()
        }.isInstanceOf(PaidOrderCannotBeCancelledException::class.java)
    }

    private fun validOrder(): Order =
        Order.create(
            customerId = UUID.randomUUID(),
            items = listOf(
                OrderLineInput(
                    productId = UUID.randomUUID(),
                    quantity = 2,
                    unitPrice = BigDecimal("9.99")
                )
            )
        )
}