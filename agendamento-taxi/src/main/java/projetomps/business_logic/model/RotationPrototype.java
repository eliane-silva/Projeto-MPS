package projetomps.business_logic.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Implementação concreta do padrão Prototype aplicada à classe Rotation.
 * Cria uma cópia de uma rotação existente, modificando apenas os campos necessários.
 */
public class RotationPrototype implements Prototipo<Rotation> {

    private final Rotation baseRotation;

    public RotationPrototype(Rotation baseRotation) {
        this.baseRotation = baseRotation;
    }

    @Override
    public Rotation clonar() {
        Rotation clone = new Rotation();
        clone.setIdRotation(baseRotation.getIdRotation());
        clone.setDate(baseRotation.getDate());
        clone.setStartTime(baseRotation.getStartTime());
        clone.setEndTime(baseRotation.getEndTime());
        clone.setStatus(baseRotation.getStatus());
        clone.setTaxist(baseRotation.getTaxist());
        return clone;
    }

    /**
     * Cria uma cópia com novos valores de data, horário e taxista.
     */
    public Rotation clonarComAlteracoes(LocalDate novaData, LocalTime novoInicio, LocalTime novoFim, Taxist novoTaxist) {
        Rotation clone = clonar();
        if (novaData != null) clone.setDate(novaData);
        if (novoInicio != null) clone.setStartTime(novoInicio);
        if (novoFim != null) clone.setEndTime(novoFim);
        if (novoTaxist != null) clone.setTaxist(novoTaxist);
        return clone;
    }
}
