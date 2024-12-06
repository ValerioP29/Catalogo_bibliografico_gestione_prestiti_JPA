package org.example;

import jakarta.persistence.*;
import lombok.Data;

import javax.lang.model.element.Name;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data

public class Prestito {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "elemento_id", nullable = false)
    private  Elemento elemento;

    @Column (name = "inizio_prestito", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date inizioPrestito;

    @Column (name = "data_restituzione_prevista", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataRestituzionePrevista;

    @Column(name= "data_restituzione_effettiva", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date dataRestituzioneEffettiva;

    @PrePersist
    public void calcolaDataRestituzione() {
        if (inizioPrestito != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(inizioPrestito);
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            dataRestituzionePrevista = calendar.getTime();
        }
    }
}
