package com.jpr.filmdatabaseapp.security

import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest


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
        val ip = webRequest.toHttpServletRequest()?.getActualRemoteAddr() ?: throw Exception("Could not get IP address of user!")
        return UserClientInfo(ip)
    }

    private fun NativeWebRequest.toHttpServletRequest(): HttpServletRequest? {
        return this.getNativeRequest(HttpServletRequest::class.java)
    }

    private fun HttpServletRequest.getActualRemoteAddr(): String? {
        return this.getHeader("X-FORWARDED-FOR") ?: this.remoteAddr
    }
}