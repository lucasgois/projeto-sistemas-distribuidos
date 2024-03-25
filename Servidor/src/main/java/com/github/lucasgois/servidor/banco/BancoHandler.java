package com.github.lucasgois.servidor.banco;

import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import com.github.lucasgois.core.mensagem.DadoEmail;
import com.github.lucasgois.core.mensagem.DadoLogin;
import com.github.lucasgois.servidor.banco.entidades.Email;
import com.github.lucasgois.servidor.banco.entidades.Usuario;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Log4j2
@UtilityClass
public class BancoHandler {

    private final Set<UUID> UUID_SET = new HashSet<>(8);

    public void email(@NotNull final DadoEmail email) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            final Usuario remetente = buscarUsuario(session, email.getRemetente());
            final Usuario destinatario = buscarUsuario(session, email.getDestinatario());

            if (destinatario == null) {
                throw new ErroRuntimeException("Destinatario nao encontrado: " + email.getDestinatario());
            }

            final Email entidade = new Email();
            entidade.setAssunto(email.getAssunto());
            entidade.setConteudo(email.getTexto());
            entidade.setDestinatario(destinatario);
            entidade.setRemetente(remetente);

            session.persist(entidade);
            session.getTransaction().commit();
        }
    }

    private Usuario buscarUsuario(@NotNull final Session session, final String nome) {
        return session.createQuery("FROM Usuario WHERE nome = :nome", Usuario.class)
                .setParameter("nome", nome)
                .uniqueResult();
    }

    public List<DadoEmail> buscarEmails(@NotNull final String usuario) {
        final Usuario usuarioEncontrado;

        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            usuarioEncontrado = buscarUsuario(session, usuario);

            if (usuarioEncontrado == null) {
                throw new ErroRuntimeException("Usuário não encontrado: " + usuario);
            }

            final List<DadoEmail> dadoEmails = new ArrayList<>(8);

            for (final Email email : usuarioEncontrado.getEmails()) {
                final DadoEmail dadoEmail = new DadoEmail();
                dadoEmail.setRemetente(email.getRemetente().getNome());
                dadoEmail.setDestinatario(email.getDestinatario().getNome());
                dadoEmail.setAssunto(email.getAssunto());
                dadoEmail.setTexto(email.getConteudo());
                dadoEmails.add(dadoEmail);
            }

            return dadoEmails;
        }

    }

    public UUID login(@NotNull final DadoLogin login) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            final Usuario usuarioEncontrado = buscarUsuario(session, login.getNome());

            if (usuarioEncontrado == null) {
                session.beginTransaction();
                final Usuario novoUsuario = new Usuario();
                novoUsuario.setNome(login.getNome());
                novoUsuario.setSenha(login.getSenha());
                session.persist(novoUsuario);
                session.getTransaction().commit();
                log.info("Usuário criado com sucesso: {}", novoUsuario);
                return getUuid();
            } else {
                if (Objects.equals(usuarioEncontrado.getSenha(), login.getSenha())) {
                    log.info("Login efetuado com sucesso: {}", usuarioEncontrado.getNome());
                    return getUuid();

                } else {
                    throw new ErroRuntimeException("Usuário ou senha inválidos");
                }
            }
        }
    }

    private @NotNull UUID getUuid() {
        final UUID uuid = UUID.randomUUID();
        UUID_SET.add(uuid);
        return uuid;
    }

}
