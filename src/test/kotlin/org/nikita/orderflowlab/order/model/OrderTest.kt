package org.nikita.orderflowlab.order.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.order.exception.EmptyOrderException
import org.nikita.orderflowlab.order.exception.InvalidOrderLineQuantityException
import org.nikita.orderflowlab.order.exception.OrderAlreadyPaidException
import org.nikita.orderflowlab.order.exception.PaidOrderCannotBeCancelledException
import java.math.BigDecimal
import java.util.*

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
                    unitPrice = BigDecimal("9.99")
                )
            )
        )

        Assertions.assertThat(order.customerId).isEqualTo(customerId)
        Assertions.assertThat(order.status).isEqualTo(OrderStatus.CREATED)
        Assertions.assertThat(order.lines).hasSize(1)
        Assertions.assertThat(order.lines.first().productId).isEqualTo(productId)
        Assertions.assertThat(order.lines.first().quantity).isEqualTo(2)
        Assertions.assertThat(order.lines.first().unitPrice).isEqualTo(BigDecimal("9.99"))
    }

    @Test
    fun `rejects empty order`() {
        Assertions.assertThatThrownBy {
            Order.create(
                customerId = UUID.randomUUID(),
                items = emptyList()
            )
        }.isInstanceOf(EmptyOrderException::class.java)
    }

    @Test
    fun `rejects order line with invalid quantity`() {
        Assertions.assertThatThrownBy {
            Order.create(
                customerId = UUID.randomUUID(),
                items = listOf(
                    OrderLineInput(
                        productId = UUID.randomUUID(),
                        quantity = 0,
                        unitPrice = BigDecimal("9.99")
                    )
                )
            )
        }.isInstanceOf(InvalidOrderLineQuantityException::class.java)
    }

    @Test
    fun `marks order as paid`() {
        val order = validOrder()

        order.markAsPaid()

        Assertions.assertThat(order.status).isEqualTo(OrderStatus.PAID)
    }

    @Test
    fun `cannot pay order twice`() {
        val order = validOrder()
        order.markAsPaid()

        Assertions.assertThatThrownBy {
            order.markAsPaid()
        }.isInstanceOf(OrderAlreadyPaidException::class.java)
    }

    @Test
    fun `cancels order`() {
        val order = validOrder()

        order.cancel()

        Assertions.assertThat(order.status).isEqualTo(OrderStatus.CANCELLED)
    }

    @Test
    fun `cannot cancel paid order`() {
        val order = validOrder()
        order.markAsPaid()

        Assertions.assertThatThrownBy {
            order.cancel()
        }.isInstanceOf(PaidOrderCannotBeCancelledException::class.java)
    }

    @Test
    fun `calculates order total`() {
        val order = Order.create(
            customerId = UUID.randomUUID(),
            items = listOf(
                OrderLineInput(
                    productId = UUID.randomUUID(),
                    quantity = 2,
                    unitPrice = BigDecimal("10.00")
                ),
                OrderLineInput(
                    productId = UUID.randomUUID(),
                    quantity = 1,
                    unitPrice = BigDecimal("5.00")
                )
            )
        )

        Assertions.assertThat(order.total()).isEqualTo(BigDecimal("25.00"))
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