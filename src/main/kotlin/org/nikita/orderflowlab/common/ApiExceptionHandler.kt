package org.nikita.orderflowlab.common

import org.nikita.orderflowlab.order.exception.InvalidOrderLineQuantityException
import org.nikita.orderflowlab.order.exception.OrderAlreadyPaidException
import org.nikita.orderflowlab.order.exception.PaidOrderCannotBeCancelledException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ErrorResponse(
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

    @ExceptionHandler(OrderAlreadyPaidException::class)
    fun handleOrderAlreadyPaid(
        exception: OrderAlreadyPaidException
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    status = 409,
                    error = "Order already paid",
                    message = exception.message
                )
            )
    }

    @ExceptionHandler(PaidOrderCannotBeCancelledException::class)
    fun handlePaidOrderCannotBeCancelled(
        exception: PaidOrderCannotBeCancelledException
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                ErrorResponse(
                    status = 409,
                    error = "Paid order cannot be cancelled",
                    message = exception.message
                )
            )
    }

    @ExceptionHandler(InvalidOrderLineQuantityException::class)
    fun handleInvalidQuantity(
        ex: InvalidOrderLineQuantityException
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    status = 400,
                    error = "Invalid order line quantity",
                    message = ex.message
                )
            )
    }
}
