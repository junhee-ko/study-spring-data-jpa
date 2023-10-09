package com.example.studyspringdatajpa.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Team(

    @Id @GeneratedValue
    @Column(name = "team_id")
    val id: Long = 0L,

    val name: String,

    @OneToMany(mappedBy = "team")
    val members: MutableList<Member> = mutableListOf()
){

    override fun toString(): String {
        return "Team(id=$id, name='$name')"
    }
}