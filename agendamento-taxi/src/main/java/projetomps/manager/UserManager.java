package projetomps.manager;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import projetomps.model.User;
import projetomps.repository.UserRepository;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class UserManager {
    UserRepository userRepository = new UserRepository(); // Classe que vai simular o banco de dados

    // Metodo para listar todos os usuarios para o view
    public List<User> getAllUsers() {
        log.info("Buscando todos os usuarios");
        return userRepository.findAll();
    }

    // Metodo para criar um usuario
    public boolean createUser(User user){
        log.info("Criando usuario: {}", user.getLogin());
        return userRepository.insert(user);
    }

    // Metodo para deletar usuario
    public boolean deleteUser(User user){
        log.info("Deletando usuario: {}", user.getLogin());
        var realUser = userRepository.findOneByLogin(user.getLogin());
        return userRepository.delete(realUser);
    }

    // Metodo para atualizar usuario
    public boolean updateUser(User user){
        log.info("Atualizando usuario: {}", user.getLogin());
        return userRepository.update(user);
    }
}
