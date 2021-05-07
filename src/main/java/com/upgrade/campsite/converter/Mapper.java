package com.upgrade.campsite.converter;


import com.upgrade.campsite.dto.AvailabilityDTO;
import com.upgrade.campsite.dto.ReservationDTO;
import com.upgrade.campsite.model.AvailabilityEntity;
import com.upgrade.campsite.model.ReservationEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class Mapper {

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
