/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackerrank.github.model;

import java.util.Comparator;

/**
 *
 * @author andre
 */
public class MaximumEventsComparator implements Comparator<Actor> {

    @Override
    public int compare(Actor o1, Actor o2) {
        int sizeCompare = Integer.compare(o1.getEvents().size(), o2.getEvents().size());
        if (sizeCompare != 0) {
            return sizeCompare;
        }
        if (!o1.getEvents().isEmpty() && !o2.getEvents().isEmpty()) {
            o1.getEvents().sort((e1, e2) -> e2.getCreated_at().compareTo(e1.getCreated_at()));
            o2.getEvents().sort((e1, e2) -> e2.getCreated_at().compareTo(e1.getCreated_at()));
            int lastEventCompare = o1.getEvents().get(0).getCreated_at().compareTo(o2.getEvents().get(0).getCreated_at());
            if(lastEventCompare != 0) {
                return lastEventCompare;
            }
        }
        return o1.getLogin().compareTo(o2.getLogin());
    }

}
