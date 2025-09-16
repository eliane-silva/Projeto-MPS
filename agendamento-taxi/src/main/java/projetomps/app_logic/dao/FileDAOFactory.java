package projetomps.app_logic.dao;

public class FileDAOFactory extends DAOFactory {
    public UserDAO getUserDAO() {
        return new FileUserDAO();
    }

    public RotationDAO getRotationDAO() {
        return new FileRotationDAO();
    }
}
