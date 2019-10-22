package com.jpr.FilmDatabaseApp.user.repository

import com.jpr.FilmDatabaseApp.user.model.User
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Repository
@Transactional
class UserRepositoryImpl : UserRepositoryCustom {

    @PersistenceContext
    private var entityManager: EntityManager? = null

    override fun findByToken(token: String): User? {
        val query = "" +
                "SELECT u " +
                "FROM User u " +
                "LEFT JOIN FETCH u.tokens t " +
                "WHERE t.token = :token "

        return try {
            entityManager
                ?.createQuery(query, User::class.java)
                ?.setParameter("token", token)
                ?.singleResult
        } catch(e: NoResultException) {
            null
        }
    }
}