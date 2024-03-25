package com.github.lucasgois.servidor.banco;

import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import com.github.lucasgois.core.mensagem.DadoEmail;
import com.github.lucasgois.core.mensagem.DadoLogin;
import com.github.lucasgois.core.mensagem.DadoUsuario;
import com.github.lucasgois.servidor.banco.entidades.Email;
import com.github.lucasgois.servidor.banco.entidades.Usuario;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Hibernate;
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

            final Usuario remetente = buscarUusuario(session, email.getRemetente());
            final Usuario destinatario = buscarUusuario(session, email.getDestinatario());

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

    public Usuario buscarUusuario(final Session session, final String nome) {
        return session.createQuery("FROM Usuario WHERE nome = :nome", Usuario.class)
                .setParameter("nome", nome)
                .uniqueResult();
    }

    public List<Email> buscarEmails(@NotNull final DadoUsuario usuario) {
        final Usuario usuarioEncontrado;

        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            usuarioEncontrado = buscarUusuario(session, usuario.getUsuario());

            if (usuarioEncontrado == null) {
                throw new ErroRuntimeException("Usuário não encontrado: " + usuario.getUsuario());
            }

            Hibernate.initialize(usuarioEncontrado.getEmails());

            return usuarioEncontrado.getEmails();
        }

    }

    public UUID login(@NotNull final DadoLogin login) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            final Usuario usuarioEncontrado = buscarUusuario(session, login.getNome());

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
                    log.info("Login efetuado com sucesso: {}", usuarioEncontrado);
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
