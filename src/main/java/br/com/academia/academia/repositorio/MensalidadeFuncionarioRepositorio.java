package br.com.academia.academia.repositorio;

import br.com.academia.academia.modelo.MensalidadeFuncionario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensalidadeFuncionarioRepositorio extends CrudRepository<MensalidadeFuncionario, Long> {
}
