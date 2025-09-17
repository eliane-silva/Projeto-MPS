package projetomps.business_logic.service;

import lombok.AllArgsConstructor;
import projetomps.app_logic.log.AppLogger;
import projetomps.app_logic.log.AppLoggerFactory;
import projetomps.app_logic.dao.UserDAO;
import projetomps.business_logic.model.Admin;
import projetomps.business_logic.model.Taxist;
import projetomps.business_logic.model.User;
import projetomps.util.exception.LoginException;
import projetomps.util.exception.RepositoryException;

@AllArgsConstructor
public class AuthenticationService {
    private static final AppLogger log =
            AppLoggerFactory.getLogger(AuthenticationService.class);

    private final UserDAO userDAO;

    public User autenticarUsuario(String login, String senha) throws LoginException, RepositoryException {
        log.info("Tentativa de login para usuário: {}", login);

        if (login == null || login.trim().isEmpty()) {
            throw new LoginException("Login não pode ser vazio");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new LoginException("Senha não pode ser vazia");
        }

        User usuario = userDAO.buscarPorLogin(login.trim());

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