package org.nikita.orderflowlab.order

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "order_lines")
class OrderLine(

    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val productId: UUID,

    @Column(nullable = false)
    val quantity: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order
)