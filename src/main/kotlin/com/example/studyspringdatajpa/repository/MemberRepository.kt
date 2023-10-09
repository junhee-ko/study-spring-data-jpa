package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.dto.MemberDto
import com.example.studyspringdatajpa.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberRepository: JpaRepository<Member, Long>{

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
}