package projetomps.business_logic.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import projetomps.business_logic.command.CommandInvoker;
import projetomps.business_logic.command.CreateRotationCommand;
import projetomps.business_logic.command.DeleteRotationCommand;
import projetomps.business_logic.command.UpdateRotationCommand;
import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.model.RotationPrototype; // << usa o Prototype que você já criou
import projetomps.business_logic.model.Taxist;
import projetomps.business_logic.service.RotationService;
import projetomps.business_logic.service.UserService;
import projetomps.util.exception.RepositoryException;

@AllArgsConstructor
public class RotationController {
    private final RotationService rotationService;
    private final UserService userService;
    private final CommandInvoker invoker;

    // ================= CRUD já existente =================

    public List<Rotation> getAllRotations() throws RepositoryException {
        return rotationService.getAllRotations();
    }

    public Boolean createRotation(Rotation rotation) {
        try {
            CreateRotationCommand command = new CreateRotationCommand(rotationService, rotation);
            invoker.execute(command);
            return true;
        } catch (RepositoryException e) {
            return false;
        }
    }

    public Rotation updateRotation(Rotation rotation) {
        try {
            UpdateRotationCommand command = new UpdateRotationCommand(
                    rotationService, userService, rotation);
            invoker.execute(command);
            return rotation;
        } catch (RepositoryException e) {
            return null;
        }
    }

    public Boolean deleteRotation(Rotation rotation) {
        try {
            DeleteRotationCommand command = new DeleteRotationCommand(
                    rotationService, userService, rotation);
            invoker.execute(command);
            return true;
        } catch (RepositoryException e) {
            return false;
        }
    }

    public boolean undo() {
        return invoker.undo();
    }

    public boolean redo() {
        return invoker.redo();
    }

    public boolean canUndo() {
        return invoker.canUndo();
    }

    public boolean canRedo() {
        return invoker.canRedo();
    }

    // ================= Prototype: novos métodos =================

    /**
     * Clona uma rotação existente (protótipo) alterando data, horários e taxista.
     * Não altera a rotação base. Persiste o clone usando o fluxo normal (Command).
     *
     * @param base       rotação que servirá de protótipo (já existente)
     * @param newDate    nova data (obrigatória)
     * @param newStart   novo horário de início (obrigatório)
     * @param newEnd     novo horário de fim (opcional; pode ser null)
     * @param newTaxist  taxista do clone (se null, mantém o da base)
     * @return           a rotação clonada e persistida; null se falhar
     */
    public Rotation cloneFromPrototype(Rotation base,
                                       LocalDate newDate,
                                       LocalTime newStart,
                                       LocalTime newEnd,
                                       Taxist newTaxist) {
        if (base == null || newDate == null || newStart == null) return null;
        if (newEnd != null && newEnd.isBefore(newStart)) return null;

        // Usa o Prototype concreto
        RotationPrototype proto = new RotationPrototype(base);

        // Gera o clone mudando apenas o necessário
        Rotation clone = proto.clonarComAlteracoes(newDate, newStart, newEnd, newTaxist != null ? newTaxist : base.getTaxist());

        // Zera o id para deixar o DAO/Service atribuírem um novo (se aplicável)
        clone.setIdRotation(0);

        // Persiste pelo mesmo fluxo do create
        return createRotation(clone) ? clone : null;
    }

    /**
     * Clonagem em lote: recebe specs com (data, início, fim, taxista) e cria várias cópias.
     * Útil para preencher turnos rapidamente.
     */
    public List<Rotation> bulkCloneFromPrototype(Rotation base, List<CloneSpec> specs) {
        List<Rotation> out = new ArrayList<>();
        if (base == null || specs == null || specs.isEmpty()) return out;

        for (CloneSpec spec : specs) {
            Rotation c = cloneFromPrototype(base, spec.date, spec.start, spec.end, spec.taxist);
            if (c != null) out.add(c);
        }
        return out;
    }

    /** DTO simples para bulk clone sem criar novos arquivos. */
    public static class CloneSpec {
        public final LocalDate date;
        public final LocalTime start;
        public final LocalTime end;    // pode ser null
        public final Taxist taxist;    // pode ser null (mantém da base)

        public CloneSpec(LocalDate date, LocalTime start, LocalTime end, Taxist taxist) {
            this.date = date;
            this.start = start;
            this.end = end;
            this.taxist = taxist;
        }
    }
}
