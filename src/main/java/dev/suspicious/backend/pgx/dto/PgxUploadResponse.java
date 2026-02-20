package dev.suspicious.backend.pgx.dto;

import dev.suspicious.backend.pgx.entity.PgxReport;

public record PgxUploadResponse(
        String id,
        String gene,
        String diplotype,
        String phenotype,
        String drug,
        String riskLevel,
        String recommendation,
        BlockchainPayload blockchainPayload
) {}
