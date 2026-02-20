package dev.suspicious.backend.auth.dto;

import dev.suspicious.backend.model.Role;

public record VerifyRequest(
        String address,
        String signature,
        Role role
) {}
