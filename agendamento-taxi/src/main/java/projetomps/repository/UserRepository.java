package projetomps.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import projetomps.model.User;

public class UserRepository { // Essa classe simula o banco de dados do sistema
    private List<User> user = new ArrayList<>();

    public boolean insert(User user){
        return this.user.add(user);
    }

    public boolean delete(User user){
        return this.user.remove(user);
    }

    public List<User> findAll(){
        return this.user;
    }

    // Se for possivel o usuario alterar o login, sera necessario adicionar o id
    public boolean update(User novoUsuario) {
        for (int i = 0; i < user.size(); i++) {
            if (Objects.equals(user.get(i).getLogin(), novoUsuario.getLogin())) {
                user.set(i, novoUsuario);
                return true; // sucesso
            }
        }
        return false; // usuario não encontrado
    }
}
