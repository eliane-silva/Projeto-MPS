package projetomps.business_logic.iterator;

import projetomps.business_logic.model.User;

public interface UserIterator {
    boolean hasNext();
    User next();
}
