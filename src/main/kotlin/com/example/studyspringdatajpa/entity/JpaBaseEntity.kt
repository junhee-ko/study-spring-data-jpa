package com.example.studyspringdatajpa.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime

@MappedSuperclass
open class JpaBaseEntity(

    @Column(updatable = false)
    open var createdDate: LocalDateTime? = null,

    open var updatedDate: LocalDateTime? = null
) {

    @PrePersist
    fun prePersist() {
        println("-- prePersist")

        val now = LocalDateTime.now()
        createdDate = now
        updatedDate = now
    }

    @PreUpdate
    fun preUpdate() {
        println("-- preUpdate")

        val now = LocalDateTime.now()
        updatedDate = now
    }
}