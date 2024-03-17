package com.github.lucasgois.core.exceptions;

public class SemConexaoComServidorException extends Exception {

    public SemConexaoComServidorException() {
        super("Não há conexão com o servidor");
    }

}
