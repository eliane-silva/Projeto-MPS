package projetomps.business_logic.command;

import projetomps.util.exception.RepositoryException;

/**
 * Interface que define o contrato para todos os comandos.
 */
public interface Command {
    /**
     * Executa a operação do comando.
     * @throws RepositoryException se ocorrer um erro durante a execução.
     */
    void execute() throws RepositoryException;

    /**
     * Desfaz a operação previamente executada.
     * @throws RepositoryException se ocorrer um erro ao desfazer.
     */
    void undo() throws RepositoryException;
}