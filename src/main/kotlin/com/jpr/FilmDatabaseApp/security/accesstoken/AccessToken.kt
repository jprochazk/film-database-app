package com.jpr.FilmDatabaseApp.security.accesstoken

import com.jpr.FilmDatabaseApp.security.UserClientInfo
import com.jpr.FilmDatabaseApp.user.model.User
import javax.persistence.*

@Entity
@Table(
    name = "access_tokens",
    uniqueConstraints = [UniqueConstraint(columnNames = arrayOf("token"))]
)
data class AccessToken(
    @Id
    val token: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    val user: User?,

    @Embedded
    val userClientInfo: UserClientInfo?
)