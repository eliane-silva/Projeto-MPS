package projetomps.controller;

import lombok.extern.slf4j.Slf4j;
import projetomps.model.User;
import projetomps.service.AuthenticationService;
import projetomps.util.exception.LoginException;
import projetomps.util.exception.RepositoryException;

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

    public UserController getUserController() {
        return userController;
    }

    public RotationController getRotationController() {
        return rotationController;
    }
}
