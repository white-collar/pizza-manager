package com.pizza.manager.persist.model

import com.pizza.manager.enum.Topping
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "order_topping")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
class OrderTopping(
    id: UUID? = null,

    var email: String,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var toppings: MutableList<Topping> = mutableListOf()

    ): AbstractEntity(id = id, createdAt = LocalDateTime.now())