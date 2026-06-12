package org.nikita.orderflowlab.payment.event

import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class PaymentRequestedEvent(
    val orderId: UUID,
    val customerId: UUID,
    val amount: BigDecimal,
    val requestedAt: Instant
)