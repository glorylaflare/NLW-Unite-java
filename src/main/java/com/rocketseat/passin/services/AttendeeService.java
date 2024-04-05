package com.rocketseat.passin.services;

import com.rocketseat.passin.domain.attendee.Attendee;
import com.rocketseat.passin.domain.attendee.exceptions.AttendeNotFoundException;
import com.rocketseat.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.rocketseat.passin.domain.checkin.CheckIn;
import com.rocketseat.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.rocketseat.passin.dto.attendee.AttendeeDetails;
import com.rocketseat.passin.dto.attendee.AttendeesListResponseDTO;
import com.rocketseat.passin.dto.attendee.AttendeeBadgeDTO;
import com.rocketseat.passin.repositories.AttendeeRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRespository attendeeRespository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return this.attendeeRespository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkInAt = checkIn.<LocalDateTime>map(CheckIn::getCreateAt).orElse(null);
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }

    public void verifyAttendeeSubscription(String email, String eventId){
        Optional<Attendee> isAttendeeRegistered = this.attendeeRespository.findByEventIdAndEmail(eventId, email);
        if(isAttendeeRegistered.isPresent()) throw new AttendeeAlreadyExistException("Attendee is already registered.");
    }

    public Attendee registerAttendee(Attendee newAttendee){
        this.attendeeRespository.save(newAttendee);
        return newAttendee;
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        Attendee attendee = this.getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDTO attendeeBadgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());
        return new AttendeeBadgeResponseDTO(attendeeBadgeDTO);
    }

    public void checkInAttendee(String attendeeId){
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
    }

    private Attendee getAttendee(String attendeeId){
        return this.attendeeRespository.findById(attendeeId).orElseThrow(() -> new AttendeNotFoundException("Attendee not found with ID: " + attendeeId));
    }
}
