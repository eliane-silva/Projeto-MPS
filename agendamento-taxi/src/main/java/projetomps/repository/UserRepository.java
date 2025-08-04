package projetomps.repository;

import projetomps.model.User;
import projetomps.util.exception.RepositoryException;
import java.util.List;

public interface UserRepository {
    User salvar(User usuario) throws RepositoryException;
    User buscarPorId(int id) throws RepositoryException;
    List<User> buscarTodos() throws RepositoryException;
    User buscarPorLogin(String login) throws RepositoryException;
    boolean excluir(int id) throws RepositoryException;
    User atualizar(User usuario) throws RepositoryException;
}