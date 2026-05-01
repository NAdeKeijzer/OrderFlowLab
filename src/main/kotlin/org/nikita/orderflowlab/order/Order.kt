package org.nikita.orderflowlab.order

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

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
    val createdAt: Instant = Instant.now(),

    @OneToMany(
        mappedBy = "order",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val lines: MutableList<OrderLine> = mutableListOf()

) {

    fun addLine(productId: UUID, quantity: Int) {
        lines.add(
            OrderLine(
                productId = productId,
                quantity = quantity,
                order = this
            )
        )
    }
}