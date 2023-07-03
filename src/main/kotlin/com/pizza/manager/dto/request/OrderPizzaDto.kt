package com.pizza.manager.dto.request

import com.pizza.manager.enum.Pizza
import javax.validation.constraints.Email

data class OrderPizzaDto (
    @field:Email(message = "Invalid email")
    val email: String,
    val pizza: Pizza
)