package projetomps.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import projetomps.model.Admin;
import projetomps.model.Taxist;
import projetomps.model.User;
import projetomps.repository.UserRepository;
import projetomps.util.exception.LoginException;
import projetomps.util.exception.RepositoryException;

@Slf4j
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    public User autenticarUsuario(String login, String senha) throws LoginException, RepositoryException {
        log.info("Tentativa de login para usuário: {}", login);

        if (login == null || login.trim().isEmpty()) {
            throw new LoginException("Login não pode ser vazio");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new LoginException("Senha não pode ser vazia");
        }

        User usuario = userRepository.buscarPorLogin(login.trim());

        if (usuario == null) {
            log.warn("Usuário não encontrado: {}", login);
            throw new LoginException("Usuário ou senha inválidos");
        }

        if (!usuario.getSenha().equals(senha.trim())) {
            log.warn("Senha incorreta para usuário: {}", login);
            throw new LoginException("Usuário ou senha inválidos");
        }

        log.info("Login realizado com sucesso para: {}", login);
        return usuario;
    }

    public String getTipoUsuario(User usuario) {
        if (usuario instanceof Admin) {
            return "ADMIN";
        } else if (usuario instanceof Taxist) {
            return "TAXIST";
        } else {
            return "USER";
        }
    }
}