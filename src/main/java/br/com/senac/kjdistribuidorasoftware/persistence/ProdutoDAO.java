
package br.com.senac.kjdistribuidorasoftware.persistence;

import br.com.senac.kjdistribuidorasoftware.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    public void cadastrar(Produto produto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(produto);
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

            Produto produto = em.find(Produto.class, id);
            if (produto != null) {
                em.remove(produto);
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

    public static List<Produto> listar() {
        EntityManager em = JPAUtil.getEntityManager();

        List<Produto> listaProdutos = new ArrayList<>();
        try {
            Query consulta = em.createQuery("SELECT p FROM Produto p");
            listaProdutos = consulta.getResultList();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
        return listaProdutos;
    }

    public Produto obter(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT p FROM Produto p WHERE p.id = :id";
            Query query = em.createQuery(jpql);
            query.setParameter("id", id);
            return (Produto) query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            JPAUtil.closeEntityManager();
        }
    }

    public void atualizar(Produto produto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(produto);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
    }

    public static List<Produto> buscaProdutos(String parametro, String termoBusca) {
        EntityManager em = JPAUtil.getEntityManager();

        List<Produto> listaProdutos = new ArrayList<>();
        try {
            String jpql = "SELECT p FROM Produto p WHERE p." + parametro + " LIKE :termoBusca";
            Query consulta = em.createQuery(jpql);
            consulta.setParameter("termoBusca", "%" + termoBusca + "%");

            listaProdutos = consulta.getResultList();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
        return listaProdutos;
    }
}
