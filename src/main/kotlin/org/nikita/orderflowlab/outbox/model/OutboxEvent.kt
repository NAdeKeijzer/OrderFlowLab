package org.nikita.orderflowlab.outbox.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Entity
@Table(name = "outbox_events")
class OutboxEvent(

    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val eventType: String,

    @Column(nullable = false)
    val aggregateId: UUID,

    @Column(nullable = false, columnDefinition = "TEXT")
    val payload: String,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column
    var publishedAt: Instant? = null
) {

    fun markPublished() {
        publishedAt = Instant.now()
    }
}