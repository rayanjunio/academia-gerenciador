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

@Entity
@Table(name = "funcionarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @NotNull
    @Column
    private String name;

    @NotNull
    @NotBlank
    @Column(columnDefinition = "VARCHAR(70) COLLATE latin1_general_ci")
    private String cargo;

    @CPF(message = "CPF inválido!")
    @Column(nullable = false, unique = true)
    private String cpf;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "mensalidades_funcionarios")
    @JsonIgnore
    private MensalidadeFuncionario mensalidadeFuncionario;
}
