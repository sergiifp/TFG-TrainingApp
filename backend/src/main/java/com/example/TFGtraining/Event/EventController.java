package com.example.TFGtraining.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping
public class EventController {

    @Autowired
    EventService eventService;

    @PostMapping("/clubs/{clubId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    void createEvent(@PathVariable Long clubId, @RequestBody Map<String,String> body){
        String title = body.get("title");
        String description = body.get("description");
        Date eventDate;
        try {
            eventDate = new SimpleDateFormat("yyyy-MM-dd").parse(body.get("eventDate"));
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Date is not in a correct format");
        }
        eventService.createEvent(clubId,title,description, eventDate);
    };
}
