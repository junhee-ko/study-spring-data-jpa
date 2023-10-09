package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.entity.Member
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository

@Repository
class MemberJpaRepository(
    @PersistenceContext
    private val em: EntityManager
) {

    fun save(member: Member): Member{
        em.persist(member)

        return member
    }

    fun find(id: Long): Member{
        return em.find(Member::class.java, id)
    }
}