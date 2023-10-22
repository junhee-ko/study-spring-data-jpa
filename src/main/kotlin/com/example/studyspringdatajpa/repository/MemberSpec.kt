package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.entity.Member
import com.example.studyspringdatajpa.entity.Team
import jakarta.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification

class MemberSpec {

    companion object {
        fun teamName(teamName: String?): Specification<Member> {
            return object : Specification<Member> {
                override fun toPredicate(
                    root: Root<Member>,
                    query: CriteriaQuery<*>,
                    criteriaBuilder: CriteriaBuilder
                ): Predicate? {

                    if (teamName == null) return null

                    // team 과 join
                    val t: Join<Member, Team> = root.join(
                        /* attributeName = */ "team",
                        /* jt = */ JoinType.INNER
                    )

                    return criteriaBuilder.equal(t.get<String>("name"), teamName)
                }
            }
        }

        fun username(username: String?): Specification<Member> {
            return Specification<Member> { root, _, criteriaBuilder ->
                criteriaBuilder.equal(root.get<String>("username"), username)
            }
        }
    }
}