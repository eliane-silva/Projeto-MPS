package projetomps.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String login;
    private String senha;
    
    public User(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }
    
    // Métodos de negócio
    public boolean isValidUser() {
        return login != null && !login.trim().isEmpty() && 
               senha != null && !senha.trim().isEmpty();
    }
    
    public void setLogin(String login) {
        if (login != null) {
            this.login = login.trim();
        } else {
            this.login = login;
        }
    }
    
    public void setSenha(String senha) {
        if (senha != null) {
            this.senha = senha.trim();
        } else {
            this.senha = senha;
        }
    }
}
