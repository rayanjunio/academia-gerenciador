package br.com.academia.academia.repositorio;

import br.com.academia.academia.modelo.Funcionario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FuncionarioRepositorio extends CrudRepository<Funcionario, Long> {
    List<Funcionario> findByNameContaining(String name);

    List<Funcionario> findAll();

    Funcionario findById(long id);
}
