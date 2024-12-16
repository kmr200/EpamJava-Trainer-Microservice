package com.epam.xstack.gym.trainer.mapper;

import com.epam.xstack.gym.trainer.dto.TrainerDTO;
import com.epam.xstack.gym.trainer.jpa.entity.TrainerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    TrainerDTO toDTO(TrainerEntity trainerEntity);

    TrainerEntity toEntity(TrainerDTO dto);

}
