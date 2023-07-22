package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.Challenge;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class ChallengeDao {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String BASE_CHALLENGE_QUERY = "SELECT c FROM Challenge c ORDER BY c.id DESC";

    public Challenge findNextChallenge() {
        // select c from Challenge c order by c.id desc limit 1
        TypedQuery<Challenge> query = entityManager.createQuery(BASE_CHALLENGE_QUERY, Challenge.class);
        query.setMaxResults(1);
        return query.getSingleResult();
    }

    public Challenge findCurrentChallenge() {
        // select c from Challenge c order by c.id desc limit(1, 1)
        TypedQuery<Challenge> query = entityManager.createQuery(BASE_CHALLENGE_QUERY, Challenge.class);
        query.setMaxResults(1);
        query.setFirstResult(1);
        return query.getSingleResult();
    }
}
