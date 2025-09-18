package projetomps.business_logic.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(String login, String senha) {
        super(login, senha);
    }

    public Admin(int id, String login, String senha) {
        super(id, login, senha);
    }
}
