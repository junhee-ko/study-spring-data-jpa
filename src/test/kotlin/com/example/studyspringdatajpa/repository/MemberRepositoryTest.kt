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
class MemberRepositoryTest{

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Test
    fun `repository class`() {
        println(memberRepository)             // org.springframework.data.jpa.repository.support.SimpleJpaRepository@6a4238ff
        println(memberRepository::class.java) // class jdk.proxy2.$Proxy118
    }

    @Test
    fun testMember() {
        val member = Member(username = "jko", age = 30, team = null)
        val savedMember = memberRepository.save(member)

        val findMember = memberRepository.findById(savedMember.id).get()

        assertThat(findMember.id).isEqualTo(member.id)
        assertThat(findMember.username).isEqualTo(member.username)
        assertThat(findMember).isEqualTo(member)
    }

    @Test
    fun `basic CRUD`() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member2", age = 30, team = null)
        memberRepository.save(member1)
        memberRepository.save(member2)

        val findMember1 = memberRepository.findById(member1.id).get()
        val findMember2 = memberRepository.findById(member2.id).get()
        assertThat(findMember1).isEqualTo(member1)
        assertThat(findMember2).isEqualTo(member2)

        val all = memberRepository.findAll()
        assertThat(all.size).isEqualTo(2)

        val count = memberRepository.count()
        assertThat(count).isEqualTo(2)

        memberRepository.delete(member1)
        memberRepository.delete(member2)
        val deletedCount = memberRepository.count()
        assertThat(deletedCount).isEqualTo(0)
    }

    @Test
    fun findByUsernameAndAgeGreaterThan() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member1", age = 30, team = null)
        memberRepository.save(member1)
        memberRepository.save(member2)

        val result = memberRepository.findByUsernameAndAgeGreaterThan(username = "member1", age = 25)

        assertThat(result.size).isEqualTo(1)
        assertThat(result.get(0).username).isEqualTo("member1")
        assertThat(result.get(0).age).isEqualTo(30)
    }

    @Test
    fun testNamedQuery() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member2", age = 30, team = null)
        memberRepository.save(member1)
        memberRepository.save(member2)

        val result: List<Member> = memberRepository.findByUsername("member1")

        assertThat(result.size).isEqualTo(1)
        assertThat(result.get(0)).isEqualTo(member1)
    }

    @Test
    fun findMember() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member2", age = 30, team = null)
        memberRepository.save(member1)
        memberRepository.save(member2)

        val result: List<Member> = memberRepository.findMember(username = "member1", age = 20)

        assertThat(result.size).isEqualTo(1)
        assertThat(result.get(0)).isEqualTo(member1)
    }

    @Test
    fun findUsernameList() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member2", age = 30, team = null)
        memberRepository.save(member1)
        memberRepository.save(member2)

        val result: List<String> = memberRepository.findUsernameList()

        result.forEach{
            println(it)
        }
    }

    @Test
    fun findMemberDto() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member2", age = 30, team = null)
        memberRepository.save(member1)
        memberRepository.save(member2)

        val result = memberRepository.findMemberDto()

        result.forEach {
            println(it)
        }
    }

    @Test
    fun findByNames() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member2", age = 30, team = null)
        memberRepository.save(member1)
        memberRepository.save(member2)

        val result: List<Member> = memberRepository.findByNames(listOf("member1", "member2"))

        assertThat(result.size).isEqualTo(2)
    }
}