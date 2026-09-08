package com.erozah.kestachet.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ShareListRequest(
    @NotBlank(message = "L'identifiant de l'utilisateur cible est obligatoire") String targetUserId
) {}
