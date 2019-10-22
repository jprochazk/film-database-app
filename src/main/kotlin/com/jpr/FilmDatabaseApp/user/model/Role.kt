package com.jpr.FilmDatabaseApp.user.model

enum class Role(
    val inheritedRoles: Set<Role>
) {
    User(emptySet()),
    Admin(setOf(User));

    private val ROLE_PREFIX = "ROLE_"

    fun getRoles(): Set<Role> {
        val set: MutableSet<Role> = mutableSetOf(this)
        set.addAll(this.inheritedRoles)
        return set.toSet()
    }

    override fun toString(): String {
        return ROLE_PREFIX + this.name.toUpperCase()
    }
}