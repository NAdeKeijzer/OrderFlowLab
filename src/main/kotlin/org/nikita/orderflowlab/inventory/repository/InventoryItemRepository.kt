package org.nikita.orderflowlab.inventory.repository

import org.nikita.orderflowlab.inventory.model.InventoryItem
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface InventoryItemRepository : JpaRepository<InventoryItem, UUID>