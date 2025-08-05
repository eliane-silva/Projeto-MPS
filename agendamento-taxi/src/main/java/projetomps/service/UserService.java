package projetomps.service;

import projetomps.model.User;
import projetomps.repository.UserRepository;
import projetomps.util.exception.LoginException;
import projetomps.util.exception.RepositoryException;
import projetomps.util.exception.SenhaException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    
    public List<User> getAllUsuarios() throws RepositoryException {
        log.info("Buscando todos os usuários");
        return userRepository.buscarTodos();
    }
    
    public boolean deleteUsuario(int id) throws RepositoryException {
        log.info("Deletando usuário com ID: {}", id);
        return userRepository.excluir(id);
    }
    
    public User updateUsuario(User usuario) throws RepositoryException, LoginException, SenhaException {
        log.info("Atualizando usuário: {}", usuario.getLogin());
        
        validarLogin(usuario.getLogin());
        validarSenha(usuario.getSenha());
        
        return userRepository.atualizar(usuario);
    }
    
    public User createUser(String login, String senha) throws LoginException, SenhaException, RepositoryException {
        log.info("Criando usuário: {}", login);
        
        validarLogin(login);
        validarSenha(senha);
        
        User novoUsuario = new User(login, senha);
        return userRepository.salvar(novoUsuario);
    }
    
    public User buscarPorId(int id) throws RepositoryException {
        return userRepository.buscarPorId(id);
    }
    
    public User buscarPorLogin(String login) throws RepositoryException {
        return userRepository.buscarPorLogin(login);
    }
    
    private void validarLogin(String login) throws LoginException {
        if (login == null || login.trim().isEmpty()) {
            throw new LoginException("Login não pode ser vazio");
        }
        
        if (login.length() > 12) {
            throw new LoginException("Login deve ter no máximo 12 caracteres");
        }
        
        if (login.matches(".*\\d.*")) {
            throw new LoginException("Login não pode conter números");
        }
        
        if (!login.matches("^[a-zA-Z\\s]+$")) {
            throw new LoginException("Login deve conter apenas letras e espaços");
        }
    }
    
    private void validarSenha(String senha) throws SenhaException {
        if (senha == null || senha.trim().isEmpty()) {
            throw new SenhaException("Senha não pode ser vazia");
        }
        
        if (senha.length() < 6) {
            throw new SenhaException("Senha deve ter pelo menos 6 caracteres");
        }
        
        if (senha.length() > 20) {
            throw new SenhaException("Senha deve ter no máximo 20 caracteres");
        }
    }
}