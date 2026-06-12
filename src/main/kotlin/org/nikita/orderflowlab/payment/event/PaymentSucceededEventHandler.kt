package org.nikita.orderflowlab.payment.event

import org.nikita.orderflowlab.order.service.OrderService
import org.springframework.stereotype.Component

@Component
class PaymentSucceededEventHandler(
    private val orderService: OrderService
) {

    fun handle(event: PaymentSucceededEvent) {
        orderService.confirm(event.orderId)
    }
}