package org.nikita.orderflowlab.order

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

class OrderLineTest {

    @Test
    fun `creates order line with product and quantity`() {
        val order = Order.create(
            customerId = UUID.randomUUID(),
            items = listOf(
                OrderLineInput(
                    productId = UUID.randomUUID(),
                    quantity = 1,
                    unitPrice = BigDecimal("9.99")
                )
            )
        )

        val line = order.lines.first()

        assertThat(line.productId).isNotNull()
        assertThat(line.quantity).isEqualTo(1)
        assertThat(line.order).isEqualTo(order)
    }
}