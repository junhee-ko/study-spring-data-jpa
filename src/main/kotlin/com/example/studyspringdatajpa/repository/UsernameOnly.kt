package com.example.studyspringdatajpa.repository

import org.springframework.beans.factory.annotation.Value

interface UsernameOnly {

    @Value("#{target.username + ' ' + target.age}") // open projection - entity 필드 모두 조회해서 처리
    fun getUsername(): String
}