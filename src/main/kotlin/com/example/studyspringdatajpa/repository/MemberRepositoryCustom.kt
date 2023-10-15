package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.entity.Member

interface MemberRepositoryCustom {

    fun findMemberCustom(): List<Member>
}