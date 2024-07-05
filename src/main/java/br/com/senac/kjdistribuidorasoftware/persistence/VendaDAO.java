package br.com.senac.kjdistribuidorasoftware.persistence;

import br.com.senac.kjdistribuidorasoftware.model.Cliente;
import br.com.senac.kjdistribuidorasoftware.model.Venda;
import br.com.senac.kjdistribuidorasoftware.model.Vendedor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class VendaDAO {

    public void salvar(Venda venda) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            em.persist(venda);

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro " + e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    public void excluir(int id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Venda venda = em.find(Venda.class, id);
            if (venda != null) {

                em.remove(venda);
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

    public Venda obter(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT v FROM Venda v LEFT JOIN FETCH v.cliente LEFT JOIN FETCH v.vendedor LEFT JOIN FETCH v.produtos WHERE v.id = :id";
            TypedQuery<Venda> query = em.createQuery(jpql, Venda.class);
            query.setParameter("id", id);
            return (Venda) query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            JPAUtil.closeEntityManager();
        }
    }

    public void atualizar(Venda venda) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            if (venda.getCliente() != null) {
                Cliente cliente = venda.getCliente();
                if (venda.getId() == 0) {
                    em.persist(cliente);
                } else {
                    em.merge(cliente);
                }
            }

            if (venda.getVendedor() != null) {
                Vendedor vendedor = venda.getVendedor();
                if (venda.getId() == 0) {
                    em.persist(vendedor);
                } else {
                    em.merge(vendedor);
                }
            }

            em.merge(venda);

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
    }

    public List<Venda> listarVendas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Venda> query = em.createQuery("SELECT v FROM Venda v ORDER BY v.id DESC", Venda.class);
            return query.getResultList();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Erro " + e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }
}
