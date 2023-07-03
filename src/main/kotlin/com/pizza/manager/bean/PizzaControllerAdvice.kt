package com.pizza.manager.bean

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import kotlin.random.Random

@ControllerAdvice
class PizzaControllerAdvice {

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorMessageModel> {

        val errorMessage = ErrorMessageModel(
            httpStatus = HttpStatus.BAD_REQUEST.value(),
            errorMessage = ex.bindingResult.fieldErrors[0].defaultMessage.toString(),
            errorCode = generateRandomString()
        )
        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun handleHttpReadableException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorMessageModel> {

        val errorMessage = ErrorMessageModel(
            httpStatus = HttpStatus.BAD_REQUEST.value(),
            errorMessage = if (ex.message.toString().contains("JSON parse error")) "Invalid pizza" else "Unknown error" ,
            errorCode = generateRandomString()
        )
        return ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST)
    }

    fun generateRandomString(): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val length = 8
        val random = Random(System.currentTimeMillis())
        val stringBuilder = StringBuilder()

        repeat(length) {
            val randomIndex = random.nextInt(characters.length)
            val randomChar = characters[randomIndex]
            stringBuilder.append(randomChar)
        }

        return stringBuilder.toString()
    }
}