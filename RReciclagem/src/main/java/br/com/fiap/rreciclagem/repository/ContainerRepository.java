package br.com.fiap.rreciclagem.repository;

import br.com.fiap.rreciclagem.model.Container;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContainerRepository extends JpaRepository<Container, Long> {
    // Query para encontrar containers que atingiram o limite
    @org.springframework.data.jpa.repository.Query("SELECT c FROM Container c WHERE c.nivelAtual >= (c.capacidadeMaxima * 0.9)")
    List<Container> findContainersAtingindoLimite();
}
