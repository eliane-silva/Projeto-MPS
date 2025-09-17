package projetomps.business_logic.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Relatorio {
    private LocalDate date;
    private String conteudo;

}
