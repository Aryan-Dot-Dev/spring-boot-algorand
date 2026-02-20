package dev.suspicious.backend.pgx.service;

import dev.suspicious.backend.pgx.dto.DrugRecommendation;
import dev.suspicious.backend.pgx.entity.PgxReport;
import dev.suspicious.backend.pgx.repo.PgxReportRepository;
import dev.suspicious.backend.util.HashUtil;
import dev.suspicious.backend.vcf.dto.VariantDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PgxPipelineService {

    private final StarAlleleMapper mapper;
    private final PhenotypeService phenotypeService;
    private final CpicRuleEngine ruleEngine;
    private final PgxReportRepository repo;
    private final HashUtil hashUtil;

    public PgxPipelineService(
            StarAlleleMapper mapper,
            PhenotypeService phenotypeService,
            CpicRuleEngine ruleEngine,
            PgxReportRepository repo,
            HashUtil hashUtil
    ) {
        this.mapper = mapper;
        this.phenotypeService = phenotypeService;
        this.ruleEngine = ruleEngine;
        this.repo = repo;
        this.hashUtil = hashUtil;
    }

    public PgxReport runPipeline(
            String walletAddress,
            List<VariantDTO> variants,
            String drug
    ) {
        // Step 1: Map star alleles
        List<String> alleles = mapper.mapAlleles(variants);

        // Step 2: Determine diplotype (example CYP2C19)
        String diplotype = mapper.determineDiplotype(alleles, "CYP2C19");

        // Step 3: Phenotype
        String phenotype = phenotypeService.phenotypeFromDiplotype(diplotype);

        // Step 4: CPIC Recommendation
        DrugRecommendation rec = ruleEngine.evaluate(drug, phenotype);

        String reportJson = phenotype + "|" + rec.recommendation();

        String reportHash = hashUtil.sha256(reportJson);
        String vcfHash = hashUtil.sha256(variants.toString());

        // Step 5: Store report
        PgxReport report = new PgxReport(
                walletAddress,
                "CYP2C19",
                diplotype,
                phenotype,
                drug,
                rec.riskLevel(),
                rec.recommendation(),
                reportHash,
                vcfHash
        );

        return repo.save(report);
    }
}
