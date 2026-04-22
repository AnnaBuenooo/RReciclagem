package br.com.fiap.rreciclagem.controller;

import br.com.fiap.rreciclagem.model.PontoColeta;
import br.com.fiap.rreciclagem.repository.PontoColetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pontos-coleta")
@RequiredArgsConstructor
public class PontoColetaController {

    private final PontoColetaRepository pontoColetaRepository;

    /**
     * Endpoint público para encontrar pontos de coleta.
     * Pode filtrar por tipo de resíduo.
     * GET /api/pontos-coleta
     * GET /api/pontos-coleta?tipo=PLASTICO
     */
    @GetMapping
    public ResponseEntity<List<PontoColeta>> listarPontosColeta(
            @RequestParam(required = false) String tipo) {

        if (tipo != null && !tipo.isEmpty()) {
            return ResponseEntity.ok(pontoColetaRepository.findByTipoResiduoAceitoContainingIgnoreCase(tipo));
        }
        return ResponseEntity.ok(pontoColetaRepository.findAll());
    }
}