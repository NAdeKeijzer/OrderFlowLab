package org.nikita.orderflowlab.order.exception

import java.util.UUID

class OrderAlreadyPaidException(orderId: UUID) :
    OrderDomainException("Order $orderId is already paid")