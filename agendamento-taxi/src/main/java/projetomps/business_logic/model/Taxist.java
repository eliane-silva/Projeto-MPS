package projetomps.business_logic.model;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Taxist extends User {
    private String name;
    private String email;

    public Taxist(String login, String senha) {
        super(login, senha);
    }

    public Taxist() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Taxist)) return false;
        if (!super.equals(o)) return false;

        Taxist taxist = (Taxist) o;
        return Objects.equals(name, taxist.name) &&
                Objects.equals(email, taxist.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, email);
    }
}