package projetomps.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import projetomps.model.User;

@Slf4j
public class UserRepository { // Essa classe simula o banco de dados do sistema
    private static List<User> user = new ArrayList<>();

    public boolean insert(User user){
        var id = 1;
        if(!UserRepository.user.isEmpty()){
            var lastUser = UserRepository.user.get(UserRepository.user.size()-1);
            id = lastUser.getId() + 1;
        }
        user.setId(id);
        return UserRepository.user.add(user);
    }

    public boolean delete(User user){
        return UserRepository.user.remove(user);
    }

    public List<User> findAll(){
        return UserRepository.user;
    }

    public User findOneById(int id){
        return UserRepository.user.stream().filter(item -> item.getId() == id).findFirst().orElse(null);
    }

    public User findOneByLogin(String login){
        return UserRepository.user.stream().filter(item -> item.getLogin().contentEquals(login)).findFirst().orElse(null);
    }

    // Se for possivel o usuario alterar o login, sera necessario adicionar o id
    public boolean update(User novoUsuario) {
        for (int i = 0; i < UserRepository.user.size(); i++) {
            if (Objects.equals(UserRepository.user.get(i).getId(), novoUsuario.getId())) {
                UserRepository.user.set(i, novoUsuario);
                return true; // sucesso
            }
        }
        return false; // usuario não encontrado
    }
}
