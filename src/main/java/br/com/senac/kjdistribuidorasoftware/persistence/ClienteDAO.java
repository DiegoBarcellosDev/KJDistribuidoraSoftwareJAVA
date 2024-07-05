package br.com.senac.kjdistribuidorasoftware.persistence;

import br.com.senac.kjdistribuidorasoftware.model.Cliente;
import br.com.senac.kjdistribuidorasoftware.model.Contato;
import br.com.senac.kjdistribuidorasoftware.model.Endereco;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void cadastrar(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            if (cliente.getContato() != null) {
                em.persist(cliente.getContato());
            }
            if (cliente.getEndereco() != null) {
                em.persist(cliente.getEndereco());
            }
            em.persist(cliente);
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

            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                if (cliente.getContato() != null) {
                    em.remove(cliente.getContato());
                }
                if (cliente.getEndereco() != null) {
                    em.remove(cliente.getEndereco());
                }
                em.remove(cliente);
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

    public static List<Cliente> listar() {
        EntityManager em = JPAUtil.getEntityManager();

        List<Cliente> listaClientes = new ArrayList<>();
        try {
            Query consulta = em.createQuery("SELECT c FROM Cliente c LEFT JOIN FETCH c.contato LEFT JOIN FETCH c.endereco");
            listaClientes = consulta.getResultList();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
        return listaClientes;
    }

    public Cliente obter(int id) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
        String jpql = "SELECT c FROM Cliente c LEFT JOIN FETCH c.contato LEFT JOIN FETCH c.endereco WHERE c.id = :id";
        Query query = em.createQuery(jpql);
        query.setParameter("id", id);
        return (Cliente) query.getSingleResult();
    } catch (Exception e) {
        return null;
    } finally {
        JPAUtil.closeEntityManager();
    }
}
    
    public void atualizar(Cliente cliente) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
        em.getTransaction().begin();

        if (cliente.getContato() != null) {
            Contato contato = cliente.getContato();
            if (contato.getId() == 0) {
                em.persist(contato);
            } else {
                em.merge(contato);
            }
        }

        if (cliente.getEndereco() != null) {
            Endereco endereco = cliente.getEndereco();
            if (endereco.getId() == 0) {
                em.persist(endereco);
            } else {
                em.merge(endereco);
            }
        }

        em.merge(cliente);

        em.getTransaction().commit();
    } catch (Exception e) {
        em.getTransaction().rollback();
        throw e;
    } finally {
        JPAUtil.closeEntityManager();
    }
}
         

    public static List<Cliente> buscaNome(String nome) {
        EntityManager em = JPAUtil.getEntityManager();

        List<Cliente> listaClientes = new ArrayList<>();
        try {
            Query consulta = em.createQuery("SELECT c FROM Cliente c LEFT JOIN FETCH c.contato LEFT JOIN FETCH c.endereco WHERE c.nome LIKE :nome");
            consulta.setParameter("nome", "%" + nome + "%");
            listaClientes = consulta.getResultList();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
        return listaClientes;
    }
}
