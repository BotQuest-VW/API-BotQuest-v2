package com.botquest.BotQuestAPI.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

public record ChamadoDto(@NotBlank  String descricao,
                         @NotNull boolean situacao,
                         @NotNull  Date data_chamado,
                         @NotNull  UUID id_usuario) {
}
