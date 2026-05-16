package org.nikita.orderflowlab.order.exception

import java.util.UUID

class OrderNotFoundException(
    orderId: UUID
) : OrderDomainException("Order not found: $orderId")