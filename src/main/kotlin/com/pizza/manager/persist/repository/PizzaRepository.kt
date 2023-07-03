package com.pizza.manager.persist.repository

import com.pizza.manager.persist.model.OrderPizza
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PizzaRepository: JpaRepository<OrderPizza, UUID>