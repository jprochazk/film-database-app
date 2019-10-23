package com.jpr.filmdatabaseapp.security.accesstoken.repository

import com.jpr.filmdatabaseapp.security.accesstoken.AccessToken
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Repository
@Transactional
class AccessTokenRepositoryCustomImpl : AccessTokenRepositoryCustom {

    @PersistenceContext
    private var entityManager: EntityManager? = null

    override fun findByTokenWithUser(token: String): AccessToken? {
        val query = "" +
                "SELECT t " +
                "FROM AccessToken t " +
                "LEFT JOIN FETCH t.user u " +
                "WHERE t.token = :token "

        return try {
            entityManager
                ?.createQuery(query, AccessToken::class.java)
                ?.setParameter("token", token)
                ?.singleResult
        } catch(e: NoResultException) {
            null
        }
    }
}