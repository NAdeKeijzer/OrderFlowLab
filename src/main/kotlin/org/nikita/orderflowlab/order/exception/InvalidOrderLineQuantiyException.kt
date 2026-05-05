package org.nikita.orderflowlab.order.exception

class InvalidOrderLineQuantityException(
    quantity: Int
) : RuntimeException("Quantity must be greater than 0, but was $quantity")