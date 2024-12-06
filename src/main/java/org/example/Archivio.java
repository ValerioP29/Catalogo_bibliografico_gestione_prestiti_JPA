package org.example;

import jakarta.persistence.*;
import lombok.Data;
import org.example.Elemento;
import org.example.elementi_catalogo.Libro;
import org.example.elementi_catalogo.Rivista;
import org.example.Utente;
import org.example.Prestito;
import org.example.gestione.GestioneElemento;
import org.example.gestione.GestionePrestito;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public class Archivio {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("Catalogo Bibliografico 2");
    private static EntityManager em = emf.createEntityManager();
    private static GestioneElemento gestioneElemento = new GestioneElemento(em);
    private static GestionePrestito gestionePrestito = new GestionePrestito(em);
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            while (true) {
                System.out.println("\n--- Menu Archivio Bibliotecario ---");
                System.out.println("1. Aggiungi un libro");
                System.out.println("2. Aggiungi una rivista");
                System.out.println("3. Aggiungi un utente");
                System.out.println("4. Registra un prestito");
                System.out.println("5. Mostra prestiti per tessera");
                System.out.println("6. Mostra prestiti scaduti");
                System.out.println("7. Rimuovi un elemento");
                System.out.println("8. Ricerca per ISBN");
                System.out.println("9. Ricerca per anno pubblicazione");
                System.out.println("10. Ricerca per autore");
                System.out.println("11. Ricerca per titolo");
                System.out.println("12. Esci");
                System.out.print("Seleziona un'opzione: ");
                int scelta = scanner.nextInt();
                scanner.nextLine(); // Consuma la newline

                switch (scelta) {
                    case 1 -> aggiungiLibro();
                    case 2 -> aggiungiRivista();
                    case 3 -> aggiungiUtente();
                    case 4 -> registraPrestito();
                    case 5 -> mostraPrestitiPerTessera();
                    case 6 -> mostraPrestitiScaduti();
                    case 7 -> rimuoviElemento();
                    case 8 -> ricercaPerISBN();
                    case 9 -> ricercaPerAnnoPubblicazione();
                    case 10 -> ricercaPerAutore();
                    case 11 -> ricercaPerTitolo();
                    case 12 -> {
                        System.out.println("Uscita dal programma.");
                        em.close();
                        emf.close();
                        return;
                    }
                    default -> System.out.println("Opzione non valida.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void aggiungiLibro() {
        System.out.print("Inserisci ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Inserisci titolo: ");
        String titolo = scanner.nextLine();
        System.out.print("Inserisci anno di pubblicazione: ");
        int anno = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Inserisci numero di pagine: ");
        int pagine = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Inserisci autore: ");
        String autore = scanner.nextLine();
        System.out.print("Inserisci genere: ");
        String genere = scanner.nextLine();

        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitolo(titolo);
        libro.setAnnoPubblicazione(anno);
        libro.setNumeroPagine(pagine);
        libro.setAutore(autore);
        libro.setGenere(genere);

        gestioneElemento.aggiungiElemento(libro);
        System.out.println("Libro aggiunto con successo!");
    }

    private static void aggiungiRivista() {
        System.out.print("Inserisci ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Inserisci titolo: ");
        String titolo = scanner.nextLine();
        System.out.print("Inserisci anno di pubblicazione: ");
        int anno = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Inserisci numero di pagine: ");
        int pagine = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Inserisci periodicità (SETTIMANALE, MENSILE, SEMESTRALE): ");
        String periodicita = scanner.nextLine().toUpperCase();

        Rivista rivista = new Rivista();
        rivista.setIsbn(isbn);
        rivista.setTitolo(titolo);
        rivista.setAnnoPubblicazione(anno);
        rivista.setNumeroPagine(pagine);
        rivista.setPeriodicita(Rivista.Periodicita.valueOf(periodicita));

        gestioneElemento.aggiungiElemento(rivista);
        System.out.println("Rivista aggiunta con successo!");
    }

    private static void aggiungiUtente() {
        System.out.print("Inserisci nome: ");
        String nome = scanner.nextLine();
        System.out.print("Inserisci cognome: ");
        String cognome = scanner.nextLine();
        System.out.print("Inserisci data di nascita (yyyy-mm-dd): ");
        String dataNascita = scanner.nextLine();

        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setDataNascita(java.sql.Date.valueOf(dataNascita));

        em.getTransaction().begin();
        em.persist(utente);
        em.getTransaction().commit();

        System.out.println("Utente aggiunto con successo!");
    }

    private static void registraPrestito() {
        System.out.print("Inserisci numero di tessera dell'utente: ");
        Long numeroTessera = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Inserisci ISBN dell'elemento: ");
        String isbn = scanner.nextLine();

        Utente utente = em.find(Utente.class, numeroTessera);
        Elemento elemento = gestioneElemento.ricercaPerISBN(isbn);

        if (utente == null || elemento == null) {
            System.out.println("Utente o elemento non trovato.");
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.JANUARY, 30);
        Date dataRestituzioneProvvisoria = calendar.getTime();


        Prestito prestito = new Prestito();
        prestito.setUtente(utente);
        prestito.setElemento(elemento);
        prestito.setInizioPrestito(new Date());
        prestito.setDataRestituzioneEffettiva(dataRestituzioneProvvisoria);

        em.getTransaction().begin();
        em.persist(prestito);
        em.getTransaction().commit();

        System.out.println("Prestito registrato con successo!");
    }

    private static void mostraPrestitiPerTessera() {
        System.out.print("Inserisci numero di tessera: ");
        Long numeroTessera = scanner.nextLong();
        scanner.nextLine();

        gestionePrestito.prestitiPerTessera(numeroTessera)
                .forEach(System.out::println);
    }

    private static void mostraPrestitiScaduti() {

        List<Prestito> prestitiScaduti = gestionePrestito.prestitiScaduti();


        if (prestitiScaduti.isEmpty()) {
            System.out.println("Non ci sono prestiti scaduti.");
        } else {
            for (Prestito prestito : prestitiScaduti) {
                System.out.println("Prestito scaduto:");
                System.out.println("Utente: " + prestito.getUtente().getNome() + " " + prestito.getUtente().getCognome());
                System.out.println("Elemento: " + prestito.getElemento().getTitolo());
                System.out.println("Data inizio prestito: " + prestito.getInizioPrestito());
                System.out.println("Data restituzione prevista: " + prestito.getDataRestituzionePrevista());
            }
        }
    }

    private static void rimuoviElemento() {
        System.out.print("Inserisci ISBN dell'elemento da rimuovere: ");
        String isbn = scanner.nextLine();

        Elemento elemento = gestioneElemento.ricercaPerISBN(isbn);

        if (elemento != null) {
            em.getTransaction().begin();
            em.remove(elemento);
            em.getTransaction().commit();
            System.out.println("Elemento rimosso con successo!");
        } else {
            System.out.println("Elemento non trovato.");
        }
    }

    private static void ricercaPerISBN() {
        System.out.print("Inserisci ISBN: ");
        String isbn = scanner.nextLine();

        Elemento elemento = gestioneElemento.ricercaPerISBN(isbn);

        if (elemento != null) {
            System.out.println("Elemento trovato: " + elemento);
        } else {
            System.out.println("Elemento non trovato.");
        }
    }

    private static void ricercaPerAnnoPubblicazione() {
        System.out.print("Inserisci anno di pubblicazione: ");
        int anno = scanner.nextInt();
        scanner.nextLine();

        List<Elemento> elementi = gestioneElemento.ricercaPerAnno(anno);

        if (!elementi.isEmpty()) {
            elementi.forEach(System.out::println);
        } else {
            System.out.println("Nessun elemento trovato.");
        }
    }

    private static void ricercaPerAutore() {
        System.out.print("Inserisci autore: ");
        String autore = scanner.nextLine();

        List<Elemento> elementi = gestioneElemento.ricercaPerAutore(autore);

        if (!elementi.isEmpty()) {
            elementi.forEach(System.out::println);
        } else {
            System.out.println("Nessun elemento trovato.");
        }
    }

    private static void ricercaPerTitolo() {
        System.out.print("Inserisci titolo o parte di esso: ");
        String titolo = scanner.nextLine();

        List<Elemento> elementi = gestioneElemento.ricercaPerTitolo(titolo);

        if (!elementi.isEmpty()) {
            elementi.forEach(System.out::println);
        } else {
            System.out.println("Nessun elemento trovato.");
        }
    }
}
