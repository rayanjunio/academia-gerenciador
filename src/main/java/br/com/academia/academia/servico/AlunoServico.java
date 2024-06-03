package br.com.academia.academia.servico;

import br.com.academia.academia.excecoes.NullIdentifierException;
import br.com.academia.academia.excecoes.ObjectNotFoundException;
import br.com.academia.academia.excecoes.PendingPaymentException;
import br.com.academia.academia.modelo.Aluno;
import br.com.academia.academia.modelo.Mensalidade;
import br.com.academia.academia.repositorio.AlunoRepositorio;
import br.com.academia.academia.repositorio.MensalidadeRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoServico {

  @Autowired
  private AlunoRepositorio alunoRepositorio;

  @Autowired
  private MensalidadeRepositorio mensalidadeRepositorio;

  public ResponseEntity<?> cadastrar(Aluno aluno) {
    Mensalidade mensalidade = new Mensalidade();
    mensalidadeRepositorio.save(mensalidade);
    aluno.setMensalidade(mensalidade);
    return new ResponseEntity<>(alunoRepositorio.save(aluno), HttpStatus.CREATED);
  }

  public ResponseEntity<?> exibirTodos() {
    List<Aluno> alunos = alunoRepositorio.findAll();
    return new ResponseEntity<>(alunos, HttpStatus.OK);
  }

  public ResponseEntity<?> exibirPorNome(String name) {
    List<Aluno> alunos = alunoRepositorio.findByNameContaining(name);
    if (alunos.isEmpty()) {
      throw new ObjectNotFoundException();
    }
    return new ResponseEntity<>(alunos, HttpStatus.OK);
  }

  public ResponseEntity<?> entrar(long id) {
    Aluno aluno = alunoRepositorio.findById(id);
    if (aluno == null) {
      throw new NullIdentifierException();
    }
    Mensalidade mensalidade = aluno.getMensalidade();
    if (!mensalidade.isMensalidadePaga()) {
      throw new PendingPaymentException();
    }
    return new ResponseEntity<>("Entrada autorizada", HttpStatus.OK);
  }

  public ResponseEntity<?> pagar(long id) {
    Aluno aluno = alunoRepositorio.findById(id);
    if (aluno == null) {
      throw new NullIdentifierException();
    }
    Mensalidade mensalidade = aluno.getMensalidade();
    mensalidade.setDias(mensalidade.getDias() + 30);
    mensalidade.setMensalidadePaga(true);
    mensalidadeRepositorio.save(mensalidade);
    return new ResponseEntity<>("Mensalidade paga com sucesso!", HttpStatus.OK);
  }

  public ResponseEntity<?> deletar(long id) {
    Aluno aluno = alunoRepositorio.findById(id);
    if (aluno == null) {
      throw new NullIdentifierException();
    }
    alunoRepositorio.delete(aluno);
    Mensalidade mensalidade = aluno.getMensalidade();
    mensalidadeRepositorio.delete(mensalidade);
    return new ResponseEntity<>("Aluno deletado com sucesso!", HttpStatus.OK);
  }
}
