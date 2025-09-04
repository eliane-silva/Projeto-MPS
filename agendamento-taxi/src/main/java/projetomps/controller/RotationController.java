package projetomps.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import projetomps.model.Rotation;
import projetomps.service.RotationService;
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
