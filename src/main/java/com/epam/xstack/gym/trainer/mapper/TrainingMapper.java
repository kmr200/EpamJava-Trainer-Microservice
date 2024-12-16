package com.epam.xstack.gym.trainer.mapper;

import com.epam.xstack.gym.trainer.dto.TrainingDTO;
import com.epam.xstack.gym.trainer.jpa.entity.TrainingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TrainerMapper.class})
public interface TrainingMapper {

    @Mapping(target = "UUID", source = "trainingUUID")
    TrainingDTO toDto(TrainingEntity trainingEntity);

    TrainingEntity toEntity(TrainingDTO trainingDTO);

}
