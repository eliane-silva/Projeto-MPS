package projetomps.business_logic.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.service.RotationService;
import projetomps.business_logic.service.UserService;
import projetomps.util.exception.RepositoryException;
import projetomps.business_logic.command.CommandInvoker;
import projetomps.business_logic.command.CreateRotationCommand;
import projetomps.business_logic.command.UpdateRotationCommand;
import projetomps.business_logic.command.DeleteRotationCommand;

@AllArgsConstructor
public class RotationController {
    private final RotationService rotationService;
    private final UserService userService;
    private final CommandInvoker invoker;

    public List<Rotation> getAllRotations() throws RepositoryException {
        return rotationService.getAllRotations();
    }

    public Boolean createRotation(Rotation rotation) {
        try {
            CreateRotationCommand command = new CreateRotationCommand(rotationService, rotation);
            invoker.execute(command);
            return true;
        } catch (RepositoryException e) {
            return false;
        }
    }

    public Rotation updateRotation(Rotation rotation) {
        try {
            UpdateRotationCommand command = new UpdateRotationCommand(
                    rotationService, userService, rotation);
            invoker.execute(command);
            return rotation;
        } catch (RepositoryException e) {
            return null;
        }
    }

    public Boolean deleteRotation(Rotation rotation) {
        try {
            DeleteRotationCommand command = new DeleteRotationCommand(
                    rotationService, userService, rotation);
            invoker.execute(command);
            return true;
        } catch (RepositoryException e) {
            return false;
        }
    }

    public boolean undo() {
        return invoker.undo();
    }

    public boolean redo() {
        return invoker.redo();
    }

    public boolean canUndo() {
        return invoker.canUndo();
    }

    public boolean canRedo() {
        return invoker.canRedo();
    }
}