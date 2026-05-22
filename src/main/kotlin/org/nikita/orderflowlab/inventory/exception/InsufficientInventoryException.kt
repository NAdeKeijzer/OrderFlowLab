package org.nikita.orderflowlab.inventory.exception

import java.util.UUID

class InsufficientInventoryException(
    productId: UUID,
    requestedQuantity: Int,
    availableQuantity: Int
) : InventoryDomainException(
    "Insufficient inventory for product $productId. Requested=$requestedQuantity, available=$availableQuantity"
)