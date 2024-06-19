package br.com.academia.academia.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "alunos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {

  public Aluno(String cpf, String name, LocalDate dataNascimento) {
    this.cpf = cpf;
    this.name = name;
    this.dataNascimento = dataNascimento;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @CPF
  @Column(nullable = false, unique = true)
  private String cpf;

  @NotNull
  @NotBlank
  @Column(columnDefinition = "VARCHAR(70) COLLATE latin1_general_ci")
  private String name;

  @DateTimeFormat
  @NotNull
  private LocalDate dataNascimento;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "mensalidade")
  @JsonIgnore
  private Mensalidade mensalidade;
}
