package projetomps.business_logic.command;

import lombok.AllArgsConstructor;
import projetomps.business_logic.memento.UserMemento;
import projetomps.business_logic.model.Admin;
import projetomps.business_logic.model.Taxist;
import projetomps.business_logic.model.User;
import projetomps.business_logic.service.UserService;
import projetomps.util.exception.RepositoryException;

@AllArgsConstructor
public class UpdateUserCommand implements Command {
    private final UserService userService;
    private final User newUserState;
    private UserMemento oldMemento;

    public UpdateUserCommand(UserService userService, User newUserState) {
        this.userService = userService;
        this.newUserState = newUserState;
        this.oldMemento = null;
    }

    @Override
    public void execute() throws RepositoryException {
        User oldUser = userService.buscarPorId(newUserState.getId())
                .orElseThrow(() -> new RepositoryException("Usuário não encontrado para atualização."));

        this.oldMemento = oldUser.saveToMemento();

        if (newUserState instanceof Taxist) {
            userService.atualizarTaxista((Taxist) newUserState);
        } else if (newUserState instanceof Admin) {
            userService.atualizarAdmin((Admin) newUserState);
        } else {
            throw new RepositoryException("Tipo de usuário não suportado");
        }
    }

    @Override
    public void undo() throws RepositoryException {
        if (oldMemento == null) {
            throw new RepositoryException("Memento não disponível para desfazer.");
        }

        if ("TAXIST".equals(oldMemento.getTipo())) {
            Taxist restoredTaxist = new Taxist();
            restoredTaxist.restoreFromMemento(oldMemento);
            userService.atualizarTaxista(restoredTaxist);
        } else if ("ADMIN".equals(oldMemento.getTipo())) {
            Admin restoredAdmin = new Admin();
            restoredAdmin.restoreFromMemento(oldMemento);
            userService.atualizarAdmin(restoredAdmin);
        }
    }
}