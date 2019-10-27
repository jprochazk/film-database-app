package com.jpr.filmdatabaseapp.user

import com.jpr.filmdatabaseapp.security.UserClientInfo
import com.jpr.filmdatabaseapp.security.accesstoken.AccessToken
import com.jpr.filmdatabaseapp.security.accesstoken.repository.AccessTokenRepository
import com.jpr.filmdatabaseapp.user.model.Role
import com.jpr.filmdatabaseapp.user.model.User
import com.jpr.filmdatabaseapp.user.repository.UserRepository
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import java.util.*
import javax.transaction.Transactional

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@RunWith(SpringRunner::class)
@AutoConfigureMockMvc
class UserControllerApiTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var accessTokenRepository: AccessTokenRepository

    @Test
    @Transactional
    fun `valid access token returns user`() {
        val test_user = userRepository.save(User("email@test.com", "Test User", "https://picture.url/test_user.jpeg", Role.User))
        val test_accessToken = accessTokenRepository.save(AccessToken(UUID.randomUUID().toString(), test_user, UserClientInfo("IP_ADDRESS")))

        mockMvc.perform(
            get("/user/profile")
                .header("x-access-token", test_accessToken.token))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.email").value("email@test.com"))
            .andExpect(jsonPath("$.name").value("Test User"))
            .andExpect(jsonPath("$.picture").value("https://picture.url/test_user.jpeg"))
    }

    @Test
    @Transactional
    fun `invalid access token returns 401`() {
        val test_user = userRepository.save(User("email@test.com", "Test User", "https://picture.url/test_user.jpeg", Role.User))

        mockMvc.perform(
            get("/user/profile")
                .header("x-access-token", UUID.randomUUID().toString()))
            .andDo(print())
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.message").value("Unauthorized"))
    }

    @Test
    @Transactional
    fun `valid access token signs out user`() {
        val test_user = userRepository.save(User("email@test.com", "Test User", "https://picture.url/test_user.jpeg", Role.User))
        val test_accessToken = accessTokenRepository.save(AccessToken(UUID.randomUUID().toString(), test_user, UserClientInfo("IP_ADDRESS")))

        mockMvc.perform(
            post("/user/signout")
                .header("x-access-token", test_accessToken.token))
            .andDo(print())
            .andExpect(status().isOk)

        Assert.assertFalse(accessTokenRepository.existsById(test_accessToken.token!!))
    }

}