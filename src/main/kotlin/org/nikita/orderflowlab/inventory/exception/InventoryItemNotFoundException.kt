package org.nikita.orderflowlab.inventory.exception

import java.util.*

class InventoryItemNotFoundException(
    productId: UUID
) : InventoryDomainException("Inventory item not found for product $productId")