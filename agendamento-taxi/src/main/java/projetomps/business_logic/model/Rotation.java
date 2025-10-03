package projetomps.business_logic.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
import projetomps.business_logic.memento.RotationMemento;

@Getter
@Setter
public class Rotation {
    private int idRotation;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private Taxist taxist;

    /**
     * Cria um Memento com o estado atual da Rotation.
     */
    public RotationMemento saveToMemento() {
        return new RotationMemento(
                idRotation,
                date,
                startTime,
                endTime,
                status,
                taxist != null ? taxist.getId() : 0,
                taxist != null ? taxist.getLogin() : null
        );
    }

    /**
     * Restaura o estado a partir de um Memento.
     * Nota: O objeto Taxist precisa ser reconstruído externamente.
     */
    public void restoreFromMemento(RotationMemento memento) {
        this.idRotation = memento.getIdRotation();
        this.date = memento.getDate();
        this.startTime = memento.getStartTime();
        this.endTime = memento.getEndTime();
        this.status = memento.getStatus();
    }
}