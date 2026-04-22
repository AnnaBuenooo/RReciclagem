package br.com.fiap.rreciclagem.controller;

import br.com.fiap.rreciclagem.dto.ContainerNivelRequest;
import br.com.fiap.rreciclagem.model.Container;
import br.com.fiap.rreciclagem.service.ContainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/containers")
@RequiredArgsConstructor
public class ContainerController {

    private final ContainerService containerService;

    /**
     * Endpoint público (simulado) para um container (IoT) reportar seu nível.
     * Isso dispara a lógica de "alerta automático".
     * PUT /api/containers/{id}/nivel
     */
    @PutMapping("/{id}/nivel")
    public ResponseEntity<Container> reportarNivel(
            @PathVariable Long id,
            @Valid @RequestBody ContainerNivelRequest request) {

        Container containerAtualizado = containerService.atualizarNivel(id, request);
        return ResponseEntity.ok(containerAtualizado);
    }

    /**
     * Endpoint (seria protegido) para um dashboard de coleta ver quais containers
     * precisam de coleta.
     * GET /api/containers/alertas
     */
    @GetMapping("/alertas")
    public ResponseEntity<List<Container>> getAlertasDeColeta() {
        return ResponseEntity.ok(containerService.getContainersProntosParaColeta());
    }
}