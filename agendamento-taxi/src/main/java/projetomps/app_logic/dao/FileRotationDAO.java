package projetomps.app_logic.dao;

import java.util.List;

import projetomps.business_logic.model.Rotation;
import projetomps.util.exception.RepositoryException;

public class FileRotationDAO implements RotationDAO {
    @Override
    public Rotation salvar(Rotation rotation) throws RepositoryException {
        return null;
    }

    @Override
    public Rotation buscarPorId(int id) throws RepositoryException {
        return null;
    }

    @Override
    public List<Rotation> buscarTodos() throws RepositoryException {
        return null;
    }

    @Override
    public Rotation atualizar(Rotation rotation) throws RepositoryException {
        return null;
    }

    @Override
    public boolean excluir(int id) throws RepositoryException {
        return false;
    }
}
