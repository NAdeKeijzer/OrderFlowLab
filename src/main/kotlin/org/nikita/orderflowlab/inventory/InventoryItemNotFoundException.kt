package org.nikita.orderflowlab.inventory

import java.util.UUID

class InventoryItemNotFoundException(
    productId: UUID
) : RuntimeException("Inventory item not found for product $productId")