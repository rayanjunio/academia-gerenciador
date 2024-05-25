package br.com.academia.academia.controle;

import br.com.academia.academia.modelo.Funcionario;
import br.com.academia.academia.servico.FuncionarioServico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioControle {

  @Autowired
  private FuncionarioServico funcionarioServico;

  @PostMapping("/cadastrar-funcionario")
  public ResponseEntity<?> cadastrar(@Valid @RequestBody Funcionario obj) {
    return funcionarioServico.cadastrar(obj);
  }

  @GetMapping("/exibir-funcionarios")
  public ResponseEntity<?> exibirFuncionarios() {
    return funcionarioServico.exibirFuncionarios();
  }

  @GetMapping("/listar-por-nome/{name}")
  public ResponseEntity<?> exibirPorNome(@PathVariable String name) {
    return funcionarioServico.exibirPorNome(name);
  }

  @GetMapping("/entrar-funcionario/{id}")
  public ResponseEntity<?> entrar(@PathVariable long id) {
    return funcionarioServico.entrar(id);
  }

  @PatchMapping("/atualizar-cargo/{id}/{cargo}")
  public ResponseEntity<?> mudarCargo(@PathVariable long id, @PathVariable String cargo) {
    return funcionarioServico.mudarCargo(id, cargo);
  }

  @DeleteMapping("/demitir/{id}")
  public ResponseEntity<?> demitir(@PathVariable long id) {
    return funcionarioServico.demitir(id);
  }
}
