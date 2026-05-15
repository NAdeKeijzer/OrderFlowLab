package org.nikita.orderflowlab.inventory.exception

import java.util.*

class InventoryItemNotFoundException(
    productId: UUID
) : RuntimeException("Inventory item not found for product $productId")