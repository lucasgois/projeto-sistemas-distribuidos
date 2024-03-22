package com.github.lucasgois.servidor.banco.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "emails")
@Entity
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String assunto;

    @ManyToOne
    @JoinColumn(name = "remetente_id", referencedColumnName = "id")
    private Usuario remetente;

    @ManyToOne
    private Usuario destinatario;

    private String conteudo;

}
