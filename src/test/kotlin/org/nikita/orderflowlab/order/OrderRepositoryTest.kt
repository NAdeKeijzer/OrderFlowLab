package org.nikita.orderflowlab.order

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.util.UUID

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
                    quantity = 2
                )
            )
        )

        val savedOrder = orderRepository.save(order)

        entityManager.flush()
        entityManager.clear()

        val foundOrder = orderRepository.findById(savedOrder.id).orElseThrow()

        assertThat(foundOrder.id).isEqualTo(savedOrder.id)
        assertThat(foundOrder.lines).hasSize(1)
        assertThat(foundOrder.lines.first().productId).isEqualTo(productId)
    }

    @Test
    fun `findAll loads orders with lines`() {
        val productId = UUID.randomUUID()

        val order = Order.create(
            customerId = UUID.randomUUID(),
            items = listOf(
                OrderLineInput(
                    productId = productId,
                    quantity = 2
                )
            )
        )

        orderRepository.save(order)

        entityManager.flush()
        entityManager.clear()

        val orders = orderRepository.findAll()

        assertThat(orders).isNotEmpty
        assertThat(orders.first().lines).isNotEmpty
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
                    quantity = 2
                )
            )
        )

        val savedOrder = orderRepository.save(order)

        entityManager.flush()
        entityManager.clear()

        val foundOrder = orderRepository.findById(savedOrder.id).orElseThrow()

        assertThatCode {
            foundOrder.lines.first().productId
        }.doesNotThrowAnyException()
    }
}