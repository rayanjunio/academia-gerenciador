package br.com.academia.academia.controle;

import br.com.academia.academia.modelo.Aluno;
import br.com.academia.academia.servico.AlunoServico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alunos")
public class AlunoControle {

    @Autowired
    private AlunoServico alunoServico;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody Aluno obj){
        return alunoServico.cadastrar(obj);
    }

    @GetMapping("/exibir")
    public ResponseEntity<?> exibirTodos() {
        return alunoServico.exibirTodos();
    }

    @GetMapping("/exibir-por-nome/{name}")
    public ResponseEntity<?> exibirPorNome(@PathVariable String name) {
        return alunoServico.exibirPorNome(name);
    }

    @GetMapping("/entrar/{id}")
    public ResponseEntity<?> entrar(@PathVariable long id) {
        return alunoServico.entrar(id);
    }

    @PutMapping("/pagar/{id}")
    public ResponseEntity<?> pagar(@PathVariable long id) {
        return alunoServico.pagar(id);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletar(@PathVariable long id) {
        return alunoServico.deletar(id);
    }
}
