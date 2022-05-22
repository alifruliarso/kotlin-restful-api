package com.galapea.belajar.kotlinrestfulapi.config

import com.galapea.belajar.kotlinrestfulapi.entity.ApiKey
import com.galapea.belajar.kotlinrestfulapi.repository.ApiKeyRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class ApiKeySeeder(private val apiKeyRepository: ApiKeyRepository) : ApplicationRunner {

    private val apiKey = "SECRET"

    override fun run(args: ApplicationArguments?) {
        if (!apiKeyRepository.existsById(apiKey)) {
            val entity = ApiKey(id = apiKey)
            apiKeyRepository.save(entity)
        }
    }
}