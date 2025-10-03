package projetomps.business_logic.command;

import lombok.AllArgsConstructor;
import projetomps.business_logic.model.User;
import projetomps.business_logic.service.UserService;
import projetomps.util.exception.RepositoryException;

@AllArgsConstructor
public class UpdateUserCommand implements Command {
    private final UserService userService;
    private final User newUserState;
    private User oldUserState; // Para guardar o estado anterior

    @Override
    public void execute() throws RepositoryException {
        // Busca e armazena o estado antigo ANTES de atualizar
        this.oldUserState = userService.buscarPorId(newUserState.getId())
                .orElseThrow(() -> new RepositoryException("Usuário não encontrado para atualização."));

        userService.atualizarTaxista((projetomps.business_logic.model.Taxist) newUserState); // Supondo Taxist por simplicidade
    }

    @Override
    public void undo() throws RepositoryException {
        if (oldUserState != null) {
            userService.atualizarTaxista((projetomps.business_logic.model.Taxist) oldUserState);
        } else {
            throw new RepositoryException("Estado anterior do usuário é nulo, não é possível desfazer.");
        }
    }
}