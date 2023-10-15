package com.example.studyspringdatajpa.cotroller

import com.example.studyspringdatajpa.entity.Member
import com.example.studyspringdatajpa.repository.MemberRepository
import jakarta.annotation.PostConstruct
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberRepository: MemberRepository
)  {

    @GetMapping("/members/{id}")
    fun findMember(@PathVariable("id") id: Long): String {
        val member = memberRepository.findById(id).get()

        return member.username
    }

    @GetMapping("/members2/{id}")
    fun findMember2(@PathVariable("id") member: Member): String {
        return member.username
    }

    @PostConstruct
    fun init(){
        val member = Member(username = "member1", age = 20, team = null)
        memberRepository.save(member)
    }
}