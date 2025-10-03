package projetomps.business_logic.command;

import lombok.AllArgsConstructor;
import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.service.RotationService;
import projetomps.util.exception.RepositoryException;

@AllArgsConstructor
public class UpdateRotationCommand implements Command {
    private final RotationService rotationService;
    private final Rotation newRotationState;
    private Rotation oldRotationState;

    @Override
    public void execute() throws RepositoryException {
        this.oldRotationState = rotationService.buscarPorId(newRotationState.getIdRotation());
        if (this.oldRotationState == null) {
            throw new RepositoryException("Rotação não encontrada para atualização.");
        }
        rotationService.updateRotation(newRotationState);
    }

    @Override
    public void undo() throws RepositoryException {
        if (oldRotationState != null) {
            rotationService.updateRotation(oldRotationState);
        } else {
            throw new RepositoryException("Estado anterior da rotação é nulo, não é possível desfazer.");
        }
    }
}