package projetomps.business_logic.controller;

import projetomps.business_logic.model.Admin;
import projetomps.business_logic.model.Taxist;
import projetomps.business_logic.model.User;
import projetomps.business_logic.service.UserService;
import projetomps.util.exception.RepositoryException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    public List<User> getAllUsuarios() {
        try {
            return userService.getAllUsuarios();
        } catch (RepositoryException e) {
            log.error("Erro ao buscar usuários: {}", e.getMessage());
            throw new RuntimeException("Erro interno do sistema", e);
        }
    }

    public Optional<User> buscarUsuario(int id) {
        try {
            return userService.buscarPorId(id);
        } catch (RepositoryException e) {
            log.error("Erro ao buscar usuário: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<User> buscarUsuarioPorLogin(String login) {
        try {
            return userService.buscarPorLogin(login);
        } catch (RepositoryException e) {
            log.error("Erro ao buscar usuário por login: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public boolean deleteUsuario(int id) {
        try {
            return userService.deleteUsuario(id);
        } catch (RepositoryException e) {
            log.error("Erro ao deletar usuário: {}", e.getMessage());
            return false;
        }
    }

    public Optional<Admin> criarAdmin(String login, String senha) {
        try {
            Admin admin = userService.criarAdmin(login, senha);
            return Optional.of(admin);
        } catch (RepositoryException e) {
            log.error("Erro ao criar admin: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Admin> atualizarAdmin(Admin admin) {
        try {
            Admin adminAtualizado = userService.atualizarAdmin(admin);
            return Optional.of(adminAtualizado);
        } catch (RepositoryException e) {
            log.error("Erro ao atualizar admin: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public List<Admin> buscarTodosAdmins() {
        try {
            return userService.buscarTodosAdmins();
        } catch (RepositoryException e) {
            log.error("Erro ao buscar admins: {}", e.getMessage());
            return List.of();
        }
    }

    public Optional<Taxist> criarTaxista(String login, String senha, String name, String email) {
        try {
            Taxist taxist = userService.criarTaxista(login, senha, name, email);
            return Optional.of(taxist);
        } catch (RepositoryException e) {
            log.error("Erro ao criar taxista: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Taxist> atualizarTaxista(Taxist taxist) {
        try {
            Taxist taxistAtualizado = userService.atualizarTaxista(taxist);
            return Optional.of(taxistAtualizado);
        } catch (RepositoryException e) {
            log.error("Erro ao atualizar taxista: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public List<Taxist> buscarTodosTaxistas() {
        try {
            return userService.buscarTodosTaxistas();
        } catch (RepositoryException e) {
            log.error("Erro ao buscar taxistas: {}", e.getMessage());
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
            log.error("Erro ao contar usuários por tipo: {}", e.getMessage());
            return 0;
        }
    }
}