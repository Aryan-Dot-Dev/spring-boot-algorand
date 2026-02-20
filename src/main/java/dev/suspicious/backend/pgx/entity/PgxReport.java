package dev.suspicious.backend.pgx.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(name = "pgx_reports")
public class PgxReport {

    @Id
    @GeneratedValue
    private UUID id;

    private String walletAddress;

    private String gene;
    private String diplotype;
    private String phenotype;

    private String drug;
    private String riskLevel;

    @Column(length = 2000)
    private String recommendation;

    private Instant createdAt = Instant.now();

    private String vcfHash;
    private String reportHash;

    public PgxReport() {}

    public PgxReport(
            String walletAddress,
            String gene,
            String diplotype,
            String phenotype,
            String drug,
            String riskLevel,
            String recommendation,
            String vcfHash,
            String reportHash
    ) {
        this.walletAddress = walletAddress;
        this.gene = gene;
        this.diplotype = diplotype;
        this.phenotype = phenotype;
        this.drug = drug;
        this.riskLevel = riskLevel;
        this.recommendation = recommendation;
        this.vcfHash = vcfHash;
        this.reportHash = reportHash;
    }
}
