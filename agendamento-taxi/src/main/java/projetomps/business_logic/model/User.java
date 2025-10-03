package projetomps.business_logic.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import projetomps.business_logic.memento.UserMemento;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {
    private int id;
    private String login;
    private String senha;

    public User(String login, String senha) {
        this.login = login != null ? login.trim() : null;
        this.senha = senha != null ? senha.trim() : null;
    }

    public boolean isValidUser() {
        return login != null && !login.trim().isEmpty() &&
                senha != null && !senha.trim().isEmpty();
    }

    public void setLogin(String login) {
        this.login = login != null ? login.trim() : null;
    }

    public void setSenha(String senha) {
        this.senha = senha != null ? senha.trim() : null;
    }

    public UserMemento saveToMemento() {
        String tipo = this instanceof Admin ? "ADMIN" : "TAXIST";
        String name = this instanceof Taxist ? ((Taxist) this).getName() : null;
        String email = this instanceof Taxist ? ((Taxist) this).getEmail() : null;

        return new UserMemento(id, login, senha, tipo, name, email);
    }

    public void restoreFromMemento(UserMemento memento) {
        this.id = memento.getId();
        this.login = memento.getLogin();
        this.senha = memento.getSenha();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }
}