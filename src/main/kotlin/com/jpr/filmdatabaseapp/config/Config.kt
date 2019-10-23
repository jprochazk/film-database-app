package com.jpr.filmdatabaseapp.config

import com.jpr.filmdatabaseapp.security.AccessTokenFilter
import com.jpr.filmdatabaseapp.security.TokenAuthenticationEntryPoint
import com.jpr.filmdatabaseapp.security.UserClientInfoArgResolver
import com.jpr.filmdatabaseapp.user.UserAccessTokenArgResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@PropertySource("classpath:sensitive.properties")
class PropertyConfig {

}

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    @Bean
    fun userAccessTokenArgResolver(): UserAccessTokenArgResolver {
        return UserAccessTokenArgResolver()
    }

    @Bean
    fun userClientInfoArgResolver(): UserClientInfoArgResolver {
        return UserClientInfoArgResolver()
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
            .maxAge(3600)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userAccessTokenArgResolver())
        resolvers.add(userClientInfoArgResolver())
    }
}

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true
)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Value("\${app.unauthorized_access_routes}")
    lateinit var unauthorizedAccessRoutes: String

    @Autowired
    lateinit var entryPoint: TokenAuthenticationEntryPoint

    @Bean
    fun accessTokenFilter(): AccessTokenFilter {
        return AccessTokenFilter()
    }

    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf()
            .disable()
        http.exceptionHandling()
            .authenticationEntryPoint(entryPoint)
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        if(unauthorizedAccessRoutes == "/**") {
            http.authorizeRequests()
                .anyRequest().permitAll()
        } else {
            val authorization = http.authorizeRequests()
            for(route in unauthorizedAccessRoutes.split(";")) {
                authorization.antMatchers(route).permitAll()
            }
            authorization.anyRequest().authenticated()
        }

        http.addFilterBefore(accessTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}