package com.jpr.filmdatabaseapp.security

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class UserClientInfo(
    @Column(name = "ip")
    val ipAddress: String? = null
) {
}