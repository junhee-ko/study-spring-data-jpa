package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.entity.Member
import com.example.studyspringdatajpa.entity.Team
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest{

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var teamJpaRepository: TeamJpaRepository

    @Autowired
    private lateinit var em: EntityManager

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

    @Test
    fun paging() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member2", age = 20, team = null)
        val member3 = Member(username = "member3", age = 20, team = null)
        val member4 = Member(username = "member4", age = 20, team = null)
        val member5 = Member(username = "member5", age = 20, team = null)
        memberRepository.save(member1)
        memberRepository.save(member2)
        memberRepository.save(member3)
        memberRepository.save(member4)
        memberRepository.save(member5)

        val pageRequest: PageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"))

        val page: Page<Member> = memberRepository.findByAge(
            age = 20,
            pageable = pageRequest,
        )

        assertThat(page.content.size).isEqualTo(3)
        assertThat(page.totalElements).isEqualTo(5)
        assertThat(page.number).isEqualTo(0)
        assertThat(page.isFirst).isTrue()
        assertThat(page.hasNext()).isTrue()
    }

    @Test
    fun pagingWithSlice() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member2", age = 20, team = null)
        val member3 = Member(username = "member3", age = 20, team = null)
        val member4 = Member(username = "member4", age = 20, team = null)
        val member5 = Member(username = "member5", age = 20, team = null)
        memberRepository.save(member1)
        memberRepository.save(member2)
        memberRepository.save(member3)
        memberRepository.save(member4)
        memberRepository.save(member5)

        val pageRequest: PageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"))

        val page: Slice<Member> = memberRepository.findSliceByAge(
            age = 20,
            pageable = pageRequest,
        )

        assertThat(page.content.size).isEqualTo(3)
