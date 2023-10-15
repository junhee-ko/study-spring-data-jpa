package com.example.studyspringdatajpa.entity

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
class Item: Persistable<Long> {

    // entityManager.persist(entity); 를 해야 Id 가 생김
    // 새로운 엔티티를 판단하는 기본 전략
    // 식별자가 객체일 때 null 로 판단 식별자가 자바 기본 타입일 때 0 으로 판단

    @Id
    @GeneratedValue
    val myId: Long? = null

    @CreatedDate
    var createdDate: LocalDateTime? = null

    override fun getId(): Long? {
        return myId
    }

    override fun isNew(): Boolean {
        return createdDate == null
    }
}