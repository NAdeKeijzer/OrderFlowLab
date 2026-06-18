package org.nikita.orderflowlab

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class OrderFlowLabApplication

fun main(args: Array<String>) {
    runApplication<OrderFlowLabApplication>(*args)
}
