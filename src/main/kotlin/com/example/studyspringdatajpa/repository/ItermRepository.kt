package com.example.studyspringdatajpa.repository

import com.example.studyspringdatajpa.entity.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItermRepository: JpaRepository<Item, Long>