package com.github.lucasgois.servidor.banco.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "anexos")
@Entity
public class Anexos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private byte[] conteudo;

    @ManyToOne
    @JoinColumn(name = "email_id", referencedColumnName = "id")
    private Email email;

}
