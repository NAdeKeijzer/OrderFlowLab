package org.nikita.orderflowlab.order

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.get
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @Test
    fun `creates an order`() {
        val customerId = UUID.randomUUID()

        mockMvc.post("/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "customerId": "$customerId"
                }
            """.trimIndent()
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { exists() }
                jsonPath("$.customerId") { value(customerId.toString()) }
                jsonPath("$.status") { value("CREATED") }
            }
    }

    @Test
    fun `gets all orders`() {
        mockMvc.get("/orders")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `returns 404 for unknown order`() {
        val unknownId = UUID.randomUUID()

        mockMvc.get("/orders/$unknownId")
            .andExpect {
                status { isNotFound() }
            }
    }
}