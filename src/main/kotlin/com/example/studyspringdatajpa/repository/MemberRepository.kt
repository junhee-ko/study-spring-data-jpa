package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberRepository: JpaRepository<Member, Long>{

    fun findByUsernameAndAgeGreaterThan(username: String, age: Int): List<Member>

    @Query(name = "Member.findByUsername") // 사실 이 부분이 없어도 됨. Member entity 에 정의된 Named Query 가 있는지 우선적으로 찾음. 만약에 없으면 메서드 이름으로 쿼리 생성
    fun findByUsername(@Param("username") username: String): List<Member>
}