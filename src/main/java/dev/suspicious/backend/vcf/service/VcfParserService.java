package dev.suspicious.backend.vcf.service;

import dev.suspicious.backend.vcf.dto.VariantDTO;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class VcfParserService {

    private static final Set<String> TARGET_GENES = Set.of(
            "CYP2D6", "CYP2C19", "CYP2C9",
            "SLCO1B1", "TPMT", "DPYD"
    );

    public List<VariantDTO> parse(File vcfFile) {

        List<VariantDTO> extracted = new ArrayList<>();

        try (VCFFileReader reader = new VCFFileReader(vcfFile, false)) {

            for (VariantContext vc : reader) {

                // Hackathon assumption: INFO has GENE tag
                String gene = vc.getAttributeAsString("GENE", null);

                if (gene == null || !TARGET_GENES.contains(gene)) {
                    continue;
                }

                String rsid = vc.getID();
                String chrom = vc.getContig();
                int pos = vc.getStart();

                String ref = vc.getReference().getBaseString();
                String alt = vc.getAlternateAllele(0).getBaseString();

                // First sample genotype
                var sample = vc.getGenotype(0);

                String genotype = sample.getGenotypeString();

                String zygosity =
                        sample.isHomVar() ? "Homozygous" :
                                sample.isHet() ? "Heterozygous" :
                                        "Normal";

                extracted.add(new VariantDTO(
                        gene,
                        rsid,
                        chrom,
                        pos,
                        ref,
                        alt,
                        genotype,
                        zygosity
                ));
            }
        }

        return extracted;
    }
}
