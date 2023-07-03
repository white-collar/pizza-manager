package com.pizza.manager.persist.repository

import com.pizza.manager.enum.Topping
import com.pizza.manager.persist.model.OrderTopping
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface ToppingRepository: JpaRepository<OrderTopping, UUID> {

    @Query("SELECT distinct json_array_elements_text(toppings) FROM ORDER_TOPPING", nativeQuery = true)
    fun findToppings(): List<Topping>

    @Query("SELECT count(email) FROM order_topping WHERE toppings\\:\\:jsonb @> jsonb_build_array(:topping)", nativeQuery = true)
    fun findCustomerCountByTopping(@Param("topping") topping: String): Int

    fun findByEmail(email: String) : OrderTopping?;
}