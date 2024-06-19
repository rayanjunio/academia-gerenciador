package br.com.academia.academia.servico;

import br.com.academia.academia.excecoes.InvalidInformationException;
import br.com.academia.academia.excecoes.NullIdentifierException;
import br.com.academia.academia.modelo.Funcionario;
import br.com.academia.academia.modelo.MensalidadeFuncionario;
import br.com.academia.academia.repositorio.FuncionarioRepositorio;
import br.com.academia.academia.repositorio.MensalidadeFuncionarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioServico {

  @Autowired
  private FuncionarioRepositorio funcionarioRepositorio;

  @Autowired
  private MensalidadeFuncionarioRepositorio mensalidadeRepositorio;

  public Funcionario cadastrar(Funcionario funcionario) {
    if (funcionario.getName().isEmpty() || funcionario.getCargo().isEmpty() || funcionario.getCpf().isEmpty()) {
      throw new InvalidInformationException("Check the employee data");
    }
    MensalidadeFuncionario mensalidade = new MensalidadeFuncionario();
    mensalidadeRepositorio.save(mensalidade);
    funcionario.setMensalidadeFuncionario(mensalidade);
    Funcionario funcionarioCreated = funcionarioRepositorio.save(funcionario);
    return funcionarioCreated;
  }

  public List<Funcionario> exibirFuncionarios() {
    List<Funcionario> funcionarios = funcionarioRepositorio.findAll();
    return funcionarios;
  }

  public List<Funcionario> exibirPorNome(String name) {
    List<Funcionario> funcionarios = funcionarioRepositorio.findByNameContaining(name);
    if (funcionarios.isEmpty()) {
      throw new NullIdentifierException("There is not employees with this name");
    }
    return funcionarios;
  }

  public String entrar(long id) {
    Funcionario funcionario = funcionarioRepositorio.findById(id);
    if (funcionario == null) {
      throw new NullIdentifierException();
    }
    return "Entrada autorizada";
  }

  public Funcionario mudarCargo(long id, String cargo) {
    Funcionario funcionario = funcionarioRepositorio.findById(id);
    if (funcionario == null) {
      throw new NullIdentifierException();
    }
    funcionario.setCargo(cargo);
    funcionarioRepositorio.save(funcionario);
    return funcionario;
  }

  public String demitir(long id) {
    Funcionario funcionario = funcionarioRepositorio.findById(id);
    if (funcionario == null) {
      throw new NullIdentifierException();
    }
    funcionarioRepositorio.delete(funcionario);
    MensalidadeFuncionario mensalidade = funcionario.getMensalidadeFuncionario();
    mensalidadeRepositorio.delete(mensalidade);
    return "Funcionário demitido com sucesso!";
  }
}

