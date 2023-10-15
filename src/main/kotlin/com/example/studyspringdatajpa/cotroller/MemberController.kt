package com.example.studyspringdatajpa.cotroller

import com.example.studyspringdatajpa.dto.MemberDto
import com.example.studyspringdatajpa.entity.Member
import com.example.studyspringdatajpa.repository.MemberRepository
import jakarta.annotation.PostConstruct
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
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

    @GetMapping("/members")
    fun findMembers(@PageableDefault(size = 5, sort = ["username"]) pageable: Pageable): Page<MemberDto> {
        val page: Page<Member> = memberRepository.findAll(pageable)

        val map: Page<MemberDto> = page.map {
            MemberDto(
                id = it.id,
                username = it.username,
                age = it.age
            )
        }

        return map
    }

    @PostConstruct
    fun init(){
        (1..100).forEach {
            val member = Member(username = "username + $it", age = it, team = null)
            memberRepository.save(member)
        }
    }
}