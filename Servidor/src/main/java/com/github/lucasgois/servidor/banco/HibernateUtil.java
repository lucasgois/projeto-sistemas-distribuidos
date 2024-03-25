package com.github.lucasgois.servidor.banco;

import com.github.lucasgois.servidor.banco.entidades.Anexos;
import com.github.lucasgois.servidor.banco.entidades.Email;
import com.github.lucasgois.servidor.banco.entidades.Usuario;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@UtilityClass
public class HibernateUtil {

    private StandardServiceRegistry registry = null;
    private SessionFactory sessionFactory = getSessionFactory();

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                final StandardServiceRegistryBuilder registryBuilder = criarBuilder();
                registry = registryBuilder.build();

                final MetadataSources sources = new MetadataSources(registry)
                        .addAnnotatedClass(Email.class)
                        .addAnnotatedClass(Usuario.class)
                        .addAnnotatedClass(Anexos.class);

                final Metadata metadata = sources.getMetadataBuilder().build();

                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (final Exception e) {
                log.error("Erro ao iniciar Hibernate", e);

                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    private @NotNull StandardServiceRegistryBuilder criarBuilder() {
        final StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

        final Map<String, Object> settings = new HashMap<>(6);
        settings.put(JdbcSettings.JAKARTA_JDBC_DRIVER, "org.h2.Driver");
        settings.put(JdbcSettings.JAKARTA_JDBC_URL, "jdbc:h2:./servidor.db;DB_CLOSE_ON_EXIT=FALSE");
        settings.put(JdbcSettings.DIALECT, "org.hibernate.dialect.H2Dialect");
        settings.put(JdbcSettings.SHOW_SQL, true);
        settings.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        settings.put(SchemaToolingSettings.HBM2DDL_AUTO, "update");

        registryBuilder.applySettings(settings);
        return registryBuilder;
    }

    public void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

}
