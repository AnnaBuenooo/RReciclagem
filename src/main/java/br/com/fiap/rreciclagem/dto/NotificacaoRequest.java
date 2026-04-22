package br.com.fiap.rreciclagem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificacaoRequest {

    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    @NotBlank(message = "Mensagem é obrigatória")
    private String mensagem;
}