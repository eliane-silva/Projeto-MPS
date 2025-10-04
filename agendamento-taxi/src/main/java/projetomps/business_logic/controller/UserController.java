package projetomps.business_logic.controller;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import projetomps.app_logic.log.AppLogger;
import projetomps.app_logic.log.AppLoggerFactory;
import projetomps.business_logic.model.Admin;
import projetomps.business_logic.model.Taxist;
import projetomps.business_logic.model.User;
import projetomps.business_logic.service.UserService;
import projetomps.util.exception.RepositoryException;
import projetomps.business_logic.command.CommandInvoker;
import projetomps.business_logic.command.UpdateUserCommand;
import projetomps.business_logic.iterator.UserCollection;
import projetomps.business_logic.iterator.UserIterator;

@AllArgsConstructor
public class UserController {
    private static final AppLogger log =
            AppLoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final CommandInvoker invoker;

    public List<User> getAllUsuarios() {
        try {
            return userService.getAllUsuarios();
        } catch (RepositoryException e) {
            log.error("Erro ao buscar usuários", e);
            throw new RuntimeException("Erro interno do sistema", e);
        }
    }

    public Optional<User> buscarUsuario(int id) {
        try {
            return userService.buscarPorId(id);
        } catch (RepositoryException e) {
            log.error("Erro ao buscar usuário: {}", e, id);
            return Optional.empty();
        }
    }

    public Optional<User> buscarUsuarioPorLogin(String login) {
        try {
            return userService.buscarPorLogin(login);
        } catch (RepositoryException e) {
            log.error("Erro ao buscar usuário por login: {}", e, login);
            return Optional.empty();
        }
    }

    public boolean deleteUsuario(int id) {
        try {
            boolean ok = userService.deleteUsuario(id);
            if (ok) log.info("Usuário deletado: {}", id);
            return ok;
        } catch (RepositoryException e) {
            log.error("Erro ao deletar usuário: {}", e, id);
            return false;
        }
    }

    public Optional<Admin> criarAdmin(String login, String senha) {
        try {
            Admin admin = userService.criarAdmin(login, senha);
            log.info("Admin criado: {}", login);
            return Optional.of(admin);
        } catch (RepositoryException e) {
            log.error("Erro ao criar admin: {}", e, login);
            return Optional.empty();
        }
    }

    public Optional<Admin> atualizarAdmin(Admin admin) {
        try {
            Admin adminAtualizado = userService.atualizarAdmin(admin);
            log.info("Admin atualizado: {}", admin.getLogin());
            return Optional.of(adminAtualizado);
        } catch (RepositoryException e) {
            log.error("Erro ao atualizar admin: {}", e, admin != null ? admin.getLogin() : "null");
            return Optional.empty();
        }
    }

    public List<Admin> buscarTodosAdmins() {
        try {
            return userService.buscarTodosAdmins();
        } catch (RepositoryException e) {
            log.error("Erro ao buscar admins", e);
            return List.of();
        }
    }

    public Optional<Taxist> criarTaxista(String login, String senha, String name, String email) {
        try {
            Taxist taxist = userService.criarTaxista(login, senha, name, email);
            log.info("Taxista criado: {}", login);
            return Optional.of(taxist);
        } catch (RepositoryException e) {
            log.error("Erro ao criar taxista: {}", e, login);
            return Optional.empty();
        }
    }

    public Optional<Taxist> atualizarTaxista(Taxist taxist) {
        try {
            UpdateUserCommand command = new UpdateUserCommand(userService, taxist);
            invoker.execute(command);
            log.info("Comando de atualização de taxista executado para: {}", taxist.getLogin());
            return Optional.of(taxist);
        } catch (RepositoryException e) {
            log.error("Erro ao executar comando de atualização de taxista: {}", e, taxist != null ? taxist.getLogin() : "null");
            return Optional.empty();
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

    public List<Taxist> buscarTodosTaxistas() {
        try {
            return userService.buscarTodosTaxistas();
        } catch (RepositoryException e) {
            log.error("Erro ao buscar taxistas", e);
            return List.of();
        }
    }

    public String getTipoUsuario(User user) {
        return userService.getTipoUsuario(user);
    }

    public long contarUsuariosPorTipo(Class<? extends User> tipo) {
        try {
            return userService.contarUsuariosPorTipo(tipo);
        } catch (RepositoryException e) {
            log.error("Erro ao contar usuários por tipo: {}", e, tipo != null ? tipo.getSimpleName() : "null");
            return 0;
        }
    }

    public List<User> listarUsuariosComIterator() throws RepositoryException {
        log.info("Buscando todos os usuários");
        List<User> usuarios = userService.getAllUsuarios();

        UserCollection collection = new UserCollection(usuarios);
        UserIterator iterator = collection.createIterator();

        log.info("Listando usuários com Iterator:");
        while (iterator.hasNext()) {
            User user = iterator.next();
            log.info("Usuário encontrado: {}", user.getLogin());
        }

        return usuarios;
    } 
}