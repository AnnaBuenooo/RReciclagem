package br.com.fiap.rreciclagem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendamentoRequest {

    @NotNull(message = "Data do agendamento é obrigatória")
    @Future(message = "Data do agendamento deve ser no futuro")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataAgendamento;

    @NotBlank(message = "Tipo de resíduo é obrigatório")
    private String tipoResiduo; // Ex: PLASTICO, VIDRO
}