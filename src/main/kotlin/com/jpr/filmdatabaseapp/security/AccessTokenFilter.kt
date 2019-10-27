package com.jpr.filmdatabaseapp.security

import com.jpr.filmdatabaseapp.user.model.User
import com.jpr.filmdatabaseapp.user.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

// filter that will search a custom header for a token
// and authorize the request based on it
class AccessTokenFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var userRepository: UserRepository

    @Value("\${app.access_token_header_name}")
    lateinit var tokenHeaderName: String

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    private fun filter(request: HttpServletRequest) {
        val token: String = request.getHeader(tokenHeaderName) ?: return
        val user: User = userRepository.findByToken(token) ?: return

        // if user != null, set authentication in security context using user's roles
        user.role?.getRoles()
        ?.map {
            SimpleGrantedAuthority(it.toString()) // map each to authority
        }?.let { authorities ->
            val tokenAuthentication = UsernamePasswordAuthenticationToken(null, null, authorities) // build authentication token
            tokenAuthentication.setDetails(WebAuthenticationDetailsSource().buildDetails(request)) // insert request details
            SecurityContextHolder.getContext().setAuthentication(tokenAuthentication) // set security context authentication
        }
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        filter(request)
        filterChain.doFilter(request, response)
    }
}