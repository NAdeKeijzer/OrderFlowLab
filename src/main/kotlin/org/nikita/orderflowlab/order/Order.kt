package org.nikita.orderflowlab.order

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "orders")
class Order(

    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val customerId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.CREATED,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now()
)