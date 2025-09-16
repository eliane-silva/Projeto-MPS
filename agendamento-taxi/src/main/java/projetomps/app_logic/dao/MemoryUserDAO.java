package projetomps.app_logic.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import projetomps.business_logic.model.User;
import projetomps.util.exception.RepositoryException;

@Slf4j
public class MemoryUserDAO implements UserDAO {
    private final List<User> users = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    
    @Override
    public User salvar(User usuario) throws RepositoryException {
        try {
            if (usuario == null) {
                throw new RepositoryException("Usuário não pode ser nulo");
            }
            
            // Verifica se já existe usuário com o mesmo login
            if (buscarPorLogin(usuario.getLogin()) != null) {
                throw new RepositoryException("Já existe um usuário com este login");
            }
            
            usuario.setId(idGenerator.getAndIncrement());
            users.add(usuario);
            log.info("Usuário salvo com ID: {}", usuario.getId());
            return usuario;
        } catch (Exception e) {
            if (e instanceof RepositoryException) {
                throw e;
            }
            throw new RepositoryException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }
    
    @Override
    public User buscarPorId(int id) throws RepositoryException {
        try {
            return users.stream()
                    .filter(user -> user.getId() == id)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new RepositoryException("Erro ao buscar usuário por ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<User> buscarTodos() throws RepositoryException {
        try {
            return new ArrayList<>(users);
        } catch (Exception e) {
            throw new RepositoryException("Erro ao buscar todos os usuários: " + e.getMessage(), e);
        }
    }
    
    @Override
    public User buscarPorLogin(String login) throws RepositoryException {
        try {
            if (login == null || login.trim().isEmpty()) {
                return null;
            }
            
            return users.stream()
                    .filter(user -> user.getLogin().equals(login.trim()))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new RepositoryException("Erro ao buscar usuário por login: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean excluir(int id) throws RepositoryException {
        try {
            return users.removeIf(user -> user.getId() == id);
        } catch (Exception e) {
            throw new RepositoryException("Erro ao excluir usuário: " + e.getMessage(), e);
        }
    }
    
    @Override
    public User atualizar(User usuario) throws RepositoryException {
        try {
            if (usuario == null) {
                throw new RepositoryException("Usuário não pode ser nulo");
            }
            
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId() == usuario.getId()) {
                    users.set(i, usuario);
                    log.info("Usuário atualizado com ID: {}", usuario.getId());
                    return usuario;
                }
            }
            throw new RepositoryException("Usuário não encontrado para atualização");
        } catch (Exception e) {
            if (e instanceof RepositoryException) {
                throw e;
            }
            throw new RepositoryException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }
}
