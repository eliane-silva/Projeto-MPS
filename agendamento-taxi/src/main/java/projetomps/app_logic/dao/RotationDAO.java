package projetomps.app_logic.dao;

import java.util.List;

import projetomps.business_logic.model.Rotation;
import projetomps.util.exception.RepositoryException;

public interface RotationDAO {
    public Rotation salvar(Rotation rotation) throws RepositoryException;

    public Rotation buscarPorId(int id) throws RepositoryException;

    public List<Rotation> buscarTodos() throws RepositoryException;

    public Rotation atualizar(Rotation rotation) throws RepositoryException;

    public boolean excluir(int id) throws RepositoryException;
}
