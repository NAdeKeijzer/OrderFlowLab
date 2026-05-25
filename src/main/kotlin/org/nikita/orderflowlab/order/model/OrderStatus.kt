package org.nikita.orderflowlab.order.model

enum class OrderStatus {
    CREATED,
    INVENTORY_RESERVED,
    CONFIRMED,
    PAID,
    CANCELLED,
    INVENTORY_FAILED
}