package com.example.studyspringdatajpa.entity

import com.example.studyspringdatajpa.repository.MemberRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    private lateinit var em: EntityManager

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Test
    fun testEntity() {
        val teamA = Team(name = "teamA")
        val teamB = Team(name = "teamB")
        em.persist(teamA)
        em.persist(teamB)

        val member1 = Member(username = "member1", age = 10, team = teamA)
        val member2 = Member(username = "member2", age = 20, team = teamA)
        val member3 = Member(username = "member3", age = 30, team = teamB)
        val member4 = Member(username = "member4", age = 40, team = teamB)

        em.persist(member1)
        em.persist(member2)
        em.persist(member3)
        em.persist(member4)

        em.flush()
        em.clear()

        val members: MutableList<Member> = em.createQuery("select m from Member m", Member::class.java)
            .resultList

        members.forEach {member ->
            println("member: ${member}, team: ${member.team}")
        }
    }


    @Test
    fun jpaEventBaseEntity() {
        // given
        val member = Member(username = "member1", age = 10, team = null)
        memberRepository.save(member) // @PrePersist

        member.username = "member2"
        em.flush()
        em.clear()

        // when
        val findMember = memberRepository.findById(member.id).get()


        // then
        println("findMember.createdDate " + findMember.createdDate)
        println("findMember.updatedDate " + findMember.lastModifiedDate)
        println("findMember.createdBy " + findMember.createdBy)
        println("findMember.lastModifiedBy " + findMember.lastModifiedBy)
    }
}