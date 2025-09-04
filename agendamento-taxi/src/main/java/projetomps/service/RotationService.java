package projetomps.service;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import projetomps.model.Rotation;
import projetomps.repository.RotationRepository;
import projetomps.util.exception.RepositoryException;

@Slf4j
@AllArgsConstructor
public class RotationService {
    private final RotationRepository rotationRepository;

    public List<Rotation> getAllRotations() throws RepositoryException {
        log.info("Buscando todas as rotações");
        return rotationRepository.buscarTodos();
    }

    public boolean deleteRotation(Rotation rotation) throws RepositoryException {
        log.info("Deletando rotação com ID: {}", rotation.getIdRotation());
        return rotationRepository.excluir(rotation.getIdRotation());
    }

    public Rotation updateRotation(Rotation rotation) throws RepositoryException {
        log.info("Atualizando rotação com ID: {}", rotation.getIdRotation());
        validarRotation(rotation);
        return rotationRepository.atualizar(rotation);
    }

    public boolean createRotation(Rotation rotation) throws RepositoryException {
        log.info("Criando nova rotação para o taxista: {}",
                rotation.getTaxist() != null ? rotation.getTaxist().getLogin() : "desconhecido");

        validarRotation(rotation);
        return rotationRepository.salvar(rotation) != null;
    }

    public Rotation buscarPorId(int id) throws RepositoryException {
        log.info("Buscando rotação por ID: {}", id);
        return rotationRepository.buscarPorId(id);
    }

    private void validarRotation(Rotation rotation) throws RepositoryException {
        if (rotation.getDate() == null) {
            throw new RepositoryException("Data da rotação não pode ser nula");
        }

        if (rotation.getDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new RepositoryException("Rotação deve ser agendada para uma data futura");
        }

        if (rotation.getStartTime() == null) {
            throw new RepositoryException("Hora do início da rotação não pode ser nula");
        }

        if (rotation.getStatus() == null || rotation.getStatus().trim().isEmpty()) {
            rotation.setStatus("PENDING"); // Status padrão
        }

        if (rotation.getTaxist() == null) {
            throw new RepositoryException("É necessário vincular um taxista à rotação");
        }
    }
}