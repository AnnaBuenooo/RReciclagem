package br.com.fiap.rreciclagem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContainerNivelRequest {

    @NotNull(message = "Nível atual é obrigatório")
    @Min(value = 0, message = "Nível não pode ser negativo")
    private Double nivelAtual;
}