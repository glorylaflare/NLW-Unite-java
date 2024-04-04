package com.rocketseat.passin.services;

import com.rocketseat.passin.domain.attendee.Attendee;
import com.rocketseat.passin.domain.checkin.CheckIn;
import com.rocketseat.passin.dto.attendee.AttendeeDetails;
import com.rocketseat.passin.dto.attendee.AttendeesListResponseDTO;
import com.rocketseat.passin.repositories.AttendeeRespository;
import com.rocketseat.passin.repositories.CheckinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRespository attendeeRespository;
    private final CheckinRepository checkinRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return this.attendeeRespository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkinRepository.findByAttendeeId(attendee.getId());
            LocalDateTime checkInAt = checkIn.<LocalDateTime>map(CheckIn::getCreateAt).orElse(null);
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }
}
