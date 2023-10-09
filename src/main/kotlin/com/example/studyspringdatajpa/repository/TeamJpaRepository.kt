package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.entity.Team
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.TypedQuery
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class TeamJpaRepository(
    @PersistenceContext
    private val em: EntityManager
) {

    fun save(team: Team): Team {
        em.persist(team)

        return team
    }

    fun delete(team: Team){
        em.remove(team)
    }

    fun findAll(): MutableList<Team> {
        val query: TypedQuery<Team> = em.createQuery("select t from Team t", Team::class.java)
        val resultList: MutableList<Team> = query.resultList

        return resultList
    }

    fun findById(id: Long): Optional<Team> {
        val team: Team = em.find(Team::class.java, id)

        return Optional.ofNullable(team)
    }

    fun count(): Long {
        val query = em.createQuery("select count(t) from Team t", Long::class.java)
        val singleResult: Long = query.singleResult

        return singleResult
    }

    fun find(id: Long): Team {
        return em.find(Team::class.java, id)
    }
}