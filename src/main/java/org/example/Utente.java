package org.example;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class Utente implements Serializable {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column (name = "numero_tessera")
    private Long numeroTessera;

    @Column (name = "nome", nullable = false, length = 100)
    private String nome;

    @Column (name = "cognome", nullable = false, length = 100)
    private String cognome;

    @Column (name = "data_nascita", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataNascita;
}
