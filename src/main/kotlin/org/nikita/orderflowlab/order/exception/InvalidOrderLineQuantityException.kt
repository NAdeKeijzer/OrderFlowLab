package org.nikita.orderflowlab.order.exception

class InvalidOrderLineQuantityException(
    quantity: Int
) : OrderDomainException("Quantity must be greater than 0, but was $quantity")