package projetomps.business_logic.memento;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Memento que armazena o estado de uma Rotation.
 */
@Getter
@AllArgsConstructor
public class RotationMemento {
    private final int idRotation;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String status;
    private final int taxistId;
    private final String taxistLogin;
}