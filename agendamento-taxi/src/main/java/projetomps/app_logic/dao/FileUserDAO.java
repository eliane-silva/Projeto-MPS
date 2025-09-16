package projetomps.app_logic.dao;

import java.util.List;

import projetomps.business_logic.model.User;
import projetomps.util.exception.RepositoryException;

public class FileUserDAO implements UserDAO {
    @Override
    public User salvar(User usuario) throws RepositoryException {
        return null;
    }

    @Override
    public User buscarPorId(int id) throws RepositoryException {
        return null;
    }

    @Override
    public List<User> buscarTodos() throws RepositoryException {
        return null;
    }

    @Override
    public User atualizar(User usuario) throws RepositoryException {
        return null;
    }

    @Override
    public boolean excluir(int id) throws RepositoryException {
        return false;
    }

    @Override
    public User buscarPorLogin(String login) throws RepositoryException {
        return null;
    }
}
