package br.com.academia.academia.servico;

import br.com.academia.academia.excecoes.NullIdentifierException;
import br.com.academia.academia.excecoes.NullRegisterException;
import br.com.academia.academia.excecoes.ObjectNotFoundException;
import br.com.academia.academia.excecoes.PendingPaymentException;
import br.com.academia.academia.modelo.Funcionario;
import br.com.academia.academia.modelo.MensalidadeFuncionario;
import br.com.academia.academia.repositorio.FuncionarioRepositorio;
import br.com.academia.academia.repositorio.MensalidadeFuncionarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioServico {

  @Autowired
  private FuncionarioRepositorio funcionarioRepositorio;

  @Autowired
  private MensalidadeFuncionarioRepositorio mensalidadeRepositorio;

  public ResponseEntity<?> cadastrar(Funcionario obj) {
    MensalidadeFuncionario mensalidade = new MensalidadeFuncionario();
    mensalidadeRepositorio.save(mensalidade);
    obj.setMensalidadeFuncionario(mensalidade);
    return new ResponseEntity<>(funcionarioRepositorio.save(obj), HttpStatus.CREATED);
  }

  public ResponseEntity<?> exibirFuncionarios() {
    if (funcionarioRepositorio.findAll().isEmpty()) {
      throw new NullRegisterException();
    }
    return new ResponseEntity<>(funcionarioRepositorio.findAll(), HttpStatus.OK);
  }

  public ResponseEntity<?> exibirPorNome(String name) {
    List<Funcionario> funcionarios = funcionarioRepositorio.findByNameContaining(name);
    if (funcionarios.isEmpty()) {
      throw new ObjectNotFoundException();
    }
    return new ResponseEntity<>(funcionarios, HttpStatus.OK);
  }

  public ResponseEntity<?> entrar(long id) {
    if (funcionarioRepositorio.findById(id) == null) {
      throw new NullIdentifierException();
    }
    Funcionario obj = funcionarioRepositorio.findById(id);
    MensalidadeFuncionario mensalidadeFuncionario = obj.getMensalidadeFuncionario();
    if (!mensalidadeFuncionario.isMensalidadePaga()) {
      throw new PendingPaymentException();
    }
    return new ResponseEntity<>("Entrada autorizada", HttpStatus.OK);
  }

  public ResponseEntity<?> mudarCargo(long id, String cargo) {
    if (funcionarioRepositorio.findById(id) == null) {
      throw new NullIdentifierException();
    }
    Funcionario obj = funcionarioRepositorio.findById(id);
    obj.setCargo(cargo);
    return new ResponseEntity<>(funcionarioRepositorio.save(obj), HttpStatus.OK);
  }

  public ResponseEntity<?> demitir(long id) {
    if (funcionarioRepositorio.findById(id) == null) {
      throw new NullIdentifierException();
    }
    Funcionario obj = funcionarioRepositorio.findById(id);
    funcionarioRepositorio.delete(obj);
    MensalidadeFuncionario mensalidade = obj.getMensalidadeFuncionario();
    mensalidadeRepositorio.delete(mensalidade);
    return new ResponseEntity<>("Funcionário demitido com sucesso!", HttpStatus.OK);
  }
}

