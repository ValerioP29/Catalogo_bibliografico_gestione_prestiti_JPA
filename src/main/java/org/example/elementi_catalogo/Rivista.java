package org.example.elementi_catalogo;

import jakarta.persistence.*;
import lombok.Data;
import org.example.Elemento;
import org.hibernate.metamodel.mapping.NonAggregatedIdentifierMapping;

@Entity
@Data
@DiscriminatorValue("Rivista")
public class Rivista extends Elemento {

    public enum Periodicita {
        SETTIMANALE, MENSILE, SEMESTRALE
    }

    @Enumerated(EnumType.STRING)
    @Column (name = "periodicita", nullable = true)
    private Periodicita periodicita;


}
