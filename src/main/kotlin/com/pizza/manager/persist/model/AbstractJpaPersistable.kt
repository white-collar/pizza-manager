package com.pizza.manager.persist.model

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import java.util.*
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractJpaPersistable(@Id
                             @GeneratedValue
                             var id: UUID? = null) : Serializable {
    companion object {
        private const val serialVersionUID = -5_154_304_939_380_869_154L
    }

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) {
            return true
        }

        id ?: return false

        if (ProxyUtils.getUserClass(other) != javaClass) {
            return false
        }

        return id == (other as? AbstractJpaPersistable)?.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: -43

    override fun toString(): String = "Entity $javaClass #$id"
}
