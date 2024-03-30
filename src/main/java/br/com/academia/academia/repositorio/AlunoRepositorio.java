package br.com.academia.academia.repositorio;

import br.com.academia.academia.modelo.Aluno;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlunoRepositorio extends CrudRepository<Aluno, Long> {
    List<Aluno> findByNameContaining(String name);

    List<Aluno> findAll();
    Aluno findById(long id);
}



