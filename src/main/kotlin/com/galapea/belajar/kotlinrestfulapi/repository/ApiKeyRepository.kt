package com.galapea.belajar.kotlinrestfulapi.repository

import com.galapea.belajar.kotlinrestfulapi.entity.ApiKey
import org.springframework.data.jpa.repository.JpaRepository

interface ApiKeyRepository : JpaRepository<ApiKey, String> {
}