package org.example;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn (name = "tipo_elemento", discriminatorType = DiscriminatorType.STRING)

public abstract class Elemento implements Serializable {

    @Id
    @Column( name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column( name = "titolo", nullable = false, length = 255)
    private String titolo;

    @Column( name = "annoPubblicazione", nullable = false)
    private int annoPubblicazione;

    @Column( name =  "numeroPagine", nullable = false)
    private int numeroPagine;

}
