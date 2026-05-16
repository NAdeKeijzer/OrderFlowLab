package org.nikita.orderflowlab.order.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

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

        Assertions.assertThat(line.productId).isNotNull()
        Assertions.assertThat(line.quantity).isEqualTo(1)
        Assertions.assertThat(line.order).isEqualTo(order)
    }
}