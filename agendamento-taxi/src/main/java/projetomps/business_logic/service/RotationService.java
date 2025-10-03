package projetomps.business_logic.service;

import java.util.List;

import lombok.AllArgsConstructor;
import projetomps.app_logic.dao.RotationDAO;
import projetomps.app_logic.log.AppLogger;
import projetomps.app_logic.log.AppLoggerFactory;
import projetomps.business_logic.model.Rotation;
import projetomps.util.exception.RepositoryException;

@AllArgsConstructor
public class RotationService {
    private static final AppLogger log =
            AppLoggerFactory.getLogger(RotationService.class);

    private final RotationDAO rotationDAO;

    public List<Rotation> getAllRotations() throws RepositoryException {
        log.info("Buscando todas as rotações");
        return rotationDAO.buscarTodos();
    }

    public Boolean createRotation(Rotation rotation) throws RepositoryException {
        if (rotation == null) {
            throw new RepositoryException("Rotação não pode ser nula");
        }
        log.info("Criando nova rotação para data: {}", rotation.getDate());

        // Validações básicas
        if (rotation.getDate() == null) {
            throw new RepositoryException("Data da rotação é obrigatória");
        }
        if (rotation.getStartTime() == null) {
            throw new RepositoryException("Horário de início é obrigatório");
        }
        if (rotation.getTaxist() == null) {
            throw new RepositoryException("Taxista é obrigatório");
        }
        if (rotation.getEndTime() != null &&
                rotation.getEndTime().isBefore(rotation.getStartTime())) {
            throw new RepositoryException("Horário de fim deve ser posterior ao horário de início");
        }

        if (rotation.getStatus() == null || rotation.getStatus().trim().isEmpty()) {
            rotation.setStatus("PENDING");
        }

        try {
            Rotation rotacaoSalva = rotationDAO.salvar(rotation);
            log.info("Rotação criada com ID: {}", rotacaoSalva.getIdRotation());
            return rotacaoSalva != null;
        } catch (RepositoryException e) {
            log.error("Erro ao criar rotação", e);
            throw e;
        }
    }

    public Rotation createRotationComId(Rotation rotation) throws RepositoryException {
        if (rotation == null) {
            throw new RepositoryException("Rotação não pode ser nula");
        }
        log.info("Criando rotação com ID preservado: {}", rotation.getIdRotation());

        // Validações básicas
        if (rotation.getDate() == null) {
            throw new RepositoryException("Data da rotação é obrigatória");
        }
        if (rotation.getStartTime() == null) {
            throw new RepositoryException("Horário de início é obrigatório");
        }
        if (rotation.getTaxist() == null) {
            throw new RepositoryException("Taxista é obrigatório");
        }
        if (rotation.getEndTime() != null &&
                rotation.getEndTime().isBefore(rotation.getStartTime())) {
            throw new RepositoryException("Horário de fim deve ser posterior ao horário de início");
        }

        if (rotation.getStatus() == null || rotation.getStatus().trim().isEmpty()) {
            rotation.setStatus("PENDING");
        }

        try {
            Rotation rotacaoSalva = rotationDAO.salvarComId(rotation);
            log.info("Rotação criada com ID preservado: {}", rotacaoSalva.getIdRotation());
            return rotacaoSalva;
        } catch (RepositoryException e) {
            log.error("Erro ao criar rotação com ID", e);
            throw e;
        }
    }

    public Rotation updateRotation(Rotation rotation) throws RepositoryException {
        if (rotation == null) {
            throw new RepositoryException("Rotação não pode ser nula");
        }
        log.info("Atualizando rotação com ID: {}", rotation.getIdRotation());

        if (rotation.getIdRotation() <= 0) {
            throw new RepositoryException("ID da rotação é inválido");
        }

        Rotation rotacaoExistente = rotationDAO.buscarPorId(rotation.getIdRotation());
        if (rotacaoExistente == null) {
            throw new RepositoryException("Rotação não encontrada");
        }

        if (rotation.getDate() == null) {
            throw new RepositoryException("Data da rotação é obrigatória");
        }
        if (rotation.getStartTime() == null) {
            throw new RepositoryException("Horário de início é obrigatório");
        }
        if (rotation.getTaxist() == null) {
            throw new RepositoryException("Taxista é obrigatório");
        }
        if (rotation.getEndTime() != null &&
                rotation.getEndTime().isBefore(rotation.getStartTime())) {
            throw new RepositoryException("Horário de fim deve ser posterior ao horário de início");
        }

        if (rotation.getStatus() == null || rotation.getStatus().trim().isEmpty()) {
            rotation.setStatus("PENDING");
        } else {
            String status = rotation.getStatus().toUpperCase();
            if (!status.equals("PENDING") && !status.equals("CONFIRMED") && !status.equals("CANCELLED")) {
                throw new RepositoryException("Status inválido. Use: PENDING, CONFIRMED ou CANCELLED");
            }
            rotation.setStatus(status);
        }

        try {
            Rotation atualizado = rotationDAO.atualizar(rotation);
            log.info("Rotação atualizada: {}", atualizado.getIdRotation());
            return atualizado;
        } catch (RepositoryException e) {
            log.error("Erro ao atualizar rotação", e);
            throw e;
        }
    }

    public Boolean deleteRotation(Rotation rotation) throws RepositoryException {
        if (rotation == null) {
            throw new RepositoryException("Rotação não pode ser nula");
        }
        log.info("Deletando rotação com ID: {}", rotation.getIdRotation());

        if (rotation.getIdRotation() <= 0) {
            throw new RepositoryException("ID da rotação é inválido");
        }

        Rotation rotacaoExistente = rotationDAO.buscarPorId(rotation.getIdRotation());
        if (rotacaoExistente == null) {
            throw new RepositoryException("Rotação não encontrada");
        }

        try {
            boolean ok = rotationDAO.excluir(rotation.getIdRotation());
            if (ok) log.info("Rotação deletada: {}", rotation.getIdRotation());
            return ok;
        } catch (RepositoryException e) {
            log.error("Erro ao deletar rotação", e);
            throw e;
        }
    }

    public Rotation buscarPorId(int id) throws RepositoryException {
        if (id <= 0) {
            throw new RepositoryException("ID da rotação deve ser um número positivo");
        }
        log.info("Buscando rotação por ID: {}", id);
        return rotationDAO.buscarPorId(id);
    }

    public List<Rotation> buscarPorTaxista(int taxistId) throws RepositoryException {
        if (taxistId <= 0) {
            throw new RepositoryException("ID do taxista deve ser um número positivo");
        }
        log.info("Buscando rotações do taxista com ID: {}", taxistId);
        return getAllRotations().stream()
                .filter(r -> r.getTaxist() != null && r.getTaxist().getId() == taxistId)
                .toList();
    }

    public List<Rotation> buscarPorStatus(String status) throws RepositoryException {
        if (status == null || status.trim().isEmpty()) {
            throw new RepositoryException("Status não pode ser vazio");
        }
        String statusUpper = status.toUpperCase();
        if (!statusUpper.equals("PENDING") && !statusUpper.equals("CONFIRMED") && !statusUpper.equals("CANCELLED")) {
            throw new RepositoryException("Status inválido. Use: PENDING, CONFIRMED ou CANCELLED");
        }
        log.info("Buscando rotações com status: {}", statusUpper);
        return getAllRotations().stream()
                .filter(r -> statusUpper.equals(r.getStatus()))
                .toList();
    }
}