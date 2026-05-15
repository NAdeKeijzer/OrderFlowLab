package org.nikita.orderflowlab.inventory.repository

import org.nikita.orderflowlab.inventory.model.InventoryReservation
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface InventoryReservationRepository : JpaRepository<InventoryReservation, UUID>