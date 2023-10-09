package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Long>