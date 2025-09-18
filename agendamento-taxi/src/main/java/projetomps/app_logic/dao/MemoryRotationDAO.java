package projetomps.app_logic.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import projetomps.app_logic.log.AppLogger;
import projetomps.app_logic.log.AppLoggerFactory;
import projetomps.business_logic.model.Rotation;
import projetomps.util.exception.RepositoryException;

public class MemoryRotationDAO implements RotationDAO {
    private static final AppLogger log =
            AppLoggerFactory.getLogger(MemoryRotationDAO.class);

    private final List<Rotation> rotations = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public Rotation salvar(Rotation rotation) throws RepositoryException {
        try {
            if (rotation == null) {
                throw new RepositoryException("Rotação não pode ser nula");
            }
            rotation.setIdRotation(idGenerator.getAndIncrement());
            rotations.add(rotation);
            log.info("Rotação salva com ID: {}", rotation.getIdRotation());
            return rotation;
        } catch (Exception e) {
            if (e instanceof RepositoryException) throw e;
            log.error("Erro ao salvar rotação", e);
            throw new RepositoryException("Erro ao salvar rotação: " + e.getMessage(), e);
        }
    }

    @Override
    public Rotation buscarPorId(int id) throws RepositoryException {
        try {
            return rotations.stream()
                    .filter(r -> r.getIdRotation() == id)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            log.error("Erro ao buscar rotação por ID: {}", e, id);
            throw new RepositoryException("Erro ao buscar rotação por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Rotation> buscarTodos() throws RepositoryException {
        try {
            return new ArrayList<>(rotations);
        } catch (Exception e) {
            log.error("Erro ao buscar todas as rotações", e);
            throw new RepositoryException("Erro ao buscar todas as rotações: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean excluir(int id) throws RepositoryException {
        try {
            boolean removed = rotations.removeIf(r -> r.getIdRotation() == id);
            if (removed) log.info("Rotação excluída: {}", id);
            return removed;
        } catch (Exception e) {
            log.error("Erro ao excluir rotação: {}", e, id);
            throw new RepositoryException("Erro ao excluir rotação: " + e.getMessage(), e);
        }
    }

    @Override
    public Rotation atualizar(Rotation rotation) throws RepositoryException {
        try {
            if (rotation == null) {
                throw new RepositoryException("Rotação não pode ser nula");
            }
            for (int i = 0; i < rotations.size(); i++) {
                if (rotations.get(i).getIdRotation() == rotation.getIdRotation()) {
                    rotations.set(i, rotation);
                    log.info("Rotação atualizada com ID: {}", rotation.getIdRotation());
                    return rotation;
                }
            }
            throw new RepositoryException("Rotação não encontrada para atualização");
        } catch (Exception e) {
            if (e instanceof RepositoryException) throw e;
            log.error("Erro ao atualizar rotação", e);
            throw new RepositoryException("Erro ao atualizar rotação: " + e.getMessage(), e);
        }
    }
}
