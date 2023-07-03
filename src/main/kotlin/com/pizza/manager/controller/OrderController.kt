package com.pizza.manager.controller

import com.pizza.manager.dto.request.OrderPizzaDto
import com.pizza.manager.dto.request.OrderToppingDto
import com.pizza.manager.dto.response.CustomerCountDto
import com.pizza.manager.enum.Topping
import com.pizza.manager.service.OrderService
import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/orders")
@Validated
class OrderController(@Autowired private val orderService: OrderService) {

    companion object : KLogging()

    @PostMapping("/pizza")
    @ResponseStatus(HttpStatus.CREATED)
    fun createPizzaOrder(@Valid @RequestBody orderPizzaDto: OrderPizzaDto) {
        logger.info { "Submitting pizza's order request ..." }
        orderService.makePizzaOrder(orderPizzaDto);
    }

    @PostMapping("/topping")
    @ResponseStatus(HttpStatus.CREATED)
    fun createToppingOrder(@Valid @RequestBody orderToppingDto: OrderToppingDto) {
        logger.info { "Submitting pizza's order request ..." }
        orderService.makeToppingOrder(orderToppingDto);
    }

    @GetMapping("/toppings")
    @ResponseStatus(HttpStatus.OK)
    fun getSubmittedToppings(): ResponseEntity<List<Topping>> {
        logger.info { "Requesting list of toppings ..." }
        return ResponseEntity.ok(orderService.getSubmittedToppings());
    }

    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    fun getCustomerCountByTopping(@RequestParam(value = "topping", required = true) topping: Topping): ResponseEntity<CustomerCountDto> {
        logger.info { "Requesting count of customers who requested $topping ..." }
        return ResponseEntity.ok(CustomerCountDto(
            count = orderService.findCustomerCountByTopping(topping = topping)));
    }
}