package org.nikita.orderflowlab.payment.event

import java.time.Instant
import java.util.*

data class PaymentFailedEvent(
    val orderId: UUID,
    val failedAt: Instant,
    val reason: String
)