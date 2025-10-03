package projetomps.business_logic.command;

import lombok.AllArgsConstructor;
import projetomps.business_logic.memento.RotationMemento;
import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.model.Taxist;
import projetomps.business_logic.service.RotationService;
import projetomps.business_logic.service.UserService;
import projetomps.util.exception.RepositoryException;

@AllArgsConstructor
public class DeleteRotationCommand implements Command {
    private final RotationService rotationService;
    private final UserService userService;
    private final Rotation rotationToDelete;
    private RotationMemento deletedMemento;

    public DeleteRotationCommand(RotationService rotationService,
                                 UserService userService,
                                 Rotation rotationToDelete) {
        this.rotationService = rotationService;
        this.userService = userService;
        this.rotationToDelete = rotationToDelete;
        this.deletedMemento = null;
    }

    @Override
    public void execute() throws RepositoryException {
        Rotation existingRotation = rotationService.buscarPorId(rotationToDelete.getIdRotation());
        if (existingRotation == null) {
            throw new RepositoryException("Rotação não encontrada para exclusão.");
        }

        this.deletedMemento = existingRotation.saveToMemento();
        rotationService.deleteRotation(rotationToDelete);
    }

    @Override
    public void undo() throws RepositoryException {
        if (deletedMemento == null) {
            throw new RepositoryException("Memento não disponível para desfazer.");
        }

        Rotation restoredRotation = new Rotation();
        restoredRotation.restoreFromMemento(deletedMemento);

        if (deletedMemento.getTaxistId() > 0) {
            Taxist taxist = (Taxist) userService.buscarPorId(deletedMemento.getTaxistId())
                    .orElseThrow(() -> new RepositoryException("Taxista não encontrado"));
            restoredRotation.setTaxist(taxist);
        }

        rotationService.createRotationComId(restoredRotation);
    }
}