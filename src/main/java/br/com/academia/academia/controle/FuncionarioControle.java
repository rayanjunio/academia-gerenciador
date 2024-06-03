package br.com.academia.academia.controle;

import br.com.academia.academia.modelo.Funcionario;
import br.com.academia.academia.servico.FuncionarioServico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioControle {

  @Autowired
  private FuncionarioServico funcionarioServico;

  @PostMapping("/cadastrar-funcionario")
  public ResponseEntity<?> cadastrar(@Valid @RequestBody Funcionario obj) {
    return new ResponseEntity<>(funcionarioServico.cadastrar(obj), HttpStatus.CREATED);
  }

  @GetMapping("/exibir-funcionarios")
  public ResponseEntity<?> exibirFuncionarios() {
    return new ResponseEntity<>(funcionarioServico.exibirFuncionarios(), HttpStatus.OK);
  }

  @GetMapping("/listar-por-nome/{name}")
  public ResponseEntity<?> exibirPorNome(@PathVariable String name) {
    return new ResponseEntity<>(funcionarioServico.exibirPorNome(name), HttpStatus.OK);
  }

  @GetMapping("/entrar-funcionario/{id}")
  public ResponseEntity<?> entrar(@PathVariable long id) {
    return new ResponseEntity<>(funcionarioServico.entrar(id), HttpStatus.OK);
  }

  @PatchMapping("/atualizar-cargo/{id}/{cargo}")
  public ResponseEntity<?> mudarCargo(@PathVariable long id, @PathVariable String cargo) {
    return new ResponseEntity<>(funcionarioServico.mudarCargo(id, cargo), HttpStatus.OK);
  }

  @DeleteMapping("/demitir/{id}")
  public ResponseEntity<?> demitir(@PathVariable long id) {
    return new ResponseEntity<>(funcionarioServico.demitir(id), HttpStatus.OK);
  }
}
