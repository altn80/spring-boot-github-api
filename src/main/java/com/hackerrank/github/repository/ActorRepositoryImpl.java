/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackerrank.github.repository;

import com.hackerrank.github.model.Actor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author andre
 */
@Repository
@Transactional
public class ActorRepositoryImpl implements CustomActorRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Actor> getActorsOrderedByNumberOfEvents() {
        Query query = entityManager.createNativeQuery("SELECT a from Event e inner join e.actor a order by count(a.events)  ", Actor.class);
        return query.getResultList();
    }

    @Override
    public List<Actor> getActorsOrderedByMaximumStreak() {
        Query query = entityManager.createNativeQuery("SELECT a from Event e inner join e.actor a", Actor.class);
        return query.getResultList();
    }

}
