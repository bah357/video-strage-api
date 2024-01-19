package com.demo.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackerrank.github.dto.ActorDTO;
import com.hackerrank.github.dto.EventDTO;
import com.hackerrank.github.model.Actor;
import com.hackerrank.github.model.Event;
import com.hackerrank.github.service.ActorService;
import com.hackerrank.github.service.EventService;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class TestController {

    @Autowired
    private ActorService actorService;
    @Autowired
    private EventService eventService;
    @Autowired
    private ModelMapper mapper;

    @DeleteMapping("/erase")
    public ResponseEntity<Void> deleteAllEvents() {
        eventService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/events", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveEvent(@RequestBody EventDTO eventDTO) {
        eventService.save(mapper.map(eventDTO, Event.class));
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getEvents() {
        Iterable<Event> iterable = eventService.findAll();

        List<EventDTO> events = new ArrayList<>();
        iterable.forEach(event -> events.add(mapper.map(event, EventDTO.class)));

        return ResponseEntity.ok().body(events);
    }

    @GetMapping("/events/actors/{id}")
    public ResponseEntity<List<EventDTO>> getEventsByActorId(@PathVariable("id") Long actorId) {
        List<Event> events = eventService.getEventsByActorId(actorId);

        List<EventDTO> eventsDTO = events.stream().map(e -> mapper.map(e, EventDTO.class)).collect(Collectors.toList());

        return ResponseEntity.ok().body(eventsDTO);
    }

    @PutMapping(path = "/actors", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateActor(@RequestBody ActorDTO actorDTO) {
        actorService.update(mapper.map(actorDTO, Actor.class));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/actors")
    public ResponseEntity<List<ActorDTO>> getActorsOrderedByTotalNumberOfEvents() {
        Iterable<Actor> iterable = actorService.getActorsOrderedByTotalNumberOfEvents();

        List<ActorDTO> actors = new ArrayList<>();
        iterable.forEach(actor -> actors.add(mapper.map(actor, ActorDTO.class)));

        return ResponseEntity.ok().body(actors);
    }

    @GetMapping("/actors/streak")
    public ResponseEntity<List<ActorDTO>> getActorsOrderedByMaximumStreak() {
        List<Actor> actors = actorService.getActorsOrderedByMaximumStreak();

        List<ActorDTO> actorsDTO = actors.stream().map(a -> mapper.map(a, ActorDTO.class)).collect(Collectors.toList());

        return ResponseEntity.ok().body(actorsDTO);
    }
}