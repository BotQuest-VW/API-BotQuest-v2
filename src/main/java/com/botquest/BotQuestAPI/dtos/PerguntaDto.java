package com.botquest.BotQuestAPI.dtos;

import com.botquest.BotQuestAPI.models.SetorModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PerguntaDto(@NotBlank  String titulo,
                          @NotBlank  String pergunta,
                          @NotNull UUID id_setor) {
}
