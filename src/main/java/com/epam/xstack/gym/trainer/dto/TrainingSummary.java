package com.epam.xstack.gym.trainer.dto;

import com.epam.xstack.gym.trainer.exception.EmptyRequiredField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.*;

@NoArgsConstructor
@Data
public class TrainingSummary {


    private static final Logger logger = LoggerFactory.getLogger(TrainingSummary.class);

    private Map<Integer, Map<Integer, Integer>> summary = new HashMap<>();

    public Map<Integer, Map<Integer, Integer>> getSummary() {
        return Map.copyOf(summary);
    }

    public TrainingSummary addTraining(LocalDate trainingDate, Integer trainingDuration) {
        checkRequiredFields(trainingDate, trainingDuration);

        if (trainingDuration < 0) {
            throw new EmptyRequiredField("Training Duration can not be negative");
        }

        summary.computeIfAbsent(trainingDate.getYear(), year -> new HashMap<>())
                .merge(trainingDate.getMonthValue(), trainingDuration, Integer::sum);
        return this;
    }

    public void deleteTraining(LocalDate trainingDate, Integer trainingDuration) {
        checkRequiredFields(trainingDate, trainingDuration);

        summary.computeIfPresent(trainingDate.getYear(), (year, monthSummary) -> {
            monthSummary.computeIfPresent(trainingDate.getMonthValue(), (month, summary) -> {
                // Reduce training duration or remove the entry if it becomes zero
                int updatedSummary = summary - trainingDuration;
                return updatedSummary > 0 ? updatedSummary : null; // Remove if <= 0
            });

            // Remove the year entry if the month summary becomes empty
            return monthSummary.isEmpty() ? null : monthSummary;
        });
    }

    private void checkRequiredFields(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                logger.error("One of the required fields is empty");
                throw new EmptyRequiredField("Some of the required fields are empty");
            }
        }
    }

}
