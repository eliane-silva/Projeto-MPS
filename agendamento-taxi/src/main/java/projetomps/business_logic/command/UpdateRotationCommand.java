package projetomps.business_logic.command;

import lombok.AllArgsConstructor;
import projetomps.business_logic.memento.RotationMemento;
import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.model.Taxist;
import projetomps.business_logic.service.RotationService;
import projetomps.business_logic.service.UserService;
import projetomps.util.exception.RepositoryException;

@AllArgsConstructor
public class UpdateRotationCommand implements Command {
    private final RotationService rotationService;
    private final UserService userService;
    private final Rotation newRotationState;
    private RotationMemento oldMemento;

    public UpdateRotationCommand(RotationService rotationService,
                                 UserService userService,
                                 Rotation newRotationState) {
        this.rotationService = rotationService;
        this.userService = userService;
        this.newRotationState = newRotationState;
        this.oldMemento = null;
    }

    @Override
    public void execute() throws RepositoryException {
        Rotation oldRotation = rotationService.buscarPorId(newRotationState.getIdRotation());
        if (oldRotation == null) {
            throw new RepositoryException("Rotação não encontrada para atualização.");
        }

        this.oldMemento = oldRotation.saveToMemento();
        rotationService.updateRotation(newRotationState);
    }

    @Override
    public void undo() throws RepositoryException {
        if (oldMemento == null) {
            throw new RepositoryException("Memento não disponível para desfazer.");
        }

        Rotation restoredRotation = new Rotation();
        restoredRotation.restoreFromMemento(oldMemento);

        if (oldMemento.getTaxistId() > 0) {
            Taxist taxist = (Taxist) userService.buscarPorId(oldMemento.getTaxistId())
                    .orElseThrow(() -> new RepositoryException("Taxista não encontrado"));
            restoredRotation.setTaxist(taxist);
        }

        rotationService.updateRotation(restoredRotation);
    }
}