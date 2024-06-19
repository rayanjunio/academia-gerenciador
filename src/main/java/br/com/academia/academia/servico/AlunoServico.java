package br.com.academia.academia.servico;

import br.com.academia.academia.excecoes.InvalidInformationException;
import br.com.academia.academia.excecoes.NullIdentifierException;
import br.com.academia.academia.excecoes.PendingPaymentException;
import br.com.academia.academia.modelo.Aluno;
import br.com.academia.academia.modelo.Mensalidade;
import br.com.academia.academia.repositorio.AlunoRepositorio;
import br.com.academia.academia.repositorio.MensalidadeRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AlunoServico {

  @Autowired
  private AlunoRepositorio alunoRepositorio;

  @Autowired
  private MensalidadeRepositorio mensalidadeRepositorio;

  public Aluno cadastrar(Aluno aluno) {
    if (aluno.getName().isEmpty() || aluno.getCpf().isBlank() || aluno.getDataNascimento().isAfter(LocalDate.now())) {
      throw new InvalidInformationException("Check the gym member data!");
    }
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
      throw new NullIdentifierException("There is not gym members with those characters");
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
