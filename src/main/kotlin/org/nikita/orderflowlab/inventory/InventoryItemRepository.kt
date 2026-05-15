package org.nikita.orderflowlab.inventory

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface InventoryItemRepository : JpaRepository<InventoryItem, UUID>