package com.rocketseat.passin.repositories;

import com.rocketseat.passin.domain.attendee.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendeeRespository extends JpaRepository<Attendee, String> {
    List<Attendee> findByEventId(String eventId);
}
