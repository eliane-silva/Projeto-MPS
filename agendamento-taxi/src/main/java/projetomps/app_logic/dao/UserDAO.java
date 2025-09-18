package projetomps.app_logic.dao;

import java.util.List;

import projetomps.business_logic.model.User;
import projetomps.util.exception.RepositoryException;

public interface UserDAO {
    public User salvar(User usuario) throws RepositoryException;

    public User buscarPorId(int id) throws RepositoryException;

    public List<User> buscarTodos() throws RepositoryException;

    public User atualizar(User usuario) throws RepositoryException;

    public boolean excluir(int id) throws RepositoryException;

    public User buscarPorLogin(String login) throws RepositoryException;
}
