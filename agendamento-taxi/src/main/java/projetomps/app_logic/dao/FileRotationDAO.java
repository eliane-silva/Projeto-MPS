package projetomps.app_logic.dao;

import java.util.List;

import projetomps.business_logic.model.Rotation;
import projetomps.util.exception.RepositoryException;

public class FileRotationDAO implements RotationDAO {
    @Override
    public Rotation salvar(Rotation rotation) throws RepositoryException {
        throw new UnsupportedOperationException("FileRotationDAO não implementado");
    }

    @Override
    public Rotation salvarComId(Rotation rotation) throws RepositoryException {
        throw new UnsupportedOperationException("FileRotationDAO não implementado");
    }

    @Override
    public Rotation buscarPorId(int id) throws RepositoryException {
        throw new UnsupportedOperationException("FileRotationDAO não implementado");
    }

    @Override
    public List<Rotation> buscarTodos() throws RepositoryException {
        throw new UnsupportedOperationException("FileRotationDAO não implementado");
    }

    @Override
    public Rotation atualizar(Rotation rotation) throws RepositoryException {
        throw new UnsupportedOperationException("FileRotationDAO não implementado");
    }

    @Override
    public boolean excluir(int id) throws RepositoryException {
        throw new UnsupportedOperationException("FileRotationDAO não implementado");
    }
}