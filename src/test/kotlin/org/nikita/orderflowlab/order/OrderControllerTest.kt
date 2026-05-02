package org.nikita.orderflowlab.order

import org.junit.jupiter.api.Test
import com.jayway.jsonpath.JsonPath
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {

    @Test
    fun `creates an order with items`() {
        val customerId = UUID.randomUUID()
        val productId = UUID.randomUUID()

        mockMvc.post("/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "customerId": "$customerId",
                  "items": [
                    {
                      "productId": "$productId",
                      "quantity": 2
                    }
                  ]
                }
            """.trimIndent()
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { exists() }
                jsonPath("$.customerId") { value(customerId.toString()) }
                jsonPath("$.status") { value("CREATED") }
                jsonPath("$.lines") { exists() }
                jsonPath("$.lines[0].productId") { value(productId.toString()) }
                jsonPath("$.lines[0].quantity") { value(2) }
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

    @Test
    fun `rejects order without customer id`() {
        val productId = UUID.randomUUID()

        mockMvc.post("/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "customerId": null,
                  "items": [
                    {
                      "productId": "$productId",
                      "quantity": 2
                    }
                  ]
                }
            """.trimIndent()
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("$.status") { value(400) }
                jsonPath("$.error") { value("Validation failed") }
                jsonPath("$.fields.customerId") { exists() }
            }
    }

    @Test
    fun `rejects order without items`() {
        val customerId = UUID.randomUUID()

        mockMvc.post("/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "customerId": "$customerId",
                  "items": []
                }
            """.trimIndent()
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("$.fields.items") { exists() }
            }
    }

    @Test
    fun `rejects order line with quantity zero`() {
        val customerId = UUID.randomUUID()
        val productId = UUID.randomUUID()

        mockMvc.post("/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "customerId": "$customerId",
                  "items": [
                    {
                      "productId": "$productId",
                      "quantity": 0
                    }
                  ]
                }
            """.trimIndent()
        }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    fun `can pay order via endpoint`() {
        val customerId = UUID.randomUUID()
        val productId = UUID.randomUUID()

        val createResponse = mockMvc.post("/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = """
            {
              "customerId": "$customerId",
              "items": [
                {
                  "productId": "$productId",
                  "quantity": 2
                }
              ]
            }
        """.trimIndent()
        }.andReturn()

        val id = JsonPath.read<String>(
            createResponse.response.contentAsString,
            "$.id"
        )

        mockMvc.patch("/orders/$id/pay")
            .andExpect {
                status { isOk() }
                jsonPath("$.status") { value("PAID") }
            }
    }
}