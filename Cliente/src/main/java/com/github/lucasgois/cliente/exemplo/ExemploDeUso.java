package com.github.lucasgois.cliente.exemplo;

import com.github.lucasgois.cliente.socket.ConexaoCliente;
import com.github.lucasgois.core.mensagem.DadoEmail;
import com.github.lucasgois.core.mensagem.DadoUsuario;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExemploDeUso {

    private final DadoUsuario destinatario = new DadoUsuario("destinatario_teste", "123");
    private final DadoUsuario remetente = new DadoUsuario("remetente_teste");

    private final ConexaoCliente conexao = ConexaoCliente.SINGLETON;


    public void enviarEmail() {
        final DadoEmail email = new DadoEmail();

        email.setRemetente(remetente.getUsuario());
        email.setDestinatario(destinatario.getUsuario());
        email.setAssunto("Assunto do email");
        email.setTexto("Texto do email");

        ConexaoCliente.SINGLETON.enviarEmail(email);
    }

    public void buscarEmail() {
        ConexaoCliente.SINGLETON.buscarEmails(destinatario);
    }

}
