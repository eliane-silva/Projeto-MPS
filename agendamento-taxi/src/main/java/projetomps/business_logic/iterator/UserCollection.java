package projetomps.business_logic.iterator;

import projetomps.business_logic.model.User;
import java.util.List;

public class UserCollection implements UserAggregate {
    private final List<User> users;

    public UserCollection(List<User> users) {
        this.users = users;
    }

    @Override
    public UserIterator createIterator() {
        return new ConcreteUserIterator(users);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }
}