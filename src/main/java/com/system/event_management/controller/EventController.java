package com.system.event_management.controller;

import com.system.event_management.core.EventConstants;
import com.system.event_management.exception.EventNotFoundException;
import com.system.event_management.exception.InvalidEventIDException;
import com.system.event_management.exception.UserException;
import com.system.event_management.model.eventbeans.EventRequestBean;
import com.system.event_management.model.eventbeans.EventResponseBean;
import com.system.event_management.model.rsvpbeans.RSVPRequestBean;
import com.system.event_management.model.rsvpbeans.RSVPResponseBean;
import com.system.event_management.service.EventManagementService;
import com.system.event_management.service.RSVPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventManagementService eventManagementService;

    @Autowired
    private RSVPService rsvpService;

    @GetMapping
    public ResponseEntity<EventResponseBean<?>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        return new ResponseEntity<>(this.eventManagementService.getAllEvents(page, limit), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventResponseBean<?>> createEvent(@RequestBody EventRequestBean eventRequestBean){
        return new ResponseEntity<>(this.eventManagementService.createEvent(eventRequestBean),HttpStatus.OK );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseBean<?>> updateEvent(
            @RequestBody EventRequestBean eventRequestBean,
            @PathVariable("id") String id
    ) throws EventNotFoundException,InvalidEventIDException{
        try{
            return new ResponseEntity<>(this.eventManagementService.updateEvent(eventRequestBean,Long.parseLong(id)),HttpStatus.OK );
        } catch (NumberFormatException e) {
            throw new InvalidEventIDException(EventConstants.EVENT_ID_INVALID);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EventResponseBean<?>> deleteEvent(
            @PathVariable("id") String id
    ) throws EventNotFoundException,InvalidEventIDException {
        try{
            return new ResponseEntity<>(this.eventManagementService.deleteEvent(Long.parseLong(id)),HttpStatus.OK );
        } catch (NumberFormatException e) {
            throw new InvalidEventIDException(EventConstants.EVENT_ID_INVALID);
        }

    }

    @PostMapping("/{eventId}/rsvp")
    public ResponseEntity<RSVPResponseBean<?>> rsvpToEvent(
            @PathVariable Long eventId,
            @RequestBody RSVPRequestBean rsvpRequestBean
            ) throws EventNotFoundException, UserException {

        return new ResponseEntity<>(this.rsvpService.registerRSVP(eventId,rsvpRequestBean),HttpStatus.OK);
    }






}
