package br.com.senac.kjdistribuidorasoftware.persistence;

import br.com.senac.kjdistribuidorasoftware.model.Contato;
import br.com.senac.kjdistribuidorasoftware.model.Vendedor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class VendedorDAO {

    public void cadastrar(Vendedor vendedor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            if (vendedor.getContato() != null) {
                em.persist(vendedor.getContato());
            }
            em.persist(vendedor);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro " + e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    public static void excluir(int id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Vendedor vendedor = em.find(Vendedor.class, id);
            if (vendedor != null) {
                if (vendedor.getContato() != null) {
                    em.remove(vendedor.getContato());
                }

                em.remove(vendedor);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
    }

    public static List<Vendedor> listar() {
        EntityManager em = JPAUtil.getEntityManager();

        List<Vendedor> listaVendedores = new ArrayList<>();
        try {
            Query consulta = em.createQuery("SELECT v FROM Vendedor v LEFT JOIN FETCH v.contato");
            listaVendedores = consulta.getResultList();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
        return listaVendedores;
    }

    public Vendedor obter(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT v FROM Vendedor v LEFT JOIN FETCH v.contato WHERE v.id = :id";
            Query query = em.createQuery(jpql);
            query.setParameter("id", id);
            return (Vendedor) query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            JPAUtil.closeEntityManager();
        }
    }

   public void atualizar(Vendedor vendedor) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
        em.getTransaction().begin();

        if (vendedor.getContato() != null) {
            Contato contato = vendedor.getContato();
            if (contato.getId() == 0) {
                em.persist(contato);
            } else {
                em.merge(contato);
            }
        }

        em.merge(vendedor);

        em.getTransaction().commit();
    } catch (Exception e) {
        em.getTransaction().rollback();
        e.printStackTrace();
        throw e;
    } finally {
        JPAUtil.closeEntityManager();
    }
}



    public static List<Vendedor> buscaVendedor(String parametro, String termoBusca) {
        EntityManager em = JPAUtil.getEntityManager();

        List<Vendedor> listaVendedores = new ArrayList<>();
        try {
            String jpql = "SELECT v FROM Vendedor v LEFT JOIN FETCH v.contato WHERE v." + parametro + " LIKE :termoBusca";
            Query consulta = em.createQuery(jpql);
            consulta.setParameter("termoBusca", "%" + termoBusca + "%");

            listaVendedores = consulta.getResultList();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
        return listaVendedores;
    }
}
