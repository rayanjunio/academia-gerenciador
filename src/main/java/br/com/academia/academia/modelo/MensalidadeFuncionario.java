package br.com.academia.academia.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mensalidadesFuncionarios")
@Getter
public class MensalidadeFuncionario {

  public MensalidadeFuncionario() {
    this.mensalidadePaga = true;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private boolean mensalidadePaga;

  @OneToMany(mappedBy = "mensalidadeFuncionario")
  private List<Funcionario> funcionarios = new ArrayList<>();
}
