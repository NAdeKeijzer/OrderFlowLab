package org.nikita.orderflowlab.order.service

import org.nikita.orderflowlab.inventory.exception.InventoryDomainException
import org.nikita.orderflowlab.inventory.service.InventoryReservationService
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OrderWorkflowService(
    private val inventoryReservationService: InventoryReservationService,
    private val orderService: OrderService
) {

    private val logger = LoggerFactory.getLogger(OrderWorkflowService::class.java)

    fun handleOrderCreated(event: OrderCreatedEvent) {
        try {
            inventoryReservationService.reserveFor(event)

            orderService.markInventoryReserved(event.orderId)

            logger.info(
                "Inventory reservation succeeded for orderId={}",
                event.orderId
            )
        } catch (ex: InventoryDomainException) {
            logger.error(
                "Inventory reservation failed for orderId={}",
                event.orderId,
                ex
            )

            orderService.markInventoryFailed(event.orderId)
        }
    }
}