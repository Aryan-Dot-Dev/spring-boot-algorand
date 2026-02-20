package dev.suspicious.backend.pgx.service;

import dev.suspicious.backend.pgx.dto.DrugRecommendation;
import org.springframework.stereotype.Service;

@Service
public class CpicRuleEngine {

    public DrugRecommendation evaluate(String drug, String phenotype) {

        if (drug.equalsIgnoreCase("Clopidogrel")
                && phenotype.equals("Poor Metabolizer")) {

            return new DrugRecommendation(
                    drug,
                    "High Risk",
                    "Avoid clopidogrel. Use prasugrel or ticagrelor."
            );
        }

        if (drug.equalsIgnoreCase("Warfarin")
                && phenotype.equals("Intermediate Metabolizer")) {

            return new DrugRecommendation(
                    drug,
                    "Moderate Risk",
                    "Lower starting dose recommended."
            );
        }

        return new DrugRecommendation(
                drug,
                "Safe",
                "Standard dosing."
        );
    }
}
