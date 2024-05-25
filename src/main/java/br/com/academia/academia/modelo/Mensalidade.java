package br.com.academia.academia.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mensalidades")
@Getter
@Setter
@AllArgsConstructor
public class Mensalidade {

    public Mensalidade() {
        this.mensalidadePaga = true;
        this.dias = 7;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private final int VALOR = 90;

    private boolean mensalidadePaga;

    private int dias;

    @OneToMany(mappedBy = "mensalidade")
    private List<Aluno> alunos = new ArrayList<>();
}