package com.botquest.BotQuestAPI.dtos;

import com.botquest.BotQuestAPI.models.TipoUsuarioModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

public record UsuarioDto(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha,
        @NotNull int vw_id,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @NotNull Date data_nascimento,
        @NotNull UUID id_setor,
        @NotNull TipoUsuarioModel tipo_usuario,
        MultipartFile url_img
        ) {
}
