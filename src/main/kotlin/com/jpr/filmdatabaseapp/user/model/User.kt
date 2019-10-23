package com.jpr.filmdatabaseapp.user.model

import com.jpr.filmdatabaseapp.security.accesstoken.AccessToken
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
@Table(
    name = "users",
    uniqueConstraints = [UniqueConstraint(columnNames = ["email"])]
)
data class User(
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id: Long? = null,

    @Email
    val email: String? = null,

    val name: String? = null,
    val picture: String? = null,

    @Enumerated(EnumType.STRING)
    val role: Role? = null,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val tokens: Set<AccessToken> = HashSet()
) {

    constructor(email: String, name: String, picture: String, role: Role) : this(null, email, name, picture, role)

    data class Dto(val email: String? = null, val name: String? = null, val picture: String? = null)
    fun toDto(): Dto {
        return Dto(this.email, this.name, this.picture)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        val user: User = other
        if(this.id != user.id
        || this.email != user.email
        || this.name != user.name
        || this.picture != user.picture
        || this.role != user.role) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}