package com.pizza.manager.dto.request

import com.pizza.manager.enum.Topping
import javax.validation.constraints.Email
import javax.validation.constraints.Size

data class OrderToppingDto(
    @field:Email(message = "Invalid email")
    val email: String,

    @field:Size(min = 1, max = 5, message = "Max count of topping achieved")
    val toppings: MutableList<Topping>
)