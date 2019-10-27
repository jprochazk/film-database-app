package com.jpr.filmdatabaseapp.security.accesstoken

import com.jpr.filmdatabaseapp.security.UserClientInfo
import com.jpr.filmdatabaseapp.user.model.User
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
) {

}