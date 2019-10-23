package com.jpr.filmdatabaseapp.security

import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class UserClientInfoArgResolver : HandlerMethodArgumentResolver {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun supportsParameter(param: MethodParameter): Boolean {
        return param.parameterType == UserClientInfo::class.java
    }

    override fun resolveArgument(
        methodParameter: MethodParameter,
        modelAndViewContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        dataBinderFactory: WebDataBinderFactory?
    ): Any? {

        return UserClientInfo("IP ADDRESS")
    }
}