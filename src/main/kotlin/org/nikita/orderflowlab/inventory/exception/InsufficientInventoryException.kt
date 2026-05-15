package org.nikita.orderflowlab.inventory.exception

import java.util.*

class InsufficientInventoryException(
    productId: UUID,
    requestedQuantity: Int,
    availableQuantity: Int
) : RuntimeException(
    "Insufficient inventory for product $productId. Requested=$requestedQuantity, available=$availableQuantity"
)