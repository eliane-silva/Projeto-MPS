package projetomps.app_logic.dao;

public abstract class DAOFactory {
    public static final int FILE = 1;
    public static final int MEMORY = 2;

    public abstract UserDAO getUserDAO();

    public abstract RotationDAO getRotationDAO();

    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
            case DAOFactory.FILE:
                return new FileDAOFactory();
            case DAOFactory.MEMORY:
                return new MemoryDAOFactory();
            default:
                return null;
        }
    }
}
