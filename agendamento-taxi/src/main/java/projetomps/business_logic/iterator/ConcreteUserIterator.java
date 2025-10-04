package projetomps.business_logic.iterator;

import projetomps.business_logic.model.User;
import java.util.List;

public class ConcreteUserIterator implements UserIterator {
    private final List<User> users;
    private int position = 0;

    public ConcreteUserIterator(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean hasNext() {
        return position < users.size();
    }

    @Override
    public User next() {
        if (!hasNext()) {
            throw new IllegalStateException("Não há mais usuários na lista");
        }
        return users.get(position++);
    }
}
