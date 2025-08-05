package projetomps.controller;

import projetomps.model.User;
import projetomps.service.UserService;
import projetomps.util.exception.LoginException;
import projetomps.util.exception.RepositoryException;
import projetomps.util.exception.SenhaException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
    
    public boolean deleteUsuario(int id) {
        try {
            return userService.deleteUsuario(id);
        } catch (RepositoryException e) {
            log.error("Erro ao deletar usuário: {}", e.getMessage());
            return false;
        }
    }
    
    public User updateUsuario(User usuario) {
        try {
            return userService.updateUsuario(usuario);
        } catch (LoginException | SenhaException | RepositoryException e) {
            log.error("Erro ao atualizar usuário: {}", e.getMessage());
            return null;
        }
    }
    
    public User createUser(String login, String senha) {
        try {
            return userService.createUser(login, senha);
        } catch (LoginException | SenhaException | RepositoryException e) {
            log.error("Erro ao criar usuário: {}", e.getMessage());
            return null;
        }
    }
    
    public User buscarUsuario(int id) {
        try {
            return userService.buscarPorId(id);
        } catch (RepositoryException e) {
            log.error("Erro ao buscar usuário: {}", e.getMessage());
            return null;
        }
    }
    
    public User buscarUsuarioPorLogin(String login) {
        try {
            return userService.buscarPorLogin(login);
        } catch (RepositoryException e) {
            log.error("Erro ao buscar usuário por login: {}", e.getMessage());
            return null;
        }
    }
}

