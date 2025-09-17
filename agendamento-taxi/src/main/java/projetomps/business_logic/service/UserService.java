package projetomps.business_logic.service;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import projetomps.app_logic.dao.UserDAO;
import projetomps.app_logic.log.AppLogger;
import projetomps.app_logic.log.AppLoggerFactory;
import projetomps.business_logic.model.Admin;
import projetomps.business_logic.model.Taxist;
import projetomps.business_logic.model.User;
import projetomps.util.exception.LoginException;
import projetomps.util.exception.RepositoryException;
import projetomps.util.exception.SenhaException;

@AllArgsConstructor
public class UserService {
    private static final AppLogger log =
            AppLoggerFactory.getLogger(UserService.class);

    private final UserDAO userDAO;

    public List<User> getAllUsuarios() throws RepositoryException {
        log.info("Buscando todos os usuários");
        return userDAO.buscarTodos();
    }

    public Optional<User> buscarPorId(int id) throws RepositoryException {
        log.info("Buscando usuário por ID: {}", id);
        User user = userDAO.buscarPorId(id);
        return Optional.ofNullable(user);
    }

    public Optional<User> buscarPorLogin(String login) throws RepositoryException {
        log.info("Buscando usuário por login: {}", login);
        if (login == null || login.trim().isEmpty()) {
            return Optional.empty();
        }
        User user = userDAO.buscarPorLogin(login.trim());
        return Optional.ofNullable(user);
    }

    public boolean deleteUsuario(int id) throws RepositoryException {
        log.info("Deletando usuário com ID: {}", id);
        return userDAO.excluir(id);
    }

    public Admin criarAdmin(String login, String senha) throws RepositoryException {
        log.info("Criando admin: {}", login);
        validarCredenciais(login, senha);
        Admin admin = new Admin(login, senha);
        return (Admin) userDAO.salvar(admin);
    }

    public Admin atualizarAdmin(Admin admin) throws RepositoryException {
        log.info("Atualizando admin: {}", admin != null ? admin.getLogin() : "null");
        validarCredenciais(admin.getLogin(), admin.getSenha());
        verificarExistenciaUsuario(admin.getId(), Admin.class);
        return (Admin) userDAO.atualizar(admin);
    }

    public List<Admin> buscarTodosAdmins() throws RepositoryException {
        log.info("Buscando todos os administradores");
        return getAllUsuarios().stream()
                .filter(Admin.class::isInstance)
                .map(Admin.class::cast)
                .toList();
    }

    public Taxist criarTaxista(String login, String senha, String name, String email) throws RepositoryException {
        log.info("Criando taxista: {}", login);
        validarCredenciais(login, senha);

        Taxist taxist = new Taxist(login, senha);
        taxist.setName(name);
        taxist.setEmail(email);

        validarDadosEspecificosTaxista(taxist);
        return (Taxist) userDAO.salvar(taxist);
    }

    public Taxist atualizarTaxista(Taxist taxist) throws RepositoryException {
        log.info("Atualizando taxista: {}", taxist != null ? taxist.getLogin() : "null");
        validarCredenciais(taxist.getLogin(), taxist.getSenha());
        verificarExistenciaUsuario(taxist.getId(), Taxist.class);
        validarDadosEspecificosTaxista(taxist);
        return (Taxist) userDAO.atualizar(taxist);
    }

    public List<Taxist> buscarTodosTaxistas() throws RepositoryException {
        log.info("Buscando todos os taxistas");
        return getAllUsuarios().stream()
                .filter(Taxist.class::isInstance)
                .map(Taxist.class::cast)
                .toList();
    }

    public String getTipoUsuario(User user) {
        if (user instanceof Admin) return "ADMIN";
        if (user instanceof Taxist) return "TAXIST";
        return "USER";
    }

    public long contarUsuariosPorTipo(Class<? extends User> tipo) throws RepositoryException {
        return getAllUsuarios().stream().filter(tipo::isInstance).count();
    }

    private void validarCredenciais(String login, String senha) throws RepositoryException {
        try {
            validarLogin(login);
            validarSenha(senha);
        } catch (LoginException | SenhaException e) {
            log.warn("Erro de validação ao processar credenciais de '{}'", e, login);
            throw new RepositoryException("Erro de validação: " + e.getMessage(), e);
        }
    }

    private void validarLogin(String login) throws LoginException {
        if (login == null || login.trim().isEmpty()) {
            throw new LoginException("Login não pode ser vazio");
        }
        String loginLimpo = login.trim();
        if (loginLimpo.length() < 3) {
            throw new LoginException("Login deve ter pelo menos 3 caracteres");
        }
        if (loginLimpo.length() > 20) {
            throw new LoginException("Login deve ter no máximo 20 caracteres");
        }
        if (!loginLimpo.matches("^[a-zA-Z0-9_]+$")) {
            throw new LoginException("Login deve conter apenas letras, números e underscore");
        }
    }

    private void validarSenha(String senha) throws SenhaException {
        if (senha == null || senha.trim().isEmpty()) {
            throw new SenhaException("Senha não pode ser vazia");
        }
        if (senha.length() < 3) {
            throw new SenhaException("Senha deve ter pelo menos 3 caracteres");
        }
        if (senha.length() > 50) {
            throw new SenhaException("Senha deve ter no máximo 50 caracteres");
        }
    }

    private void validarDadosEspecificosTaxista(Taxist taxist) throws RepositoryException {
        if (taxist.getEmail() != null && !taxist.getEmail().trim().isEmpty()) {
            String email = taxist.getEmail().trim();
            if (!email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")) {
                throw new RepositoryException("Email inválido");
            }
            taxist.setEmail(email);
        }

        if (taxist.getName() != null && !taxist.getName().trim().isEmpty()) {
            String nome = taxist.getName().trim();
            if (nome.length() > 100) {
                throw new RepositoryException("Nome deve ter no máximo 100 caracteres");
            }
            if (!nome.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
                throw new RepositoryException("Nome deve conter apenas letras e espaços");
            }
            taxist.setName(nome);
        }
    }

    private void verificarExistenciaUsuario(int id, Class<? extends User> tipoEsperado) throws RepositoryException {
        Optional<User> usuario = buscarPorId(id);
        if (usuario.isEmpty()) {
            throw new RepositoryException("Usuário não encontrado");
        }
        if (!tipoEsperado.isInstance(usuario.get())) {
            throw new RepositoryException("Tipo de usuário incorreto");
        }
    }
}
