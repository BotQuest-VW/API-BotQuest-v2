package com.botquest.BotQuestAPI.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SetorDto(@NotNull int cod_setor,
                       @NotBlank String nome) {
}
