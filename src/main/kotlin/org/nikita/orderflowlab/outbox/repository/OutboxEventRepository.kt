package org.nikita.orderflowlab.outbox.repository

import org.nikita.orderflowlab.outbox.model.OutboxEvent
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OutboxEventRepository : JpaRepository<OutboxEvent, UUID>