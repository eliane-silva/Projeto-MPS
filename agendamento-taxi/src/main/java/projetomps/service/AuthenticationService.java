package projetomps.service;

import projetomps.model.Admin;
import projetomps.model.Taxist;
import projetomps.model.User;
import projetomps.repository.UserRepository;
import projetomps.util.exception.LoginException;
import projetomps.util.exception.RepositoryException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    public User autenticarUsuario(String login, String senha) throws LoginException, RepositoryException {
        log.info("Tentativa de autenticação para usuário: {}", login);

        if (login == null || login.trim().isEmpty()) {
            throw new LoginException("Login não pode ser vazio");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new LoginException("Senha não pode ser vazia");
        }

        User usuario = userRepository.buscarPorLogin(login.trim());

        if (usuario == null || !usuario.getSenha().equals(senha.trim())) {
            throw new LoginException("Credenciais inválidas");
        }

        log.info("Usuário autenticado com sucesso: {}", login);
        return usuario;
    }

    public String getTipoUsuario(User usuario) {
        if (usuario instanceof Admin) {
            return "ADMIN";
        } else if (usuario instanceof Taxist) {
            return "TAXISTA";
        }
        return "UNKNOWN";
    }
}
