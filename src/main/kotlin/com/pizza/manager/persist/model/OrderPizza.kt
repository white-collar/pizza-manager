package com.pizza.manager.persist.model

import com.pizza.manager.enum.Pizza
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table


@Entity
@Table(name = "order_pizza")
class OrderPizza(
    id: UUID? = null,

    var email: String,

    @Enumerated(EnumType.STRING)
    var pizza: Pizza,

    ): AbstractEntity(id = id, createdAt = LocalDateTime.now());