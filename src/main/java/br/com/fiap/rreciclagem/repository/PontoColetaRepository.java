package br.com.fiap.rreciclagem.repository;

import br.com.fiap.rreciclagem.model.PontoColeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {
    // Busca por tipo de resíduo, ignorando case
    List<PontoColeta> findByTipoResiduoAceitoContainingIgnoreCase(String tipoResiduo);
}