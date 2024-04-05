package com.rocketseat.passin.services;

import com.rocketseat.passin.domain.attendee.Attendee;
import com.rocketseat.passin.domain.checkin.CheckIn;
import com.rocketseat.passin.domain.checkin.exceptions.CheckInAlreadyExistException;
import com.rocketseat.passin.repositories.CheckinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckinRepository checkinRepository;

    public void registerCheckIn(Attendee attendee){
        this.verifyCheckInExists(attendee.getId());

        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreateAt(LocalDateTime.now());
        this.checkinRepository.save(newCheckIn);
    }

    private void verifyCheckInExists(String attendeeId){
        Optional<CheckIn> isCheckIn = this.getCheckIn(attendeeId);
        if(isCheckIn.isPresent()) throw new CheckInAlreadyExistException("Attendee already checked in.");
    }

    public Optional<CheckIn> getCheckIn(String attendeeId){
        return this.checkinRepository.findByAttendeeId(attendeeId);
    }
}
