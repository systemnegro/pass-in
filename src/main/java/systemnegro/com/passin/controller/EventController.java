package systemnegro.com.passin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import systemnegro.com.passin.dto.attendee.AttendeRequestDTO;
import systemnegro.com.passin.dto.attendee.AttendeeIdDTO;
import systemnegro.com.passin.dto.attendee.AttendeesListResponseDTO;
import systemnegro.com.passin.dto.event.EventIdDTO;
import systemnegro.com.passin.dto.event.EventRequestDTO;
import systemnegro.com.passin.dto.event.EventResponseDTO;
import systemnegro.com.passin.services.AttendeeService;
import systemnegro.com.passin.services.EventServices;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventServices eventServices;
    private final AttendeeService attendeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String id) {

        EventResponseDTO event = this.eventServices.getEventDetail(id);

        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
        EventIdDTO eventIdDTO = this.eventServices.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();

        return ResponseEntity.created(uri).body(eventIdDTO);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String eventId, @RequestBody AttendeRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {

        AttendeeIdDTO attendeeIdDTO = this.eventServices.registerAttendeeOnEvent(eventId ,body);

        var uri = uriComponentsBuilder.path("/attendees/{attendId}/badge").buildAndExpand(attendeeIdDTO.attendeeId()).toUri();

        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String id) {

        AttendeesListResponseDTO attendeesListResponse = this.attendeeService.getEventsAteendee(id);

        return ResponseEntity.ok(attendeesListResponse);
    }

}
