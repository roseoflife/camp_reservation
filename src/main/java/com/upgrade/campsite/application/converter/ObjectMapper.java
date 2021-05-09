package com.upgrade.campsite.application.converter;


import com.upgrade.campsite.domain.model.AvailabilityEntity;
import com.upgrade.campsite.domain.model.ReservationEntity;
import com.upgrade.campsite.interfaces.dto.AvailabilityDTO;
import com.upgrade.campsite.interfaces.dto.ReservationDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class ObjectMapper {

    @Autowired
    public ModelMapper modelMapper;

    public ReservationDTO convertToDto(ReservationEntity reservationEnt) {
        return modelMapper.map(reservationEnt, ReservationDTO.class);
    }

    public ReservationEntity convertToEntity(ReservationDTO reservationDTO) {
        return modelMapper.map(reservationDTO, ReservationEntity.class);
    }

    public AvailabilityDTO convertToDto(AvailabilityEntity availabilityEntity) {
        return modelMapper.map(availabilityEntity, AvailabilityDTO.class);
    }

    public AvailabilityEntity convertToEntity(AvailabilityDTO availabilityDTO) {
        return modelMapper.map(availabilityDTO, AvailabilityEntity.class);
    }

}
