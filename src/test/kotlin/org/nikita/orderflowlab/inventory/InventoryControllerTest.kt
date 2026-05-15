package org.nikita.orderflowlab.inventory

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InventoryControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `creates inventory item`() {
        val productId = UUID.randomUUID()

        mockMvc.post("/inventory-items") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "productId": "$productId",
                  "availableQuantity": 10
                }
            """.trimIndent()
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.productId") { value(productId.toString()) }
                jsonPath("$.availableQuantity") { value(10) }
                jsonPath("$.updatedAt") { exists() }
            }
    }

    @Test
    fun `gets inventory item by product id`() {
        val productId = UUID.randomUUID()

        mockMvc.post("/inventory-items") {
            contentType = MediaType.APPLICATION_JSON
            content = """
            {
              "productId": "$productId",
              "availableQuantity": 10
            }
        """.trimIndent()
        }
            .andExpect {
                status { isOk() }
            }

        mockMvc.get("/inventory-items/$productId")
            .andExpect {
                status { isOk() }
                jsonPath("$.productId") { value(productId.toString()) }
                jsonPath("$.availableQuantity") { value(10) }
                jsonPath("$.updatedAt") { exists() }
            }
    }

    @Test
    fun `returns 404 for unknown order`() {
        val unknownId = UUID.randomUUID()

        mockMvc.get("/inventory-items/$unknownId")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun `updates inventory item`() {
        val productId = UUID.randomUUID()

        mockMvc.post("/inventory-items") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "productId": "$productId",
                  "availableQuantity": 10
                }
            """.trimIndent()
        }
        mockMvc.post("/inventory-items") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                  "productId": "$productId",
                  "availableQuantity": 9
                }
            """.trimIndent()
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.productId") { value(productId.toString()) }
                jsonPath("$.availableQuantity") { value(9) }
                jsonPath("$.updatedAt") { exists() }
            }
    }

    @Test
    fun `rejects negative quantity`() {
        val productId = UUID.randomUUID()

        mockMvc.post("/inventory-items") {
            contentType = MediaType.APPLICATION_JSON
            content = """
            {
              "productId": "$productId",
              "availableQuantity": -1
            }
        """.trimIndent()
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("$.status") { value(400) }
                jsonPath("$.error") { value("Validation failed") }
                jsonPath("$.fields.availableQuantity") { exists() }
            }
    }
}