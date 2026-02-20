package dev.suspicious.backend.pgx.service;

import dev.suspicious.backend.vcf.dto.VariantDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StarAlleleMapper {

    // Hackathon lookup table
    private static final Map<String, String> RSID_TO_ALLELE = Map.of(
            "rs4244285", "CYP2C19*2",
            "rs12248560", "CYP2C19*17",
            "rs3892097", "CYP2D6*4",
            "rs1799853", "CYP2C9*2",
            "rs1057910", "CYP2C9*3"
    );

    public List<String> mapAlleles(List<VariantDTO> variants) {

        List<String> alleles = new ArrayList<>();

        for (VariantDTO v : variants) {
            if (RSID_TO_ALLELE.containsKey(v.rsid())) {
                alleles.add(RSID_TO_ALLELE.get(v.rsid()));
            }
        }

        return alleles;
    }

    public String determineDiplotype(List<String> alleles, String gene) {

        List<String> geneAlleles =
                alleles.stream()
                        .filter(a -> a.startsWith(gene))
                        .toList();

        if (geneAlleles.isEmpty()) {
            return gene + "*1/*1";
        }

        if (geneAlleles.size() == 1) {
            return geneAlleles.get(0) + "/*1";
        }

        return geneAlleles.get(0) + "/" + geneAlleles.get(1);
    }
}
