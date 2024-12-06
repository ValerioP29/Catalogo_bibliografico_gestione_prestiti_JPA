package org.example.elementi_catalogo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import org.example.Elemento;

@Entity
@Data
@DiscriminatorValue("Libro")
public class Libro extends Elemento {

    @Column (name = "autore", nullable = true, length = 255)
    private String autore;

    @Column (name = "genere", nullable = true, length = 100)
    private String genere;
}
