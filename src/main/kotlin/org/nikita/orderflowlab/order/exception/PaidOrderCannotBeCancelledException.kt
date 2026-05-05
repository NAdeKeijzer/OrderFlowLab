package org.nikita.orderflowlab.order.exception

import java.util.UUID

class PaidOrderCannotBeCancelledException(orderId: UUID) :
    RuntimeException("Paid order $orderId cannot be cancelled")