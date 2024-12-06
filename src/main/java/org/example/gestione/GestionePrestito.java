package org.example.gestione;

import jakarta.persistence.EntityManager;
import org.example.Prestito;

import java.util.List;

public class GestionePrestito {
    private EntityManager em;

    public GestionePrestito(EntityManager em) {
        this.em = em;
    }

    public List<Prestito> prestitiPerTessera(Long tessera) {
        return em.createQuery("SELECT p FROM Prestito p WHERE p.utente.numeroTessera = :tessera", Prestito.class)
                .setParameter("tessera", tessera)
                .getResultList();
    }

    public List<Prestito> prestitiScaduti() {
        List<Prestito> scaduti = em.createQuery(
                "SELECT p FROM Prestito p WHERE p.dataRestituzioneEffettiva > p.dataRestituzionePrevista ", Prestito.class).getResultList();

        if (scaduti.isEmpty()) {
            System.out.println("Nessun prestito scaduto trovato.");
        }
        return scaduti;
    }
}
