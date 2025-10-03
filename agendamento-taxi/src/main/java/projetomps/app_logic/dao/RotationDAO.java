package projetomps.app_logic.dao;

import java.util.List;
import projetomps.business_logic.model.Rotation;
import projetomps.util.exception.RepositoryException;

public interface RotationDAO {
    Rotation salvar(Rotation rotation) throws RepositoryException;
    Rotation salvarComId(Rotation rotation) throws RepositoryException;
    Rotation buscarPorId(int id) throws RepositoryException;
    List<Rotation> buscarTodos() throws RepositoryException;
    Rotation atualizar(Rotation rotation) throws RepositoryException;
    boolean excluir(int id) throws RepositoryException;
}