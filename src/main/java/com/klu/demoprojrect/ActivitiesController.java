package com.klu.demoprojrect;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/auth/events")   
@CrossOrigin(origins = "http://localhost:3000") 
public class ActivitiesController {

    private final List<Activity> events = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    // constructor (sample data)
    public ActivitiesController() {
        events.add(new Activity(idGen.getAndIncrement(), "Basketball Club", "Weekly basketball practice"));
        events.add(new Activity(idGen.getAndIncrement(), "Art Society", "Open art studio on weekends"));
        events.add(new Activity(idGen.getAndIncrement(), "Coding Club", "Weekly coding sessions"));
        events.add(new Activity(idGen.getAndIncrement(), "CLUB EVENT", "Weekly coding sessions"));
    }
    
    @GetMapping
    public ResponseEntity<List<Activity>> getAllEvents() {
        return ResponseEntity.ok(events);
    }

   
    @PostMapping
    public ResponseEntity<Activity> createEvent(@RequestBody Activity a) {

        if (a.getTitle() == null || a.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        a.setId(idGen.getAndIncrement());
        events.add(a);

        return ResponseEntity.ok(a);
    }
}