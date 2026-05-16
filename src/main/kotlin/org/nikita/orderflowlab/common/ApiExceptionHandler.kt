package org.nikita.orderflowlab.common

import org.nikita.orderflowlab.order.exception.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ApiErrorResponse(
    val status: Int,
    val error: String,
    val message: String?
)

data class ValidationErrorResponse(
    val status: Int,
    val error: String,
    val fields: Map<String, String?>
)



@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        exception: MethodArgumentNotValidException
    ): ResponseEntity<ValidationErrorResponse> {
        val fieldErrors = exception.bindingResult.fieldErrors.associate {
            it.field to it.defaultMessage
        }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ValidationErrorResponse(
                    status = 400,
                    error = "Validation failed",
                    fields = fieldErrors
                )
            )
    }

    @ExceptionHandler(OrderNotFoundException::class)
    fun handleOrderNotFound(
        ex: OrderNotFoundException
    ): ResponseEntity<Unit> =
        ResponseEntity.notFound().build()

    @ExceptionHandler(OrderAlreadyPaidException::class)
    fun handleOrderAlreadyPaid(
        exception: OrderAlreadyPaidException
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                ApiErrorResponse(
                    status = 409,
                    error = "Order already paid",
                    message = exception.message
                )
            )
    }

    @ExceptionHandler(PaidOrderCannotBeCancelledException::class)
    fun handlePaidOrderCannotBeCancelled(
        exception: PaidOrderCannotBeCancelledException
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                ApiErrorResponse(
                    status = 409,
                    error = "Paid order cannot be cancelled",
                    message = exception.message
                )
            )
    }

    @ExceptionHandler(InvalidOrderLineQuantityException::class)
    fun handleInvalidQuantity(
        ex: InvalidOrderLineQuantityException
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ApiErrorResponse(
                    status = 400,
                    error = "Invalid order line quantity",
                    message = ex.message
                )
            )
    }

    @ExceptionHandler(EmptyOrderException::class)
    fun handleEmptyOrder(
        ex: EmptyOrderException
    ): ResponseEntity<ApiErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ApiErrorResponse(
                    status = 400,
                    error = "Empty order",
                    message = ex.message
                )
            )
    }
}
