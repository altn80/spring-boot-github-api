package com.hackerrank.github.controller;

import com.hackerrank.github.model.Actor;
import com.hackerrank.github.model.Event;
import com.hackerrank.github.repository.ActorRepository;
import com.hackerrank.github.repository.EventRepository;
import com.hackerrank.github.repository.RepoRepository;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubApiRestController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ActorRepository actorRepository;

    @Autowired
    RepoRepository repoRepository;

    @RequestMapping(name = "/erase", method = RequestMethod.DELETE)
    public ResponseEntity eraseAllEvents() {
        eventRepository.deleteAll();
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/events")
    public ResponseEntity addEvent(@RequestBody Event event) {
        if (eventRepository.findOne(event.getId()) != null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        actorRepository.save(event.getActor());
        repoRepository.save(event.getRepo());
        eventRepository.save(event);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/events")
    public ResponseEntity returnAllEvents() {
        return new ResponseEntity(eventRepository.findAll().
                stream().sorted((a, b) -> a.getId().compareTo(b.getId())).
                collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/events/actors/{actorID}")
    public ResponseEntity returnEventsFilteredByActor(@PathVariable(name = "actorID") Long id) {
        return new ResponseEntity(eventRepository.findAll().
                stream().filter(event -> id.equals(event.getActor().getId())).sorted((a, b) -> a.getId().compareTo(b.getId())).
                collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping("/actors")
    public ResponseEntity updateActorAvatar(@RequestBody Actor actor) {
        Actor persistedActor = actorRepository.findOne(actor.getId());
        if (persistedActor == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if (!persistedActor.getLogin().equals(actor.getLogin())) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        persistedActor.setAvatar_url(actor.getAvatar_url());
        actorRepository.save(persistedActor);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/actors")
    public ResponseEntity returnActorsNumberOfEvents() {
        Comparator<Actor> comparator = Comparator.comparing(Actor::getTotalEvents).thenComparing(Actor::getLastEvent).reversed().thenComparing(Actor::getLogin);
        return new ResponseEntity(actorRepository.findAll().stream().sorted(comparator).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/actors/streak")
    public ResponseEntity returnActorsMaximumStreak() {
        Comparator<Actor> comparator = Comparator.comparing(Actor::getMaximumStreak).thenComparing(Actor::getLastEvent).reversed().thenComparing(Actor::getLogin);
        return new ResponseEntity(actorRepository.findAll().stream().sorted(comparator).collect(Collectors.toList()), HttpStatus.OK);
    }

}
