package com.github.lucasgois.cliente.exemplo;

import com.github.lucasgois.cliente.socket.ConexaoCliente;
import com.github.lucasgois.core.mensagem.DadoEmail;
import com.github.lucasgois.core.mensagem.DadoLogin;
import com.github.lucasgois.core.mensagem.DadoUsuario;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExemploDeUso {

    private final DadoUsuario destinatario = new DadoUsuario("destinatario_teste", "123");
    private final DadoUsuario remetente = new DadoUsuario("remetente_teste");

    private final ConexaoCliente conexao = new ConexaoCliente();

    public void login() {
        conexao.conectar(new DadoLogin("lucas", "123"));
    }

    public void enviarEmail() {
        final DadoEmail email = new DadoEmail();

        email.setRemetente(remetente.getUsuario());
        email.setDestinatario(destinatario.getUsuario());
        email.setAssunto("Assunto do email");
        email.setTexto("Texto do email");

        conexao.enviarEmail(email);
    }

    public void buscarEmail() {
        conexao.buscarEmails(destinatario);
    }

}
