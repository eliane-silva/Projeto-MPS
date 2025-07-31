package projetomps.view;

import java.util.List;
import java.util.Scanner;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import projetomps.manager.UserManager;
import projetomps.model.User;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class UserView {
    private static final String LOGIN = "Login: ";

    UserManager userManager = new UserManager();
    Scanner scanner = new Scanner(System.in);
    
    public void getAllUsers(){
        List<User> users = userManager.getAllUsers();
        if (users.isEmpty()) {
            log.info("Nenhum usuario cadastrado.");
        } else {
            users.forEach(user -> log.info("Usuario: {}", user.getLogin()));
        }
    }

    private boolean isValidLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            log.warn("Erro: o login não pode ser vazio.");
            return false;
        }
        if (login.length() > 12) {
            log.warn("Erro: o login deve ter no máximo 12 caracteres.");
            return false;
        }
        if (login.matches(".*\\d.*")) {
            log.warn("Erro: o login não pode conter números.");
            return false;
        }
        return true;
    }

    public void createUser(){
        User newUser = new User();
        log.info(LOGIN);
        String login = scanner.nextLine();
        
        if (!isValidLogin(login)) {
            return;
        }
        newUser.setLogin(login);

        log.info("Senha: ");
        newUser.setPassword(scanner.nextLine());
        
        if(userManager.createUser(newUser)){
            log.info("Usuario criado com sucesso!");
        }else{
            log.warn("Erro ao criar usuario.");
        }
    }

    // public void createUser(){
    //     User newUser = new User();
    //     log.info(LOGIN);
    //     newUser.setLogin(scanner.nextLine());
    //     log.info("Senha: ");
    //     newUser.setPassword(scanner.nextLine());
    //     if(userManager.createUser(newUser)){
    //         log.info("Usuario criado com sucesso!");
    //     }else{
    //         log.warn("Erro ao criar usuario.");
    //     }
    // }

    public void updateUser(){
        User newUser = new User();
        log.info(LOGIN);
        newUser.setLogin(scanner.nextLine());
        log.info("Nova senha: ");
        newUser.setPassword(scanner.nextLine());
        if(userManager.updateUser(newUser)){
            log.info("Usuario atualizado com sucesso!");
        }else{
            log.warn("Erro ao atualizar usuario.");
        }
    }

    public void deleteUser(){
        User user = new User();
        log.info(LOGIN);
        user.setLogin(scanner.nextLine());
        log.info("Senha: ");
        user.setPassword(scanner.nextLine());
        if (userManager.deleetUser(user)){
            log.info("Usuario deletado com sucesso!");
        }else{
            log.warn("Erro ao deletar usuario.");
        }
    }
}
