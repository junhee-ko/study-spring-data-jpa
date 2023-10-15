package com.example.studyspringdatajpa.entity

import jakarta.persistence.*

@Entity
@NamedQuery(
    name = "Member.findByUsername",
    query = "select m from Member m where m.username = :username"
)
@NamedEntityGraph(
    name = "Member.all",
    attributeNodes = [NamedAttributeNode("team")]
)
class Member(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    val id: Long = 0L,

    val username: String,

    val age: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    var team: Team?
) {

    override fun toString(): String {
        return "Member(id=$id, username='$username', age=$age)"
    }
}