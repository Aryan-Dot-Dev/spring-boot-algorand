package dev.suspicious.backend.pgx.dto;

public record DrugRecommendation(
        String drug,
        String riskLevel,
        String recommendation
) {}
