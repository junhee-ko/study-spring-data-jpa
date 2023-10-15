package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.entity.Item
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ItermRepositoryTest {

    @Autowired
    private lateinit var itermRepository: ItermRepository

    @Test
    fun save() {
        val item = Item()
        itermRepository.save(item)
    }
}