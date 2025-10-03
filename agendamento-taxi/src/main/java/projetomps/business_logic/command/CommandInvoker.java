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
    private static final int MAX_HISTORY_SIZE = 10; // Limite do histórico

    /**
     * Executa um comando e o adiciona à pilha de undo.
     * @param command O comando a ser executado.
     * @throws RepositoryException se a execução do comando falhar.
     */
    public void execute(Command command) throws RepositoryException {
        try {
            command.execute();
            addToUndoStack(command);
            redoStack.clear(); // Uma nova ação limpa a pilha de redo.
            log.info("Comando executado: {}", command.getClass().getSimpleName());
        } catch (RepositoryException e) {
            log.error("Falha ao executar comando: {}", e, command.getClass().getSimpleName());
            throw e;
        }
    }

    /**
     * Desfaz o último comando executado.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            try {
                command.undo();
                redoStack.push(command);
                log.info("Comando desfeito: {}", command.getClass().getSimpleName());
            } catch (RepositoryException e) {
                log.error("Falha ao desfazer comando: {}", e, command.getClass().getSimpleName());
                // Opcional: Adicionar o comando de volta à pilha de undo se o undo falhar.
                undoStack.push(command);
            }
        } else {
            log.warn("Pilha de undo está vazia. Nenhuma ação para desfazer.");
        }
    }

    /**
     * Refaz o último comando desfeito.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            try {
                command.execute(); // Refazer é simplesmente executar novamente.
                addToUndoStack(command);
                log.info("Comando refeito: {}", command.getClass().getSimpleName());
            } catch (RepositoryException e) {
                log.error("Falha ao refazer comando: {}", e, command.getClass().getSimpleName());
                // Opcional: Adicionar o comando de volta à pilha de redo se o redo falhar.
                redoStack.push(command);
            }
        } else {
            log.warn("Pilha de redo está vazia. Nenhuma ação para refazer.");
        }
    }

    private void addToUndoStack(Command command) {
        if (undoStack.size() >= MAX_HISTORY_SIZE) {
            undoStack.removeLast(); // Remove o comando mais antigo.
        }
        undoStack.push(command);
    }
}