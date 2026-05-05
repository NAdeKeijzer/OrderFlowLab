package org.nikita.orderflowlab.order

import java.util.UUID

class PaidOrderCannotBeCancelledException(orderId: UUID) :
    RuntimeException("Paid order $orderId cannot be cancelled")