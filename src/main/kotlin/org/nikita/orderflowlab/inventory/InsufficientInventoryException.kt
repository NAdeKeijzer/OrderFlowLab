package org.nikita.orderflowlab.inventory

import java.util.UUID

class InsufficientInventoryException(
    productId: UUID,
    requestedQuantity: Int,
    availableQuantity: Int
) : RuntimeException(
    "Insufficient inventory for product $productId. Requested=$requestedQuantity, available=$availableQuantity"
)