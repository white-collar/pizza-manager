package com.pizza.manager.service.impl

import com.pizza.manager.dto.request.OrderPizzaDto
import com.pizza.manager.dto.request.OrderToppingDto
import com.pizza.manager.enum.Topping
import com.pizza.manager.persist.model.OrderPizza
import com.pizza.manager.persist.model.OrderTopping
import com.pizza.manager.persist.repository.PizzaRepository
import com.pizza.manager.persist.repository.ToppingRepository
import com.pizza.manager.service.OrderService
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class OrderServiceImpl(
    @Autowired private val orderRepository: PizzaRepository,
    @Autowired private val toppingRepository: ToppingRepository,
): OrderService {

    companion object : KLogging()

    override fun makePizzaOrder(orderPizzaDto: OrderPizzaDto) {
        logger.info { "Pizza order accepted: creating new ..." }
        val orderPizza = OrderPizza(
            email = orderPizzaDto.email,
            pizza = orderPizzaDto.pizza
        );
        orderRepository.save(orderPizza)
    }

    @Transactional
    override fun makeToppingOrder(orderToppingDto: OrderToppingDto) {
        logger.info { "Assigning topping for user $orderToppingDto "}
        val orderTopping = OrderTopping(
            email = orderToppingDto.email,
            toppings = orderToppingDto.toppings
        )
        val existedOrder = toppingRepository.findByEmail(email = orderToppingDto.email);
        if (existedOrder == null) {
            toppingRepository.save(orderTopping);
        } else {
            existedOrder.toppings = orderTopping.toppings;
            existedOrder.createdAt = LocalDateTime.now();
            toppingRepository.save(existedOrder);
        }

    }

    override fun getSubmittedToppings(): List<Topping> {
        return toppingRepository.findToppings();
    }

    override fun findCustomerCountByTopping(topping: Topping): Int {
        return toppingRepository.findCustomerCountByTopping(topping.name)
    }
}