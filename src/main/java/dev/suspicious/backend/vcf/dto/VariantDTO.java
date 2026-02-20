package dev.suspicious.backend.vcf.dto;

public record VariantDTO(
        String gene,
        String rsid,
        String chrom,
        int pos,
        String ref,
        String alt,
        String genotype,
        String zygosity
) {}
