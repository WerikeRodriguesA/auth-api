package com.projetoapi.auth_api.dto.response;

import com.projetoapi.auth_api.domain.Role;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        Role role
) {}