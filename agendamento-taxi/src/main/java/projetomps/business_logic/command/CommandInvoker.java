package projetomps.business_logic.command;

import projetomps.app_logic.log.AppLogger;
import projetomps.app_logic.log.AppLoggerFactory;
import projetomps.util.exception.RepositoryException;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * O Invoker é responsável por executar comandos e gerenciar o histórico de undo/redo.
 */
public class CommandInvoker {
    private static final AppLogger log = AppLoggerFactory.getLogger(CommandInvoker.class);
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();
    private static final int MAX_HISTORY_SIZE = 10;

    public void execute(Command command) throws RepositoryException {
        try {
            command.execute();
            addToUndoStack(command);
            redoStack.clear();
            log.info("Comando executado: {}", command.getClass().getSimpleName());
        } catch (RepositoryException e) {
            log.error("Falha ao executar comando: {}", e, command.getClass().getSimpleName());
            throw e;
        }
    }

    /**
     * Desfaz o último comando executado.
     * @return true se conseguiu desfazer, false se não havia comandos
     */
    public boolean undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            try {
                command.undo();
                redoStack.push(command);
                log.info("Comando desfeito: {}", command.getClass().getSimpleName());
                return true;
            } catch (RepositoryException e) {
                log.error("Falha ao desfazer comando: {}", e, command.getClass().getSimpleName());
                undoStack.push(command);
                return false;
            }
        } else {
            log.warn("Pilha de undo está vazia. Nenhuma ação para desfazer.");
            return false;
        }
    }

    /**
     * Refaz o último comando desfeito.
     * @return true se conseguiu refazer, false se não havia comandos
     */
    public boolean redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            try {
                command.execute();
                addToUndoStack(command);
                log.info("Comando refeito: {}", command.getClass().getSimpleName());
                return true;
            } catch (RepositoryException e) {
                log.error("Falha ao refazer comando: {}", e, command.getClass().getSimpleName());
                redoStack.push(command);
                return false;
            }
        } else {
            log.warn("Pilha de redo está vazia. Nenhuma ação para refazer.");
            return false;
        }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    private void addToUndoStack(Command command) {
        if (undoStack.size() >= MAX_HISTORY_SIZE) {
            undoStack.removeLast();
        }
        undoStack.push(command);
    }
}