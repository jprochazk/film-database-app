package com.jpr.FilmDatabaseApp.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class AuthenticationToken(authorities: Collection<GrantedAuthority>) : AbstractAuthenticationToken(authorities) {
    override fun getCredentials(): Any? {
        TODO("not implemented")
    }

    override fun getPrincipal(): Any? {
        TODO("not implemented")
    }
}