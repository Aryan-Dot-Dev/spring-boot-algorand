package dev.suspicious.backend.vcf.controller;

import dev.suspicious.backend.pgx.dto.BlockchainPayload;
import dev.suspicious.backend.pgx.dto.PgxUploadResponse;
import dev.suspicious.backend.pgx.entity.PgxReport;
import dev.suspicious.backend.pgx.repo.PgxReportRepository;
import dev.suspicious.backend.pgx.service.PgxPipelineService;
import dev.suspicious.backend.vcf.dto.VariantDTO;
import dev.suspicious.backend.vcf.service.VcfParserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/vcf")
public class VcfController {

    private final VcfParserService parser;
    private final PgxPipelineService pipeline;
    private final PgxReportRepository reportRepository;

    public VcfController(
            VcfParserService parser,
            PgxPipelineService pipeline,
            PgxReportRepository reportRepository
    ) {
        this.parser = parser;
        this.pipeline = pipeline;
        this.reportRepository = reportRepository;
    }

    // ============================
    // 1️⃣ Upload + Process
    // ============================

    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("wallet") String walletAddress,
            @RequestParam("drug") String drug
    ) {

        try {

            File tempFile = File.createTempFile("upload-", ".vcf");
            file.transferTo(tempFile);

            List<VariantDTO> variants = parser.parse(tempFile);
            tempFile.delete();

            PgxReport report = pipeline.runPipeline(walletAddress, variants, drug);

            BlockchainPayload payload = new BlockchainPayload(
                    walletAddress,
                    report.getDrug(),
                    report.getGene(),
                    report.getVcfHash(),
                    report.getReportHash(),
                    "CPIC-2024.1",
                    report.getCreatedAt().toString()
            );

            PgxUploadResponse response = new PgxUploadResponse(
                    report.getId().toString(),
                    report.getGene(),
                    report.getDiplotype(),
                    report.getPhenotype(),
                    report.getDrug(),
                    report.getRiskLevel(),
                    report.getRecommendation(),
                    payload
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("VCF processing failed: " + e.getMessage());
        }
    }

    // ============================
    // 2️⃣ Get Metadata (From DB)
    // ============================

    @GetMapping("/metadata")
    public ResponseEntity<?> getMetadata(
            @RequestParam("patientId") String wallet,
            @RequestParam("drug") String drug
    ) {

        try {

            return reportRepository
                    .findTopByWalletAddressAndDrugOrderByCreatedAtDesc(wallet, drug)
                    .map(report -> {

                        BlockchainPayload payload = new BlockchainPayload(
                                report.getWalletAddress(),
                                report.getDrug(),
                                report.getGene(),
                                report.getVcfHash(),
                                report.getReportHash(),
                                "CPIC-2024.1",
                                report.getCreatedAt().toString()
                        );

                        PgxUploadResponse response = new PgxUploadResponse(
                                report.getId().toString(),
                                report.getGene(),
                                report.getDiplotype(),
                                report.getPhenotype(),
                                report.getDrug(),
                                report.getRiskLevel(),
                                report.getRecommendation(),
                                payload
                        );

                        return ResponseEntity.ok(response);
                    })
                    .orElseGet(() ->
                            ResponseEntity.status(HttpStatus.NOT_FOUND)
                                    .body(null)
                    );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Metadata fetch failed: " + e.getMessage());
        }
    }
}
