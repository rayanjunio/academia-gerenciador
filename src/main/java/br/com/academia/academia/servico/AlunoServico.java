package br.com.academia.academia.servico;

import br.com.academia.academia.excecoes.NullIdentifierException;
import br.com.academia.academia.excecoes.NullRegisterException;
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

  public ResponseEntity<?> cadastrar(Aluno obj) {
    Mensalidade mensalidade = new Mensalidade();
    mensalidadeRepositorio.save(mensalidade);
    obj.setMensalidade(mensalidade);
    return new ResponseEntity<>(alunoRepositorio.save(obj), HttpStatus.CREATED);
  }

  public ResponseEntity<?> exibirTodos() {
    if (alunoRepositorio.findAll().isEmpty()) {
      throw new NullRegisterException();
    }
    return new ResponseEntity<>(alunoRepositorio.findAll(), HttpStatus.OK);
  }

  public ResponseEntity<?> exibirPorNome(String name) {
    List<Aluno> alunos = alunoRepositorio.findByNameContaining(name);
    if (alunos.isEmpty()) {
      throw new ObjectNotFoundException();
    }
    return new ResponseEntity<>(alunos, HttpStatus.OK);
  }

  public ResponseEntity<?> entrar(long id) {
    if (alunoRepositorio.findById(id) == null) {
      throw new NullIdentifierException();
    }
    Aluno obj = alunoRepositorio.findById(id);
    Mensalidade mensalidade = obj.getMensalidade();
    if (!mensalidade.isMensalidadePaga()) {
      throw new PendingPaymentException();
    }
    return new ResponseEntity<>("Entrada autorizada", HttpStatus.OK);
  }

  public ResponseEntity<?> pagar(long id) {
    if (alunoRepositorio.findById(id) == null) {
      throw new NullIdentifierException();
    }
    Aluno obj = alunoRepositorio.findById(id);
    Mensalidade mensalidade = obj.getMensalidade();
    mensalidade.setDias(mensalidade.getDias() + 30);
    mensalidade.setMensalidadePaga(true);
    mensalidadeRepositorio.save(mensalidade);
    return new ResponseEntity<>("Mensalidade paga com sucesso!", HttpStatus.OK);
  }

  public ResponseEntity<?> deletar(long id) {
    if (alunoRepositorio.findById(id) == null) {
      throw new NullIdentifierException();
    }
    Aluno obj = alunoRepositorio.findById(id);
    alunoRepositorio.delete(obj);
    Mensalidade mensalidade = obj.getMensalidade();
    mensalidadeRepositorio.delete(mensalidade);
    return new ResponseEntity<>("Aluno deletado com sucesso!", HttpStatus.OK);
  }
}
