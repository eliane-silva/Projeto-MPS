package projetomps.app_logic.dao;

public class MemoryDAOFactory extends DAOFactory {
    public UserDAO getUserDAO() {
        return new MemoryUserDAO();
    }

    public RotationDAO getRotationDAO() {
        return new MemoryRotationDAO();
    }
}
