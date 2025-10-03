package projetomps.app_logic.dao;

import java.util.List;
import projetomps.business_logic.model.User;
import projetomps.util.exception.RepositoryException;

public interface UserDAO {
    User salvar(User usuario) throws RepositoryException;
    User buscarPorId(int id) throws RepositoryException;
    List<User> buscarTodos() throws RepositoryException;
    User atualizar(User usuario) throws RepositoryException;
    boolean excluir(int id) throws RepositoryException;
    User buscarPorLogin(String login) throws RepositoryException;
}