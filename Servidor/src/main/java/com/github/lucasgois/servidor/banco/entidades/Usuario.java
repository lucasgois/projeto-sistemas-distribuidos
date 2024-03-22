package com.github.lucasgois.servidor.banco.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "usuarios")
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;

    private byte[] senha;

}
