package org.nikita.orderflowlab.inventory

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.inventory.exception.InsufficientInventoryException
import org.nikita.orderflowlab.inventory.model.InventoryItem
import org.nikita.orderflowlab.inventory.repository.InventoryItemRepository
import org.nikita.orderflowlab.inventory.repository.InventoryReservationRepository
import org.nikita.orderflowlab.inventory.service.InventoryReservationService
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.order.event.OrderCreatedLineEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import kotlin.jvm.java

@DataJpaTest
class InventoryReservationServiceTest @Autowired constructor(
    private val inventoryReservationRepository: InventoryReservationRepository,
    private val inventoryItemRepository: InventoryItemRepository
) {

    @Test
    fun `reserves stock when enough inventory exists`() {
        val service = InventoryReservationService(
            inventoryReservationRepository,
            inventoryItemRepository
        )

        val productId = UUID.randomUUID()

        inventoryItemRepository.save(
            InventoryItem(
                productId = productId,
                availableQuantity = 5
            )
        )

        val event = orderCreatedEvent(
            productId = productId,
            quantity = 2
        )

        service.reserveFor(event)

        val updatedItem = inventoryItemRepository.findById(productId).orElseThrow()
        val reservations = inventoryReservationRepository.findAll()

        assertThat(updatedItem.availableQuantity).isEqualTo(3)
        assertThat(reservations).hasSize(1)
        assertThat(reservations.first().productId).isEqualTo(productId)
        assertThat(reservations.first().quantity).isEqualTo(2)
    }

    @Test
    fun `fails when insufficient stock exists`() {
        val service = InventoryReservationService(
            inventoryReservationRepository,
            inventoryItemRepository
        )

        val productId = UUID.randomUUID()

        inventoryItemRepository.save(
            InventoryItem(
                productId = productId,
                availableQuantity = 1
            )
        )

        val event = orderCreatedEvent(
            productId = productId,
            quantity = 2
        )

        assertThatThrownBy {
            service.reserveFor(event)
        }.isInstanceOf(InsufficientInventoryException::class.java)

        val updatedItem = inventoryItemRepository.findById(productId).orElseThrow()
        val reservations = inventoryReservationRepository.findAll()

        assertThat(updatedItem.availableQuantity).isEqualTo(1)
        assertThat(reservations).isEmpty()
    }

    private fun orderCreatedEvent(
        productId: UUID,
        quantity: Int
    ): OrderCreatedEvent =
        OrderCreatedEvent(
            orderId = UUID.randomUUID(),
            customerId = UUID.randomUUID(),
            totalPrice = BigDecimal("25.48"),
            createdAt = Instant.parse("2026-05-13T12:42:29Z"),
            lines = listOf(
                OrderCreatedLineEvent(
                    productId = productId,
                    quantity = quantity
                )
            )
        )
}