package dev.suspicious.backend.pgx.repo;

import dev.suspicious.backend.pgx.entity.PgxReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PgxReportRepository extends JpaRepository<PgxReport, UUID> {

    Optional<PgxReport> findTopByWalletAddressAndDrugOrderByCreatedAtDesc(
            String walletAddress,
            String drug
    );
}
