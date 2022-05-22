package com.galapea.belajar.kotlinrestfulapi.auth

import com.galapea.belajar.kotlinrestfulapi.error.UnauthorizedException
import com.galapea.belajar.kotlinrestfulapi.repository.ApiKeyRepository
import org.springframework.stereotype.Component
import org.springframework.ui.ModelMap
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.WebRequestInterceptor

@Component
class ApiKeyInterceptor(val apiKeyRepository: ApiKeyRepository) : WebRequestInterceptor {
    override fun preHandle(request: WebRequest) {
        val apiKey = request.getHeader("X-Api-Key") ?: throw UnauthorizedException()
        if (!apiKeyRepository.existsById(apiKey)) {
            throw UnauthorizedException()
        }
    }

    override fun postHandle(request: WebRequest, model: ModelMap?) {
        //
    }

    override fun afterCompletion(request: WebRequest, ex: Exception?) {
        //
    }
}