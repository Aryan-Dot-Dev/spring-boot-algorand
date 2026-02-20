package dev.suspicious.backend.pgx.dto;

public record BlockchainPayload(
        String patientId,
        String drug,
        String primaryGene,
        String vcfHash,
        String reportHash,
        String guidelineVersion,
        String timestamp
) {}
