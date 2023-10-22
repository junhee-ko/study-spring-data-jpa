package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.dto.MemberDto
import com.example.studyspringdatajpa.entity.Member
import jakarta.persistence.LockModeType
import jakarta.persistence.QueryHint
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.data.repository.query.Param

interface MemberRepository: JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member>{

    fun findByUsernameAndAgeGreaterThan(username: String, age: Int): List<Member>

    @Query(name = "Member.findByUsername") // 사실 이 부분이 없어도 됨. Member entity 에 정의된 Named Query 가 있는지 우선적으로 찾음. 만약에 없으면 메서드 이름으로 쿼리 생성
    fun findByUsername(@Param("username") username: String): List<Member>

    @Query("select m from Member m where m.username = :username and m.age = :age") // 이름이 없는 namedQuery
    fun findMember(@Param("username") username: String, @Param("age") age: Int): List<Member>

    @Query("select m.username from Member m")
    fun findUsernameList(): List<String>

    @Query("select new com.example.studyspringdatajpa.dto.MemberDto(m.id, m.username, m.age) from Member m")
    fun findMemberDto(): List<MemberDto>

    @Query("select m from Member m where m.username in :names")
    fun findByNames(@Param("names") names: List<String>): List<Member>

    fun findByAge(age: Int, pageable: Pageable): Page<Member>

    @Query(value = "select m from Member m left join m.team t",
        countQuery = "select count(m) from Member m")
    fun findWithCountByAge(age: Int, pageable: Pageable): Page<Member>

    fun findSliceByAge(age: Int, pageable: Pageable): Slice<Member>

    fun findListByAge(age: Int, pageable: Pageable): List<Member>

    @Modifying(clearAutomatically = true) // required
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    fun bulkAgePlus(@Param("age") age: Int): Int

    @Query("select m from Member m left join fetch m.team")
    fun findMemberFetchJoin(): List<Member>

    @EntityGraph(attributePaths = ["team"])
    override fun findAll(): List<Member>

    @EntityGraph(attributePaths = ["team"])
    fun findEntityGraphByUsername(@Param("username") username: String): List<Member>

    @EntityGraph("Member.all")
    fun findNamedEntityGraphByUsername(@Param("username") username: String): List<Member>

    @QueryHints(value = [QueryHint(name = "org.hibernate.readOnly", value = "true")])
    fun findReadOnlyByUsername(username: String): Member

    // select for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findLockByUsername(username: String): List<Member>
}