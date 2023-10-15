package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.entity.Member
import jakarta.persistence.EntityManager

// naming rule: MemberRepository + Impl
class MemberRepositoryImpl(
    private val entityManager: EntityManager
) : MemberRepositoryCustom {

    override fun findMemberCustom(): List<Member> {
        return entityManager.createQuery("select m from Member m", Member::class.java)
            .resultList.toList()
    }
}