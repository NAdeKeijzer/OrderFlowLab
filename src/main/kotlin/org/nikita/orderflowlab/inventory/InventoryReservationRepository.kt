package org.nikita.orderflowlab.inventory

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface InventoryReservationRepository : JpaRepository<InventoryReservation, UUID>