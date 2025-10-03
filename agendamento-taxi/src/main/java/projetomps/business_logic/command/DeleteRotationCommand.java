package projetomps.business_logic.command;

import lombok.AllArgsConstructor;
import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.service.RotationService;
import projetomps.util.exception.RepositoryException;

@AllArgsConstructor
public class DeleteRotationCommand implements Command {
    private final RotationService rotationService;
    private final Rotation rotationToDelete;
    private Rotation deletedRotationState;

    @Override
    public void execute() throws RepositoryException {
        this.deletedRotationState = rotationService.buscarPorId(rotationToDelete.getIdRotation());
        if (this.deletedRotationState == null) {
            throw new RepositoryException("Rotação não encontrada para exclusão.");
        }
        rotationService.deleteRotation(rotationToDelete);
    }

    @Override
    public void undo() throws RepositoryException {
        if (deletedRotationState != null) {
            // Recria a rotação. O ID será novo, mas os dados serão os mesmos.
            // Esta é uma limitação sem um método "ressuscitar" no serviço/DAO.
            rotationService.createRotation(deletedRotationState);
        } else {
            throw new RepositoryException("Estado da rotação deletada é nulo, não é possível desfazer.");
        }
    }
}