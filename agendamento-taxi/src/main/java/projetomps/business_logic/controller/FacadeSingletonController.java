package projetomps.business_logic.controller;

import java.time.LocalDate;
import java.util.List;

import projetomps.app_logic.log.AppLogger;
import projetomps.app_logic.log.AppLoggerFactory;
import projetomps.business_logic.model.Admin;
import projetomps.business_logic.model.Relatorio;
import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.model.Taxist;
import projetomps.business_logic.model.User;
import projetomps.business_logic.service.AuthenticationService;
import projetomps.util.exception.LoginException;
import projetomps.util.exception.RepositoryException;

import java.util.Optional;

public class FacadeSingletonController {
    private static final AppLogger log = AppLoggerFactory.getLogger(FacadeSingletonController.class);

    private static FacadeSingletonController instance;
    private final UserController userController;
    private final RotationController rotationController;
    private final RelatorioController relatorioController;
    private final AuthenticationService authenticationService;

    private FacadeSingletonController(UserController userController,
            RotationController rotationController,
            AuthenticationService authenticationService, RelatorioController relatorioController) {
        this.userController = userController;
        this.rotationController = rotationController;
        this.authenticationService = authenticationService;
        this.relatorioController = relatorioController;
    }

    public static synchronized FacadeSingletonController getInstance(UserController userController,
            RotationController rotationController,
            AuthenticationService authenticationService,
            RelatorioController relatorioController) {
        if (instance == null) {
            instance = new FacadeSingletonController(userController, rotationController, authenticationService, relatorioController);
            log.info("FacadeSingletonController inicializado");
        }
        return instance;
    }

    public User autenticarUsuario(String login, String senha) throws LoginException, RepositoryException {
        log.debug("Tentando autenticar usuário: {}", login);
        return authenticationService.autenticarUsuario(login, senha);
    }

    public String getTipoUsuario(User usuario) {
        String tipo = authenticationService.getTipoUsuario(usuario);
        log.debug("Tipo de usuário resolvido: {}", tipo);
        return tipo;
    }

    public boolean isAuthorizedForAdmim(User user) {
        boolean ok = user instanceof Admin;
        log.debug("isAuthorizedForAdmim({}) -> {}", user != null ? user.getLogin() : "null", ok);
        return ok;
    }

    public boolean isAuthorizedForTaxist(User user) {
        boolean ok = user instanceof Taxist;
        log.debug("isAuthorizedForTaxist({}) -> {}", user != null ? user.getLogin() : "null", ok);
        return ok;
    }

    public boolean canAccessUserManagement(User user) {
        boolean ok = user instanceof Admin;
        log.debug("canAccessUserManagement({}) -> {}", user != null ? user.getLogin() : "null", ok);
        return ok;
    }

    public UserController getUserController() {
        return userController;
    }

    public RotationController getRotationController() {
        return rotationController;
    }

    public Optional<Admin> criarAdmin(String login, String senha, User requestingUser) {
        if (!canAccessUserManagement(requestingUser)) {
            log.warn("Usuário {} tentou criar admin sem permissão",
                    requestingUser != null ? requestingUser.getLogin() : "null");
            return Optional.empty();
        }
        log.info("Criando Admin: {}", login);
        return userController.criarAdmin(login, senha);
    }

    public Optional<Taxist> criarTaxista(String login, String senha, String name, String email, User requestingUser) {
        if (!canAccessUserManagement(requestingUser)) {
            log.warn("Usuário {} tentou criar taxista sem permissão",
                    requestingUser != null ? requestingUser.getLogin() : "null");
            return Optional.empty();
        }
        log.info("Criando Taxista: {}", login);
        return userController.criarTaxista(login, senha, name, email);
    }

    public Relatorio gerarRelatorio(LocalDate dataInicio, LocalDate dataFim) throws RepositoryException {
        return relatorioController.getRelatorio(dataInicio, dataFim);
    }
}
