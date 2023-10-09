package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.entity.Member
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Query
import jakarta.persistence.TypedQuery
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class MemberJpaRepository(
    @PersistenceContext
    private val em: EntityManager
) {

    fun save(member: Member): Member{
        em.persist(member)

        return member
    }

    fun delete(member: Member) {
        em.remove(member)
    }

    fun findAll(): MutableList<Member> {
        val query: TypedQuery<Member> = em.createQuery("select m from Member m", Member::class.java)
        val resultList: MutableList<Member> = query.resultList

        return resultList
    }

    fun findById(id: Long): Optional<Member> {
        val member: Member = em.find(Member::class.java, id)

        return Optional.ofNullable(member)
    }

    fun count(): Long {
        val query = em.createQuery("select count(m) from Member m", Long::class.java)
        val singleResult: Long = query.singleResult

        return singleResult
    }

    fun find(id: Long): Member{
        return em.find(Member::class.java, id)
    }

    fun findByUsernameAndAgeGreaterThan(username: String, age: Int): MutableList<Member> {
        val query = em.createQuery("select m from Member m where m.username = :username and m.age > :age", Member::class.java)
            .setParameter("username", username)
            .setParameter("age", age)

        val resultList: MutableList<Member> = query.resultList

        return resultList
    }

    fun findByUsername(username: String): MutableList<Member> {
        val query = em.createNamedQuery("Member.findByUsername", Member::class.java)
            .setParameter("username", username)

        val resultList: MutableList<Member> = query.resultList

        return resultList
    }

    fun findByPage(age: Int, offset: Int, limit: Int): MutableList<Member> {
        val query: TypedQuery<Member> = em.createQuery("select m from Member m where m.age = :age order by m.username desc", Member::class.java)
            .setParameter("age", age)
            .setFirstResult(offset)
            .setMaxResults(limit)

        val resultList = query.resultList

        return resultList
    }

    fun totalCount(age: Int): Long {
        val query: TypedQuery<Long> = em.createQuery("select count(m) from Member m where m.age = :age", Long::class.java)
            .setParameter("age", age)

        return query.singleResult
    }

    fun bulkAgePlus(age: Int): Int {
        val query: Query = em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
            .setParameter("age", age)

        val resultCount: Int = query.executeUpdate()

        return resultCount
    }

}