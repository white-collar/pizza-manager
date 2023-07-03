package com.pizza.manager.persist.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Basic
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractEntity(
    id: UUID?,

    @Basic(optional = false)
    @CreatedDate
    var createdAt: LocalDateTime,

) : AbstractJpaPersistable(id = id)