//        assertThat(page.totalElements).isEqualTo(5)
        assertThat(page.number).isEqualTo(0)
        assertThat(page.isFirst).isTrue()
        assertThat(page.hasNext()).isTrue()
    }

    @Test
    fun pagingWithList() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member2", age = 20, team = null)
        val member3 = Member(username = "member3", age = 20, team = null)
        val member4 = Member(username = "member4", age = 20, team = null)
        val member5 = Member(username = "member5", age = 20, team = null)
        memberRepository.save(member1)
        memberRepository.save(member2)
        memberRepository.save(member3)
        memberRepository.save(member4)
        memberRepository.save(member5)

        val pageRequest: PageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"))

        val page: List<Member> = memberRepository.findListByAge(
            age = 20,
            pageable = pageRequest,
        )
    }

    @Test
    fun pagingWithCountQuery() {
        val member1 = Member(username = "member1", age = 20, team = null)
        val member2 = Member(username = "member2", age = 20, team = null)
        val member3 = Member(username = "member3", age = 20, team = null)
        val member4 = Member(username = "member4", age = 20, team = null)
        val member5 = Member(username = "member5", age = 20, team = null)
        memberRepository.save(member1)
        memberRepository.save(member2)
        memberRepository.save(member3)
        memberRepository.save(member4)
        memberRepository.save(member5)

        val pageRequest: PageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"))

        val page: Page<Member> = memberRepository.findWithCountByAge(
            age = 20,
            pageable = pageRequest,
        )
    }

    @Test
    fun bulkUpdate(){
        val member1 = Member(username = "member1", age = 18, team = null)
        val member2 = Member(username = "member2", age = 19, team = null)
        val member3 = Member(username = "member3", age = 20, team = null)
        val member4 = Member(username = "member4", age = 21, team = null)
        val member5 = Member(username = "member5", age = 22, team = null)
        memberRepository.save(member1)
        memberRepository.save(member2)
        memberRepository.save(member3)
        memberRepository.save(member4)
        memberRepository.save(member5)

        val resultCount = memberRepository.bulkAgePlus(20)

        assertThat(resultCount).isEqualTo(3)
    }

    @Test
    fun bulkUpdateWithEntityMangerClear(){
        val member1 = Member(username = "member1", age = 18, team = null)
        val member2 = Member(username = "member2", age = 19, team = null)
        val member3 = Member(username = "member3", age = 20, team = null)
        val member4 = Member(username = "member4", age = 21, team = null)
        val member5 = Member(username = "member5", age = 22, team = null)
        memberRepository.save(member1)
        memberRepository.save(member2)
        memberRepository.save(member3)
        memberRepository.save(member4)
        memberRepository.save(member5)

        val resultCount = memberRepository.bulkAgePlus(20)
        // em.clear() // 를 하거나, @Modifying(clearAutomatically = true)

        val result: List<Member> = memberRepository.findByUsername("member5")
        println("findByUsername: $result")

        assertThat(resultCount).isEqualTo(3)
    }

    @Test
    fun findMemberLazy() {
        // given
        val teamA = Team(name = "teamA")
        val teamB = Team(name = "teamB")
        teamJpaRepository.save(teamA)
        teamJpaRepository.save(teamB)

        val member1 = Member(username = "member1", age = 18, team = teamA)
        val member2 = Member(username = "member2", age = 19, team = teamB)
        memberRepository.save(member1)
        memberRepository.save(member2)

        em.flush()
        em.clear()

        // when
        val members: List<Member> = memberRepository.findAll()

        // then
        members.forEach {
            println("member: ${it}")
            println("team: ${it.team!!::class.java}")
            println("team.name: ${it.team!!.name}")
        }
    }

    @Test
    fun findMemberFetchJoin() {
        // given
        val teamA = Team(name = "teamA")
        val teamB = Team(name = "teamB")
        teamJpaRepository.save(teamA)
        teamJpaRepository.save(teamB)

        val member1 = Member(username = "member1", age = 18, team = teamA)
        val member2 = Member(username = "member2", age = 19, team = teamB)
        memberRepository.save(member1)
        memberRepository.save(member2)

        em.flush()
        em.clear()

        // when
        val members: List<Member> = memberRepository.findMemberFetchJoin()

        // then
        members.forEach {
            println("member: ${it}")
            println("team: ${it.team!!::class.java}")
            println("team.name: ${it.team!!.name}")
        }
    }

    @Test
    fun findEntityGraphByUsername() {
        // given
        val teamA = Team(name = "teamA")
        val teamB = Team(name = "teamB")
        teamJpaRepository.save(teamA)
        teamJpaRepository.save(teamB)

        val member1 = Member(username = "member1", age = 18, team = teamA)
        val member2 = Member(username = "member2", age = 19, team = teamB)
        memberRepository.save(member1)
        memberRepository.save(member2)

        em.flush()
        em.clear()

        // when
        val members: List<Member> = memberRepository.findEntityGraphByUsername("member1")

        // then
        members.forEach {
            println("member: ${it}")
            println("team: ${it.team!!::class.java}")
            println("team.name: ${it.team!!.name}")
        }
    }

    @Test
    fun findNamedEntityGraphByUsername() {
        // given
        val teamA = Team(name = "teamA")
        val teamB = Team(name = "teamB")
        teamJpaRepository.save(teamA)
        teamJpaRepository.save(teamB)

        val member1 = Member(username = "member1", age = 18, team = teamA)
        val member2 = Member(username = "member2", age = 19, team = teamB)
        memberRepository.save(member1)
        memberRepository.save(member2)

        em.flush()
        em.clear()

        // when
        val members: List<Member> = memberRepository.findNamedEntityGraphByUsername("member1")

        // then
        members.forEach {
            println("member: ${it}")
            println("team: ${it.team!!::class.java}")
            println("team.name: ${it.team!!.name}")
        }
    }

    @Test
    fun queryHint() {
        // given
        val member = Member(username = "member1", age = 10, team = null)
        memberRepository.save(member)
        em.flush()
        em.clear()

        // when
        val findMember = memberRepository.findById(member.id).get()
        findMember.username = "member2" // 변경 감지

        // then
        em.flush()
    }

    @Test
    fun findReadOnlyByUsername() {
        // given
        val member = Member(username = "member1", age = 10, team = null)
        memberRepository.save(member)
        em.flush()
        em.clear()

        // when
        val findMember = memberRepository.findReadOnlyByUsername("member1") // snapshot 만들지 않아, 변경감지 동작 X
        findMember.username = "member2"

        // then
        em.flush()
    }

    @Test
    fun lock() {
        // given
        val member = Member(username = "member1", age = 10, team = null)
        memberRepository.save(member)
        em.flush()
        em.clear()

        // when
        val findMember = memberRepository.findLockByUsername("member1")

        // then
        em.flush()
    }

    @Test
    fun customRepository() {
        val findMember = memberRepository.findMemberCustom()
    }

    @Test
    fun spec() {
        // given
        val teamA = Team(name = "teamA")
        teamJpaRepository.save(teamA)

        val member1 = Member(username = "member1", age = 10, team = teamA)
        val member2 = Member(username = "member2", age = 20, team = teamA)
        memberRepository.save(member1)
        memberRepository.save(member2)

        em.flush()
        em.clear()

        // when
        val specification: Specification<Member> = MemberSpec.username("member1").and(MemberSpec.teamName("teamA"))
        val members: MutableList<Member> = memberRepository.findAll(specification)

        // then
        assertThat(members.size).isEqualTo(1)
    }

    // inner join 은 가능, outer join 은 불가능
    @Test
    fun queryByExample() {
        // given
        val teamA = Team(name = "teamA")
        teamJpaRepository.save(teamA)

        val member1 = Member(username = "member1", age = 10, team = teamA)
        val member2 = Member(username = "member2", age = 20, team = teamA)
        memberRepository.save(member1)
        memberRepository.save(member2)

        em.flush()
        em.clear()

        // when
        val member = Member(username = "member1", age = 10, team = null)
        val team = Team(name = "teamA")
        member.team = team

        val exampleMatcher: ExampleMatcher = ExampleMatcher.matching()
            .withIgnorePaths("id", "age", "team.id", "team.name")

        val example: Example<Member> = Example.of(member, exampleMatcher)
        val members: MutableList<Member> = memberRepository.findAll(example)

        // then
        assertThat(members.size).isEqualTo(1)
    }

    @Test
    fun projectionsWithInterface() {
        // given
        val teamA = Team(name = "teamA")
        teamJpaRepository.save(teamA)

        val member1 = Member(username = "member1", age = 10, team = teamA)
        val member2 = Member(username = "member2", age = 20, team = teamA)
        memberRepository.save(member1)
        memberRepository.save(member2)

        em.flush()
        em.clear()

        // when
        val results: List<UsernameOnly> = memberRepository.findProjectionsByUsername("member1")

        // then
        results.forEach{
            println(it)
        }
    }

    @Test
    fun projectionsWithDto() {
        // given
        val teamA = Team(name = "teamA")
        teamJpaRepository.save(teamA)

        val member1 = Member(username = "member1", age = 10, team = teamA)
        val member2 = Member(username = "member2", age = 20, team = teamA)
        memberRepository.save(member1)
        memberRepository.save(member2)

        em.flush()
        em.clear()

        // when
        val results: List<UsernameOnlyDto> = memberRepository.findProjectionsWithDtoByUsername("member1")

        // then
        results.forEach{
            println(it)
        }
    }

    @Test
    fun projectionsWithDtoAndGeneric() {
        // given
        val teamA = Team(name = "teamA")
        teamJpaRepository.save(teamA)

        val member1 = Member(username = "member1", age = 10, team = teamA)
        val member2 = Member(username = "member2", age = 20, team = teamA)
        memberRepository.save(member1)
        memberRepository.save(member2)

        em.flush()
        em.clear()

        // when
        val results: List<UsernameOnlyDto> = memberRepository.findProjectionsWithGenericByUsername("member1", UsernameOnlyDto::class.java)

        // then
        results.forEach{
            println(it)
        }
    }

    @Test
    fun projectionsWithNested() {
        // given
        val teamA = Team(name = "teamA")
        teamJpaRepository.save(teamA)

        val member1 = Member(username = "member1", age = 10, team = teamA)
        val member2 = Member(username = "member2", age = 20, team = teamA)
        memberRepository.save(member1)
        memberRepository.save(member2)

        em.flush()
        em.clear()

        // when
        val results: List<NestedClosedProjections> = memberRepository.findProjectionsWithGenericByUsername("member1", NestedClosedProjections::class.java)

        // then
        results.forEach{
            println(it)
        }
    }

    @Test
    fun nativeQuery() {
        // given
        val teamA = Team(name = "teamA")
        teamJpaRepository.save(teamA)

        val member1 = Member(username = "member1", age = 10, team = teamA)
        val member2 = Member(username = "member2", age = 20, team = teamA)
        memberRepository.save(member1)
        memberRepository.save(member2)

        em.flush()
        em.clear()

        // when
        val members: List<Member> = memberRepository.findByNativeQuery("member1")

        // then
        members.forEach{
            println(it)
        }
    }

    @Test
    fun nativeQueryWithProjection() {
        // given
        val teamA = Team(name = "teamA")
        teamJpaRepository.save(teamA)

        val member1 = Member(username = "member1", age = 10, team = teamA)
        val member2 = Member(username = "member2", age = 20, team = teamA)
        memberRepository.save(member1)
        memberRepository.save(member2)

        em.flush()
        em.clear()

        // when
        val members: Page<MemberProjection> = memberRepository.findByNativeProjection(PageRequest.of(0, 10))

        // then
        members.forEach{
            println("memberId: ${it.getId()}, username: ${it.getUserName()}, teamName: ${it.getTeamName()}")
        }
    }
}