package projetomps.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String login;
    private String password;
    //Adicionar tipo de usuário? (administrador/taxista) userType: String userTypeId: int
}
