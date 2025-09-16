package projetomps.business_logic.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rotation {
    private int idRotation;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private Taxist taxist;
}
