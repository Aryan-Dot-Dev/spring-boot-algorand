package dev.suspicious.backend.pgx.service;

import org.springframework.stereotype.Service;

@Service
public class PhenotypeService {

    public String phenotypeFromDiplotype(String diplotype) {

        // Simplified CYP2C19 rules
        if (diplotype.contains("*2/*2")) {
            return "Poor Metabolizer";
        }

        if (diplotype.contains("*1/*2")) {
            return "Intermediate Metabolizer";
        }

        if (diplotype.contains("*17")) {
            return "Rapid Metabolizer";
        }

        return "Normal Metabolizer";
    }
}
