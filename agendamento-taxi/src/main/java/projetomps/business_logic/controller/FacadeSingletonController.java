package projetomps.business_logic.controller;

import lombok.extern.slf4j.Slf4j;
import projetomps.business_logic.model.Admin;
import projetomps.business_logic.model.Taxist;
import projetomps.business_logic.model.User;
import projetomps.business_logic.service.AuthenticationService;
import projetomps.util.exception.LoginException;
import projetomps.util.exception.RepositoryException;

import java.util.Optional;

@Slf4j
public class FacadeSingletonController {
    private static FacadeSingletonController instance;
    private final UserController userController;
    private final RotationController rotationController;
    private final AuthenticationService authenticationService;

    private FacadeSingletonController(UserController userController,
                                      RotationController rotationController,
                                      AuthenticationService authenticationService) {
        this.userController = userController;
        this.rotationController = rotationController;
        this.authenticationService = authenticationService;
    }

    public static synchronized FacadeSingletonController getInstance(UserController userController,
                                                                     RotationController rotationController,
                                                                     AuthenticationService authenticationService) {
        if (instance == null) {
            instance = new FacadeSingletonController(userController, rotationController, authenticationService);
        }
        return instance;
    }

    public User autenticarUsuario(String login, String senha) throws LoginException, RepositoryException {
        return authenticationService.autenticarUsuario(login, senha);
    }

    public String getTipoUsuario(User usuario) {
        return authenticationService.getTipoUsuario(usuario);
    }

    public boolean isAuthorizedForAdmim(User user) {
        return user instanceof Admin;
    }

    public boolean isAuthorizedForTaxist(User user) {
        return user instanceof Taxist;
    }

    public boolean canAccessUserManagement(User user) {
        return user instanceof Admin;
    }

    public UserController getUserController() {
        return userController;
    }

    public RotationController getRotationController() {
        return rotationController;
    }

    public Optional<Admin> criarAdmin(String login, String senha, User requestingUser) {
        if (!canAccessUserManagement(requestingUser)) {
            log.warn("Usuário {} tentou criar admin sem permissão", requestingUser.getLogin());
            return Optional.empty();
        }

        return userController.criarAdmin(login, senha);
    }

    public Optional<Taxist> criarTaxista(String login, String senha, String name, String email, User requestingUser) {
        if (!canAccessUserManagement(requestingUser)) {
            log.warn("Usuário {} tentou criar taxista sem permissão", requestingUser != null ? requestingUser.getLogin() : "null");
            return Optional.empty();
        }

        return userController.criarTaxista(login, senha, name, email);
    }
}