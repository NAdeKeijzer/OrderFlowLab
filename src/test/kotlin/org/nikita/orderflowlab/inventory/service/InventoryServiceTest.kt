package org.nikita.orderflowlab.inventory.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.inventory.exception.InventoryItemNotFoundException
import org.nikita.orderflowlab.inventory.model.InventoryItem
import org.nikita.orderflowlab.inventory.repository.InventoryItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import java.util.*

@DataJpaTest
class InventoryServiceTest @Autowired constructor(
    private val inventoryItemRepository: InventoryItemRepository
) {

    @Test
    fun `creates inventory item`() {
        val service = InventoryService(inventoryItemRepository)
        val productId = UUID.randomUUID()

        val item = service.createOrUpdateItem(
            productId = productId,
            availableQuantity = 10
        )

        assertThat(item.productId).isEqualTo(productId)
        assertThat(item.availableQuantity).isEqualTo(10)
    }

    @Test
    fun `updates existing inventory item`() {
        val service = InventoryService(inventoryItemRepository)
        val productId = UUID.randomUUID()

        inventoryItemRepository.save(
            InventoryItem(
                productId = productId,
                availableQuantity = 10
            )
        )

        val item = service.createOrUpdateItem(
            productId = productId,
            availableQuantity = 7
        )

        assertThat(item.productId).isEqualTo(productId)
        assertThat(item.availableQuantity).isEqualTo(7)
        assertThat(inventoryItemRepository.findAll()).hasSize(1)
    }

    @Test
    fun `throws when inventory item does not exist`() {
        val service = InventoryService(inventoryItemRepository)

        assertThatThrownBy {
            service.getItem(UUID.randomUUID())
        }.isInstanceOf(InventoryItemNotFoundException::class.java)
    }
}