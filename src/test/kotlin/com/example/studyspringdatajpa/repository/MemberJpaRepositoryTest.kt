package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.entity.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest{

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @Test
    fun testMember() {
        val member = Member(username = "jko", age = 30, team = null)
        val savedMember = memberJpaRepository.save(member)

        val findMember = memberJpaRepository.find(savedMember.id)

        assertThat(findMember.id).isEqualTo(member.id)
        assertThat(findMember.username).isEqualTo(member.username)
        assertThat(findMember).isEqualTo(member)
    }

    @Test
    fun `basic CRUD`() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member2", age = 30, team = null)
        memberJpaRepository.save(member1)
        memberJpaRepository.save(member2)

        val findMember1 = memberJpaRepository.findById(member1.id).get()
        val findMember2 = memberJpaRepository.findById(member2.id).get()
        assertThat(findMember1).isEqualTo(member1)
        assertThat(findMember2).isEqualTo(member2)

        val all = memberJpaRepository.findAll()
        assertThat(all.size).isEqualTo(2)

        val count = memberJpaRepository.count()
        assertThat(count).isEqualTo(2)

        memberJpaRepository.delete(member1)
        memberJpaRepository.delete(member2)
        val deletedCount = memberJpaRepository.count()
        assertThat(deletedCount).isEqualTo(0)
    }

    @Test
    fun findByUsernameAndAgeGreaterThan() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member1", age = 30, team = null)
        memberJpaRepository.save(member1)
        memberJpaRepository.save(member2)

        val result = memberJpaRepository.findByUsernameAndAgeGreaterThan(username = "member1", age = 25)

        assertThat(result.size).isEqualTo(1)
        assertThat(result.get(0).username).isEqualTo("member1")
        assertThat(result.get(0).age).isEqualTo(30)
    }

    @Test
    fun testNamedQuery() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member2", age = 30, team = null)
        memberJpaRepository.save(member1)
        memberJpaRepository.save(member2)

        val result: MutableList<Member> = memberJpaRepository.findByUsername("member1")

        assertThat(result.size).isEqualTo(1)
        assertThat(result.get(0)).isEqualTo(member1)
    }

    @Test
    fun paging() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member2", age = 20, team = null)
        val member3 = Member(username = "member3", age = 20, team = null)
        val member4 = Member(username = "member4", age = 20, team = null)
        val member5 = Member(username = "member5", age = 20, team = null)
        memberJpaRepository.save(member1)
        memberJpaRepository.save(member2)
        memberJpaRepository.save(member3)
        memberJpaRepository.save(member4)
        memberJpaRepository.save(member5)

        val totalCount = memberJpaRepository.totalCount(20)
        val members = memberJpaRepository.findByPage(
            age = 20,
            offset = 0,
            limit = 3
        )

        assertThat(totalCount).isEqualTo(5)
        assertThat(members.size).isEqualTo(3)
    }
}