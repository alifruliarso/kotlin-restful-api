package com.galapea.belajar.kotlinrestfulapi.controller

import com.galapea.belajar.kotlinrestfulapi.error.ProductNotFoundException
import com.galapea.belajar.kotlinrestfulapi.model.WebResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class ErrorController {
    companion object {
        private val log = LoggerFactory.getLogger(ErrorController::class.java)
    }

    data class SimpleError(val path: String? = null, val code: String? = null, val message: String? = null)
    data class Errors(val code: String, val message: String, val errors: List<SimpleError>? = emptyList())

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun handleConstraintViolation(constraintValidationException: ConstraintViolationException): ResponseEntity<Any> {
        log.debug("catching exception: ${constraintValidationException.message}")
        val fieldErrs = constraintValidationException.constraintViolations
            .map {
                SimpleError(
                    path = it.propertyPath.joinToString("."),
                    code = it.constraintDescriptor.annotation.annotationClass.simpleName,
                    message = it.message
                )
            }
            .toList()
        val errors = Errors(
            code = "validation_failure",
            message = "Validation failed.",
            errors = fieldErrs
        )
        val webResponse = WebResponse(
            code = HttpStatus.BAD_REQUEST.value(),
            status = "Bad Request",
            data = errors
        )
        return ResponseEntity(webResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [ProductNotFoundException::class])
    fun notFound(notFoundException: ProductNotFoundException): ResponseEntity<Any> {
        val webResponse = WebResponse(
            code = 404,
            status = "Not Found",
            data = null
        )
        return ResponseEntity(webResponse, HttpStatus.NOT_FOUND)
    }
}