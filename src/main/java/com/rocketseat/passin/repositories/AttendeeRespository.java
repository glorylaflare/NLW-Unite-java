package com.rocketseat.passin.repositories;

import com.rocketseat.passin.domain.attendee.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendeeRespository extends JpaRepository<Attendee, String> {
}
