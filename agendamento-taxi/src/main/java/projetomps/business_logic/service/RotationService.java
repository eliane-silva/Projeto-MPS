package projetomps.business_logic.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import projetomps.app_logic.dao.RotationDAO;
import projetomps.business_logic.model.Rotation;
import projetomps.util.exception.RepositoryException;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class RotationService {
    private final RotationDAO rotationDAO;

    public List<Rotation> getAllRotations() throws RepositoryException {
        log.info("Buscando todas as rotações");
        return rotationDAO.buscarTodos();
    }

    public Boolean createRotation(Rotation rotation) throws RepositoryException {
        log.info("Criando nova rotação para data: {}", rotation.getDate());

        if (rotation == null) {
            throw new RepositoryException("Rotação não pode ser nula");
        }

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

        // Validar se horário de fim é posterior ao início (se fornecido)
        if (rotation.getEndTime() != null &&
                rotation.getEndTime().isBefore(rotation.getStartTime())) {
            throw new RepositoryException("Horário de fim deve ser posterior ao horário de início");
        }

        // Definir status padrão se não fornecido
        if (rotation.getStatus() == null || rotation.getStatus().trim().isEmpty()) {
            rotation.setStatus("PENDING");
        }

        try {
            Rotation rotacaoSalva = rotationDAO.salvar(rotation);
            return rotacaoSalva != null;
        } catch (RepositoryException e) {
            log.error("Erro ao criar rotação: {}", e.getMessage());
            throw e;
        }
    }

    public Rotation updateRotation(Rotation rotation) throws RepositoryException {
        log.info("Atualizando rotação com ID: {}", rotation.getIdRotation());

        if (rotation == null) {
            throw new RepositoryException("Rotação não pode ser nula");
        }

        if (rotation.getIdRotation() <= 0) {
            throw new RepositoryException("ID da rotação é inválido");
        }

        // Verificar se a rotação existe
        Rotation rotacaoExistente = rotationDAO.buscarPorId(rotation.getIdRotation());
        if (rotacaoExistente == null) {
            throw new RepositoryException("Rotação não encontrada");
        }

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

        // Validar se horário de fim é posterior ao início (se fornecido)
        if (rotation.getEndTime() != null &&
                rotation.getEndTime().isBefore(rotation.getStartTime())) {
            throw new RepositoryException("Horário de fim deve ser posterior ao horário de início");
        }

        // Validar status
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
            return rotationDAO.atualizar(rotation);
        } catch (RepositoryException e) {
            log.error("Erro ao atualizar rotação: {}", e.getMessage());
            throw e;
        }
    }

    public Boolean deleteRotation(Rotation rotation) throws RepositoryException {
        log.info("Deletando rotação com ID: {}", rotation.getIdRotation());

        if (rotation == null) {
            throw new RepositoryException("Rotação não pode ser nula");
        }

        if (rotation.getIdRotation() <= 0) {
            throw new RepositoryException("ID da rotação é inválido");
        }

        // Verificar se a rotação existe
        Rotation rotacaoExistente = rotationDAO.buscarPorId(rotation.getIdRotation());
        if (rotacaoExistente == null) {
            throw new RepositoryException("Rotação não encontrada");
        }

        try {
            return rotationDAO.excluir(rotation.getIdRotation());
        } catch (RepositoryException e) {
            log.error("Erro ao deletar rotação: {}", e.getMessage());
            throw e;
        }
    }

    public Rotation buscarPorId(int id) throws RepositoryException {
        log.info("Buscando rotação por ID: {}", id);

        if (id <= 0) {
            throw new RepositoryException("ID da rotação deve ser um número positivo");
        }

        return rotationDAO.buscarPorId(id);
    }

    public List<Rotation> buscarPorTaxista(int taxistId) throws RepositoryException {
        log.info("Buscando rotações do taxista com ID: {}", taxistId);

        if (taxistId <= 0) {
            throw new RepositoryException("ID do taxista deve ser um número positivo");
        }

        return getAllRotations().stream()
                .filter(r -> r.getTaxist() != null && r.getTaxist().getId() == taxistId)
                .toList();
    }

    public List<Rotation> buscarPorStatus(String status) throws RepositoryException {
        log.info("Buscando rotações com status: {}", status);

        if (status == null || status.trim().isEmpty()) {
            throw new RepositoryException("Status não pode ser vazio");
        }

        String statusUpper = status.toUpperCase();
        if (!statusUpper.equals("PENDING") && !statusUpper.equals("CONFIRMED") && !statusUpper.equals("CANCELLED")) {
            throw new RepositoryException("Status inválido. Use: PENDING, CONFIRMED ou CANCELLED");
        }

        return getAllRotations().stream()
                .filter(r -> statusUpper.equals(r.getStatus()))
                .toList();
    }
}