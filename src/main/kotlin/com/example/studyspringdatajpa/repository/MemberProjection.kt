package com.example.studyspringdatajpa.repository

interface MemberProjection {

    fun getId(): Long

    fun getUserName(): String

    fun getTeamName(): String
}