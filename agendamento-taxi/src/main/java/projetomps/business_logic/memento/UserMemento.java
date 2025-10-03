package projetomps.business_logic.memento;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMemento {
    private final int id;
    private final String login;
    private final String senha;
    private final String tipo; // "ADMIN" ou "TAXIST"
    private final String name;
    private final String email;
}