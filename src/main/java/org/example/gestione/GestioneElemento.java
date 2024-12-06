package org.example.gestione;

import jakarta.persistence.EntityManager;
import org.example.Elemento;

import java.util.List;

public class GestioneElemento {
    private EntityManager em;

    public GestioneElemento(EntityManager em) {
        this.em = em;
    }

    public void aggiungiElemento(Elemento elemento) {
        try {
            em.getTransaction().begin();
            em.persist(elemento);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Errore nell'aggiunta dell'elemento: " + e.getMessage());
        }
    }

    public void rimuoviElemento(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN non può essere nullo o vuoto.");
        }
        em.getTransaction().begin();
        Elemento elemento = em.find(Elemento.class, isbn);
        if (elemento != null) {
            em.remove(elemento);
        } else {
            System.out.println("Elemento non trovato con ISBN: " + isbn);
        }
        em.getTransaction().commit();
    }

    public Elemento ricercaPerISBN(String isbn) {
        return em.find(Elemento.class, isbn);
    }

    public List<Elemento> ricercaPerAnno(int anno) {
        return em.createQuery("SELECT e FROM Elemento e WHERE e.annoPubblicazione = :anno", Elemento.class)
                .setParameter("anno", anno)
                .getResultList();
    }

    public List<Elemento> ricercaPerTitolo(String titolo) {
        return em.createQuery("SELECT e FROM Elemento e WHERE e.titolo LIKE :titolo", Elemento.class)
                .setParameter("titolo", "%" + titolo + "%")
                .getResultList();
    }
    public List<Elemento> ricercaPerAutore(String autore) {
        if (autore == null || autore.isEmpty()) {
            throw new IllegalArgumentException("L'autore non può essere nullo o vuoto.");
        }

        return em.createQuery("SELECT e FROM Elemento e WHERE e.autore LIKE :autore", Elemento.class)
                .setParameter("autore", "%" + autore + "%")
                .getResultList();
    }
}
