package projetomps.repository;

import projetomps.model.Rotation;
import projetomps.util.exception.RepositoryException;

import java.util.List;

public interface RotationRepository {
    Rotation salvar(Rotation rotation) throws RepositoryException;
    Rotation buscarPorId(int id) throws RepositoryException;
    List<Rotation> buscarTodos() throws RepositoryException;
    boolean excluir(int id) throws RepositoryException;
    Rotation atualizar(Rotation rotation) throws RepositoryException;
}