package org.nikita.orderflowlab.order.repository

import org.nikita.orderflowlab.order.model.Order
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderRepository : JpaRepository<Order, UUID> {

    @EntityGraph(attributePaths = ["lines"])
    override fun findAll(): MutableList<Order>

    @EntityGraph(attributePaths = ["lines"])
    override fun findById(id: UUID): Optional<Order>
}