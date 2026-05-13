package org.nikita.orderflowlab.order

import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "order_lines")
class OrderLine(

    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val productId: UUID,

    @Column(nullable = false)
    val unitPrice: BigDecimal,

    @Column(nullable = false)
    val quantity: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order

) {

    fun total(): BigDecimal =
        unitPrice.multiply(BigDecimal(quantity))
}