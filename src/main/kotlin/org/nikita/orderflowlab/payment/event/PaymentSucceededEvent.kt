package org.nikita.orderflowlab.payment.event

import java.time.Instant
import java.util.*

data class PaymentSucceededEvent(
    val orderId: UUID,
    val paidAt: Instant
)