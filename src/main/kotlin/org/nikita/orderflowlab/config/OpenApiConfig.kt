package org.nikita.orderflowlab.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun orderFlowLabOpenAPI(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title("OrderFlowLab API")
                    .version("v1")
                    .description("API documentation for the OrderFlowLab order, inventory, and payment workflow.")
            )
}