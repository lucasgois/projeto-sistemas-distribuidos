package com.github.lucasgois.servidor.banco;

import com.github.lucasgois.core.exceptions.DestinatarioNaoExisteRuntimeException;
import com.github.lucasgois.core.exceptions.ErroRuntimeException;
import com.github.lucasgois.core.mensagem.DadoEmail;
import com.github.lucasgois.core.mensagem.DadoLogin;
import com.github.lucasgois.servidor.banco.entidades.Email;
import com.github.lucasgois.servidor.banco.entidades.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
@UtilityClass
public class BancoHandler {

    public void email(@NotNull final DadoEmail email) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            final Usuario remetente = buscarUsuario(session, email.getRemetente());
            final Usuario destinatario = buscarUsuario(session, email.getDestinatario());

            if (destinatario == null) {
                throw new DestinatarioNaoExisteRuntimeException();
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

    private @Nullable Usuario buscarUsuario(@NotNull final EntityManager session, final String nome) {
        try {
            return session.createQuery("FROM Usuario WHERE nome = :nome", Usuario.class)
                    .setParameter("nome", nome)
                    .getSingleResult();

        } catch (final NoResultException ex) {
            return null;
        }
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
                dadoEmail.setId(email.getId());
                dadoEmail.setRemetente(email.getRemetente().getNome());
                dadoEmail.setDestinatario(email.getDestinatario().getNome());
                dadoEmail.setAssunto(email.getAssunto());
                dadoEmail.setTexto(email.getConteudo());
                dadoEmails.add(dadoEmail);
            }

            return dadoEmails;
        }

    }

    public void login(@NotNull final DadoLogin login) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            final Usuario usuarioEncontrado = buscarUsuario(session, login.getNome());

            if (usuarioEncontrado == null) {
                session.beginTransaction();
                cadastrarUsuario(session, login.getNome(), login.getSenha());
                session.getTransaction().commit();

            } else {
                if (Objects.equals(usuarioEncontrado.getSenha(), login.getSenha())) {
                    log.info("Login efetuado com sucesso: {}", usuarioEncontrado.getNome());

                } else if (usuarioEncontrado.getSenha() == null) {
                    usuarioEncontrado.setSenha(login.getSenha());
                    session.persist(usuarioEncontrado);

                } else {
                    throw new ErroRuntimeException("Usuário ou senha inválidos");
                }
            }
        }
    }

    private @NotNull Usuario cadastrarUsuario(@NotNull final Session session, @NotNull final String usuario, @Nullable final String senha) {
        final Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuario);
        novoUsuario.setSenha(senha);
        session.persist(novoUsuario);
        log.info("Usuário criado com sucesso: {}", novoUsuario);
        return novoUsuario;
    }

    public void excluirEmail(final int id) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            final Email email = session.get(Email.class, id);
            session.remove(email);

            session.getTransaction().commit();
        }
    }

}
