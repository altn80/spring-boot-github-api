/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackerrank.github.repository;

import com.hackerrank.github.model.Actor;
import java.util.List;

/**
 *
 * @author andre
 */
public interface CustomActorRepository {

    List<Actor> getActorsOrderedByMaximumStreak();

    List<Actor> getActorsOrderedByNumberOfEvents();
}
