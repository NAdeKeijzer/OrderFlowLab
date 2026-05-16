package org.nikita.orderflowlab.order.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.order.model.Order
import org.nikita.orderflowlab.order.model.OrderLineInput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest @Autowired constructor(
    private val orderRepository: OrderRepository,
    private val entityManager: TestEntityManager
) {

    @Test
    fun `findById loads order with lines`() {
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

        val savedOrder = orderRepository.save(order)

        entityManager.flush()
        entityManager.clear()

        val foundOrder = orderRepository.findById(savedOrder.id).orElseThrow()

        Assertions.assertThat(foundOrder.id).isEqualTo(savedOrder.id)
        Assertions.assertThat(foundOrder.lines).hasSize(1)
        Assertions.assertThat(foundOrder.lines.first().productId).isEqualTo(productId)
    }

    @Test
    fun `findAll loads orders with lines`() {
        val order = Order.create(
            customerId = UUID.randomUUID(),
            items = listOf(
                OrderLineInput(
                    productId = UUID.randomUUID(),
                    quantity = 2,
                    unitPrice = BigDecimal("9.99")
                )
            )
        )

        orderRepository.save(order)

        entityManager.flush()
        entityManager.clear()

        val orders = orderRepository.findAll()

        Assertions.assertThat(orders).isNotEmpty
        Assertions.assertThat(orders.first().lines).isNotEmpty
    }

    @Test
    fun `loaded order lines can be accessed after persistence context is cleared`() {
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

        val savedOrder = orderRepository.save(order)

        entityManager.flush()
        entityManager.clear()

        val foundOrder = orderRepository.findById(savedOrder.id).orElseThrow()

        Assertions.assertThatCode {
            foundOrder.lines.first().productId
        }.doesNotThrowAnyException()
    }
}