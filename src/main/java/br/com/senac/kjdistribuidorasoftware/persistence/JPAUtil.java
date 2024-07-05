package br.com.senac.kjdistribuidorasoftware.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final String PERSISTENCE_UNIT = "Kj-PU";

    private static EntityManager em;
    private static EntityManagerFactory fabrica;

    public static EntityManager getEntityManager() {
        try {
            if (fabrica == null || !fabrica.isOpen()) {
                fabrica = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            }

            if (em == null || !em.isOpen()) {
                em = fabrica.createEntityManager();
            }

            return em;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar o EntityManager", e);
        }
    }

    public static void closeEntityManager() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (fabrica != null && fabrica.isOpen()) {
            fabrica.close();
        }
    }
}


