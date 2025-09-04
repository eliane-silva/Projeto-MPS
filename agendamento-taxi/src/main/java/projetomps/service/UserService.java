package projetomps.service;

import projetomps.model.Admin;
import projetomps.model.Taxist;
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

    public Admin salvarAdmin(Admin admin) throws RepositoryException {
        log.info("Salvando admin: {}", admin.getLogin());
        try {
            validarLogin(admin.getLogin());
            validarSenha(admin.getSenha());

            // Verificar se login já existe
            if (loginJaExiste(admin.getLogin())) {
                throw new RepositoryException("Login já está sendo usado por outro usuário");
            }

            return (Admin) userRepository.salvar(admin);
        } catch (LoginException | SenhaException e) {
            throw new RepositoryException("Erro de validação: " + e.getMessage(), e);
        }
    }

    public Taxist salvarTaxist(Taxist taxist) throws RepositoryException {
        log.info("Salvando taxista: {}", taxist.getLogin());
        try {
            validarLogin(taxist.getLogin());
            validarSenha(taxist.getSenha());

            // Verificar se login já existe
            if (loginJaExiste(taxist.getLogin())) {
                throw new RepositoryException("Login já está sendo usado por outro usuário");
            }

            // Validar dados específicos do taxista
            validarDadosTaxist(taxist);

            return (Taxist) userRepository.salvar(taxist);
        } catch (LoginException | SenhaException e) {
            throw new RepositoryException("Erro de validação: " + e.getMessage(), e);
        }
    }

    public User buscarPorId(int id) throws RepositoryException {
        log.info("Buscando usuário por ID: {}", id);
        return userRepository.buscarPorId(id);
    }

    public User buscarPorLogin(String login) throws RepositoryException {
        log.info("Buscando usuário por login: {}", login);
        return userRepository.buscarPorLogin(login);
    }

    // Novos métodos para CRUD completo

    public Admin criarAdmin(String login, String senha) throws RepositoryException {
        log.info("Criando admin: {}", login);
        Admin admin = new Admin(login, senha);
        return salvarAdmin(admin);
    }

    public Taxist criarTaxist(String login, String senha, String name, String email) throws RepositoryException {
        log.info("Criando taxista: {}", login);
        Taxist taxist = new Taxist(login, senha);
        taxist.setName(name);
        taxist.setEmail(email);
        return salvarTaxist(taxist);
    }

    public Admin atualizarAdmin(Admin admin) throws RepositoryException {
        log.info("Atualizando admin: {}", admin.getLogin());
        try {
            validarLogin(admin.getLogin());
            validarSenha(admin.getSenha());

            // Verificar se o admin existe
            User usuarioExistente = buscarPorId(admin.getId());
            if (usuarioExistente == null || !(usuarioExistente instanceof Admin)) {
                throw new RepositoryException("Administrador não encontrado");
            }

            // Verificar se o novo login não está sendo usado por outro usuário
            if (!admin.getLogin().equals(usuarioExistente.getLogin()) && loginJaExiste(admin.getLogin())) {
                throw new RepositoryException("Login já está sendo usado por outro usuário");
            }

            return (Admin) userRepository.atualizar(admin);
        } catch (LoginException | SenhaException e) {
            throw new RepositoryException("Erro de validação: " + e.getMessage(), e);
        }
    }

    public Taxist atualizarTaxist(Taxist taxist) throws RepositoryException {
        log.info("Atualizando taxista: {}", taxist.getLogin());
        try {
            validarLogin(taxist.getLogin());
            validarSenha(taxist.getSenha());

            // Verificar se o taxista existe
            User usuarioExistente = buscarPorId(taxist.getId());
            if (usuarioExistente == null || !(usuarioExistente instanceof Taxist)) {
                throw new RepositoryException("Taxista não encontrado");
            }

            // Verificar se o novo login não está being usado por outro usuário
            if (!taxist.getLogin().equals(usuarioExistente.getLogin()) && loginJaExiste(taxist.getLogin())) {
                throw new RepositoryException("Login já está sendo usado por outro usuário");
            }

            // Validar dados específicos do taxista
            validarDadosTaxist(taxist);

            return (Taxist) userRepository.atualizar(taxist);
        } catch (LoginException | SenhaException e) {
            throw new RepositoryException("Erro de validação: " + e.getMessage(), e);
        }
    }

    public boolean removerAdmin(int adminId) throws RepositoryException {
        log.info("Removendo admin com ID: {}", adminId);

        User usuario = buscarPorId(adminId);
        if (usuario == null || !(usuario instanceof Admin)) {
            throw new RepositoryException("Administrador não encontrado");
        }

        return deleteUsuario(adminId);
    }

    public boolean removerTaxist(int taxistId) throws RepositoryException {
        log.info("Removendo taxista com ID: {}", taxistId);

        User usuario = buscarPorId(taxistId);
        if (usuario == null || !(usuario instanceof Taxist)) {
            throw new RepositoryException("Taxista não encontrado");
        }

        return deleteUsuario(taxistId);
    }

    public List<Admin> buscarTodosAdmins() throws RepositoryException {
        log.info("Buscando todos os administradores");
        return getAllUsuarios().stream()
                .filter(user -> user instanceof Admin)
                .map(user -> (Admin) user)
                .toList();
    }

    public List<Taxist> buscarTodosTaxistas() throws RepositoryException {
        log.info("Buscando todos os taxistas");
        return getAllUsuarios().stream()
                .filter(user -> user instanceof Taxist)
                .map(user -> (Taxist) user)
                .toList();
    }

    // Métodos de validação

    private boolean loginJaExiste(String login) {
        try {
            User usuario = buscarPorLogin(login);
            return usuario != null;
        } catch (RepositoryException e) {
            log.warn("Erro ao verificar se login existe: {}", e.getMessage());
            return false;
        }
    }

    private void validarDadosTaxist(Taxist taxist) throws RepositoryException {
        // Validar email se fornecido
        if (taxist.getEmail() != null && !taxist.getEmail().trim().isEmpty()) {
            if (!taxist.getEmail().matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")) {
                throw new RepositoryException("Email inválido");
            }
        }

        // Validar nome se fornecido
        if (taxist.getName() != null && !taxist.getName().trim().isEmpty()) {
            if (taxist.getName().length() > 100) {
                throw new RepositoryException("Nome deve ter no máximo 100 caracteres");
            }

            if (!taxist.getName().matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
                throw new RepositoryException("Nome deve conter apenas letras e espaços");
            }
        }
    }

    private void validarLogin(String login) throws LoginException {
        if (login == null || login.trim().isEmpty()) {
            throw new LoginException("Login não pode ser vazio");
        }

        if (login.length() > 12) {
            throw new LoginException("Login deve ter no máximo 12 caracteres");
        }

        if (login.length() < 3) {
            throw new LoginException("Login deve ter pelo menos 3 caracteres");
        }

        // Validação mais flexível para permitir logins como "admin"
        if (!login.matches("^[a-zA-Z0-9\\s]+$")) {
            throw new LoginException("Login deve conter apenas letras, números e espaços");
        }
    }

    private void validarSenha(String senha) throws SenhaException {
        if (senha == null || senha.trim().isEmpty()) {
            throw new SenhaException("Senha não pode ser vazia");
        }

        if (senha.length() < 3) {
            throw new SenhaException("Senha deve ter pelo menos 3 caracteres");
        }

        if (senha.length() > 20) {
            throw new SenhaException("Senha deve ter no máximo 20 caracteres");
        }
    }

    // Métodos utilitários

    public String getTipoUsuario(User user) {
        if (user instanceof Admin) {
            return "ADMIN";
        } else if (user instanceof Taxist) {
            return "TAXIST";
        } else {
            return "USER";
        }
    }

    public boolean isAdmin(User user) {
        return user instanceof Admin;
    }

    public boolean isTaxist(User user) {
        return user instanceof Taxist;
    }

    public long contarUsuariosPorTipo(Class<?> tipo) throws RepositoryException {
        return getAllUsuarios().stream()
                .filter(tipo::isInstance)
                .count();
    }
}