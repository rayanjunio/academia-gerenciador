package br.com.academia.academia.servico;

import br.com.academia.academia.excecoes.NullIdentifierException;
import br.com.academia.academia.excecoes.ObjectNotFoundException;
import br.com.academia.academia.excecoes.PendingPaymentException;
import br.com.academia.academia.modelo.Aluno;
import br.com.academia.academia.modelo.Mensalidade;
import br.com.academia.academia.repositorio.AlunoRepositorio;
import br.com.academia.academia.repositorio.MensalidadeRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoServico {

  @Autowired
  private AlunoRepositorio alunoRepositorio;

  @Autowired
  private MensalidadeRepositorio mensalidadeRepositorio;

  public Aluno cadastrar(Aluno aluno) {
    Mensalidade mensalidade = new Mensalidade();
    mensalidadeRepositorio.save(mensalidade);
    aluno.setMensalidade(mensalidade);
    Aluno alunoCreated = alunoRepositorio.save(aluno);
    return alunoCreated;
  }

  public List<Aluno> exibirTodos() {
    List<Aluno> alunos = alunoRepositorio.findAll();
    return alunos;
  }

  public List<Aluno> exibirPorNome(String name) {
    List<Aluno> alunos = alunoRepositorio.findByNameContaining(name);
    if (alunos.isEmpty()) {
      throw new ObjectNotFoundException();
    }
    return alunos;
  }

  public String entrar(long id) {
    Aluno aluno = alunoRepositorio.findById(id);
    if (aluno == null) {
      throw new NullIdentifierException();
    }
    Mensalidade mensalidade = aluno.getMensalidade();
    if (!mensalidade.isMensalidadePaga()) {
      throw new PendingPaymentException();
    }
    return "Entrada autorizada";
  }

  public String pagar(long id) {
    Aluno aluno = alunoRepositorio.findById(id);
    if (aluno == null) {
      throw new NullIdentifierException();
    }
    Mensalidade mensalidade = aluno.getMensalidade();
    mensalidade.setDias(mensalidade.getDias() + 30);
    mensalidade.setMensalidadePaga(true);
    mensalidadeRepositorio.save(mensalidade);
    return "Mensalidade paga com sucesso!";
  }

  public String deletar(long id) {
    Aluno aluno = alunoRepositorio.findById(id);
    if (aluno == null) {
      throw new NullIdentifierException();
    }
    alunoRepositorio.delete(aluno);
    Mensalidade mensalidade = aluno.getMensalidade();
    mensalidadeRepositorio.delete(mensalidade);
    return "Aluno deletado com sucesso!";
  }
}
