package org.nikita.orderflowlab.order.model

import jakarta.persistence.*
import org.nikita.orderflowlab.order.exception.EmptyOrderException
import org.nikita.orderflowlab.order.exception.InvalidOrderLineQuantityException
import org.nikita.orderflowlab.order.exception.OrderAlreadyPaidException
import org.nikita.orderflowlab.order.exception.PaidOrderCannotBeCancelledException
import java.math.BigDecimal
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

    fun addLine(productId: UUID, quantity: Int, unitPrice: BigDecimal) {
        if (quantity <= 0) {
            throw InvalidOrderLineQuantityException(quantity)
        }

        lines.add(
            OrderLine(
                productId = productId,
                quantity = quantity,
                unitPrice = unitPrice,
                order = this
            )
        )
    }

    fun markAsPaid() {
        if (status == OrderStatus.PAID) {
            throw OrderAlreadyPaidException(id)
        }

        status = OrderStatus.PAID
    }

    fun cancel() {
        if (status == OrderStatus.PAID) {
            throw PaidOrderCannotBeCancelledException(id)
        }

        status = OrderStatus.CANCELLED
    }

    companion object {
        fun create(
            customerId: UUID,
            items: List<OrderLineInput>
        ): Order {
            if (items.isEmpty()) {
                throw EmptyOrderException()
            }

            val order = Order(customerId = customerId)

            items.forEach {
                order.addLine(
                    productId = it.productId,
                    quantity = it.quantity,
                    unitPrice = it.unitPrice
                )
            }

            return order
        }
    }

    fun total(): BigDecimal {
        return lines.fold(BigDecimal.ZERO) { acc, line ->
            acc.add(line.total())
        }
    }
}