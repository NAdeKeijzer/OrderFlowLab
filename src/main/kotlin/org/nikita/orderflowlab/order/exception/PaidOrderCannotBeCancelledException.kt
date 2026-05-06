package org.nikita.orderflowlab.order.exception

import java.util.UUID

class PaidOrderCannotBeCancelledException(orderId: UUID) :
    OrderDomainException("Paid order $orderId cannot be cancelled")