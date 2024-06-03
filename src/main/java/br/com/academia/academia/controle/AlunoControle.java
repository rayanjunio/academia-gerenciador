package br.com.academia.academia.controle;

import br.com.academia.academia.modelo.Aluno;
import br.com.academia.academia.servico.AlunoServico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
public class AlunoControle {

  @Autowired
  private AlunoServico alunoServico;

  @PostMapping("/cadastrar")
  public ResponseEntity<?> cadastrar(@Valid @RequestBody Aluno obj) {
    return new ResponseEntity<>(alunoServico.cadastrar(obj), HttpStatus.CREATED);
  }

  @GetMapping("/exibir")
  public ResponseEntity<?> exibirTodos() {
    return new ResponseEntity<>(alunoServico.exibirTodos(), HttpStatus.OK);
  }

  @GetMapping("/exibir-por-nome/{name}")
  public ResponseEntity<?> exibirPorNome(@PathVariable String name) {
    return new ResponseEntity<>(alunoServico.exibirPorNome(name), HttpStatus.OK);
  }

  @GetMapping("/entrar/{id}")
  public ResponseEntity<?> entrar(@PathVariable long id) {
    return new ResponseEntity<>(alunoServico.entrar(id), HttpStatus.ACCEPTED);
  }

  @PutMapping("/pagar/{id}")
  public ResponseEntity<?> pagar(@PathVariable long id) {
    return new ResponseEntity<>(alunoServico.pagar(id), HttpStatus.OK);
  }

  @DeleteMapping("/deletar/{id}")
  public ResponseEntity<?> deletar(@PathVariable long id) {
    return new ResponseEntity<>(alunoServico.deletar(id), HttpStatus.OK);
  }
}
