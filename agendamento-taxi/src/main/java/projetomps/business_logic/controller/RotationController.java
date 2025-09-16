package projetomps.business_logic.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.service.RotationService;
import projetomps.util.exception.RepositoryException;

@AllArgsConstructor
public class RotationController {
    private final RotationService rotationService;

    public List<Rotation> getAllRotations() throws RepositoryException {
        return rotationService.getAllRotations();
    }

    public Boolean createRotation(Rotation rotation) throws RepositoryException {
        return rotationService.createRotation(rotation);
    }

    public Rotation updateRotation(Rotation rotation) throws RepositoryException {
        return rotationService.updateRotation(rotation);
    }

    public Boolean deleteRotation(Rotation rotation) throws RepositoryException {
        return rotationService.deleteRotation(rotation);
    }
}
