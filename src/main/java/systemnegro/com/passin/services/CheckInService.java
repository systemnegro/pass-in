package systemnegro.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import systemnegro.com.passin.domain.attendee.Attendee;
import systemnegro.com.passin.domain.checkin.CheckIn;
import systemnegro.com.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import systemnegro.com.passin.repositories.CheckInRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckInRepository checkInRepository;

    public void registerCheckIn(Attendee attendee) {
        this.verifyCheckInExists(attendee.getId());

        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreatedAt(LocalDateTime.now());

        this.checkInRepository.save(newCheckIn);

    }
    public void verifyCheckInExists(String attendeeId) {
        Optional<CheckIn> isCheckIn = this.getCheckIn(attendeeId);
        if (isCheckIn.isPresent()) throw new CheckInAlreadyExistsException("Attendee already checked in");
    }

    public Optional<CheckIn> getCheckIn(String attendeeId) {
        return this.checkInRepository.findByAttendeeId(attendeeId);
    }
}
