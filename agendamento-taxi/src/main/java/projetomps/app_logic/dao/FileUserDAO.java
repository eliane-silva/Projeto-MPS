package projetomps.app_logic.dao;

import java.util.List;

import projetomps.business_logic.model.User;
import projetomps.util.exception.RepositoryException;

public class FileUserDAO implements UserDAO {
    @Override
    public User salvar(User usuario) throws RepositoryException {
        throw new UnsupportedOperationException("FileUserDAO não implementado");
    }

    @Override
    public User buscarPorId(int id) throws RepositoryException {
        throw new UnsupportedOperationException("FileUserDAO não implementado");
    }

    @Override
    public List<User> buscarTodos() throws RepositoryException {
        throw new UnsupportedOperationException("FileUserDAO não implementado");
    }

    @Override
    public User atualizar(User usuario) throws RepositoryException {
        throw new UnsupportedOperationException("FileUserDAO não implementado");
    }

    @Override
    public boolean excluir(int id) throws RepositoryException {
        throw new UnsupportedOperationException("FileUserDAO não implementado");
    }

    @Override
    public User buscarPorLogin(String login) throws RepositoryException {
        throw new UnsupportedOperationException("FileUserDAO não implementado");
    }
}