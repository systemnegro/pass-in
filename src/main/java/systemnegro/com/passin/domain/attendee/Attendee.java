package systemnegro.com.passin.domain.attendee;

import jakarta.persistence.*;
import lombok.Data;
import systemnegro.com.passin.domain.event.Event;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendees")
@Data
public class Attendee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
