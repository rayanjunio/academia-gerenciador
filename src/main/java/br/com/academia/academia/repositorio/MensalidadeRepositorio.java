package br.com.academia.academia.repositorio;

import br.com.academia.academia.modelo.Mensalidade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MensalidadeRepositorio extends CrudRepository<Mensalidade, Long> {
    List<Mensalidade> findAll();
    Mensalidade findById(long id);
}
