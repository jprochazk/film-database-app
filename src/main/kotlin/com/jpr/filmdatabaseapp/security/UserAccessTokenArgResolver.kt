package com.jpr.filmdatabaseapp.security

import com.jpr.filmdatabaseapp.exception.UnauthorizedException
import com.jpr.filmdatabaseapp.security.accesstoken.AccessToken
import com.jpr.filmdatabaseapp.security.accesstoken.repository.AccessTokenRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer


class UserAccessTokenArgResolver : HandlerMethodArgumentResolver {

    @Value("\${app.access_token_header_name}")
    lateinit var accessTokenHeaderName: String

    @Autowired
    lateinit var accessTokenRepository: AccessTokenRepository

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun supportsParameter(param: MethodParameter): Boolean {
        return param.parameterType == AccessToken::class.java
    }

    override fun resolveArgument(
        methodParameter: MethodParameter,
        modelAndViewContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        dataBinderFactory: WebDataBinderFactory?
    ): Any? {
        val tokenValue = webRequest.getHeader(accessTokenHeaderName) ?: throw UnauthorizedException()
        return accessTokenRepository.findByTokenWithUser(tokenValue) ?: throw UnauthorizedException()
    }
}