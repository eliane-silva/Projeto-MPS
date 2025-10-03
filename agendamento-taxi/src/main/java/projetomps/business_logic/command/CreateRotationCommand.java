package projetomps.business_logic.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.service.RotationService;
import projetomps.util.exception.RepositoryException;

@RequiredArgsConstructor
public class CreateRotationCommand implements Command {
    private final RotationService rotationService;
    private final Rotation rotation;

    @Getter
    private Rotation createdRotation; // Para armazenar a rotação com ID

    @Override
    public void execute() throws RepositoryException {
        // O service já retorna um booleano, vamos adaptar para o objeto salvo
        rotationService.createRotation(rotation); // O ID é setado dentro do DAO
        this.createdRotation = rotation;
    }

    @Override
    public void undo() throws RepositoryException {
        if (createdRotation != null) {
            rotationService.deleteRotation(createdRotation);
        }
    }
}