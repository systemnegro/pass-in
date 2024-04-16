package systemnegro.com.passin.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import systemnegro.com.passin.domain.attendee.Attendee;
import systemnegro.com.passin.domain.event.Event;
import systemnegro.com.passin.domain.event.exceptions.EventFullException;
import systemnegro.com.passin.domain.event.exceptions.EventNotFoundException;
import systemnegro.com.passin.dto.attendee.AttendeRequestDTO;
import systemnegro.com.passin.dto.attendee.AttendeeIdDTO;
import systemnegro.com.passin.dto.event.EventIdDTO;
import systemnegro.com.passin.dto.event.EventRequestDTO;
import systemnegro.com.passin.dto.event.EventResponseDTO;
import systemnegro.com.passin.repositories.EventRepository;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServices {

    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId) {
        Event event = this.getEventById(eventId);
        List<Attendee> attendeesList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        return new EventResponseDTO(event, attendeesList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventRequestDTO) {
        Event newEvent = new Event();
        newEvent.setTitle(eventRequestDTO.title());
        newEvent.setDetails(eventRequestDTO.details());
        newEvent.setMaximumAttendees(eventRequestDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventRequestDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeRequestDTO attendeRequestDTO) {
        this.attendeeService.verifyAttendeeSubscription(attendeRequestDTO.email(), eventId);

        Event event = this.getEventById(eventId);
        List<Attendee> attendeesList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        if (event.getMaximumAttendees() <= attendeesList.size()) throw new EventFullException("event is full");

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeRequestDTO.name());
        newAttendee.setEmail(attendeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee(newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());

    }

    private Event getEventById(String eventId) {
        return this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with ID:" + eventId));
    }

    private String createSlug(String text) {

        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);

        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();


    }

}
