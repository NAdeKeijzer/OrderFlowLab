package org.nikita.orderflowlab.inventory

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.order.event.OrderCreatedLineEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@DataJpaTest
class InventoryReservationServiceTest @Autowired constructor(
    private val inventoryReservationRepository: InventoryReservationRepository
) {

    @Test
    fun `creates inventory reservations for order lines`() {
        val service = InventoryReservationService(inventoryReservationRepository)

        val productId1 = UUID.randomUUID()
        val productId2 = UUID.randomUUID()

        val event = OrderCreatedEvent(
            orderId = UUID.randomUUID(),
            customerId = UUID.randomUUID(),
            totalPrice = BigDecimal("25.48"),
            createdAt = Instant.parse("2026-05-13T12:42:29Z"),
            lines = listOf(
                OrderCreatedLineEvent(
                    productId = productId1,
                    quantity = 2
                ),
                OrderCreatedLineEvent(
                    productId = productId2,
                    quantity = 1
                )
            )
        )

        service.reserveFor(event)

        val reservations = inventoryReservationRepository.findAll()

        assertThat(reservations).hasSize(2)
        assertThat(reservations.map { it.productId })
            .containsExactlyInAnyOrder(productId1, productId2)
        assertThat(reservations.map { it.quantity })
            .containsExactlyInAnyOrder(2, 1)
    }
}