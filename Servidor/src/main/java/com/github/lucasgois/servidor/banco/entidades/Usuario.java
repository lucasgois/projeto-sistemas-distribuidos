package com.github.lucasgois.servidor.banco.entidades;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "usuarios")
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nome;

    private String senha;

    @OneToMany(mappedBy = "destinatario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = Email.class)
    private List<Email> emails = new ArrayList<>(8);

}
