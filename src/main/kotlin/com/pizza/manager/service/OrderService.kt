package com.pizza.manager.service

import com.pizza.manager.dto.request.OrderPizzaDto
import com.pizza.manager.dto.request.OrderToppingDto
import com.pizza.manager.enum.Topping

interface OrderService {

    fun makePizzaOrder(orderPizzaDto: OrderPizzaDto);

    fun makeToppingOrder(orderToppingDto: OrderToppingDto);

    fun getSubmittedToppings(): List<Topping>

    fun findCustomerCountByTopping(topping: Topping): Int

}