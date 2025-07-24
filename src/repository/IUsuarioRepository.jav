// Caminho: src/main/java/com/sistema/usuario/repository/IUsuarioRepository.java
package com.sistema.usuario.repository;

import com.sistema.usuario.model.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Interface para operações de persistência de usuários
 * Define o contrato para acesso aos dados de usuários
 * 
 * @author Sistema
 * @version 1.0
 */
public interface IUsuarioRepository {
    
    /**
     * Salva um novo usuário no repositório
     * 
     * @param usuario Usuário a ser salvo
     * @return Usuario salvo com ID gerado
     * @throws IllegalArgumentException se o usuário for inválido
     */
    Usuario salvar(Usuario usuario);
    
    /**
     * Atualiza um usuário existente
     * 
     * @param usuario Usuário a ser atualizado
     * @return Usuario atualizado
     * @throws IllegalArgumentException se o usuário não existir
     */
    Usuario atualizar(Usuario usuario);
    
    /**
     * Remove um usuário do repositório
     * 
     * @param id ID do usuário a ser removido
     * @return true se o usuário foi removido com sucesso
     */
    boolean remover(Long id);
    
    /**
     * Busca um usuário por ID
     * 
     * @param id ID do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<Usuario> buscarPorId(Long id);
    
    /**
     * Busca um usuário por login
     * 
     * @param login Login do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<Usuario> buscarPorLogin(String login);
    
    /**
     * Busca um usuário por email
     * 
     * @param email Email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<Usuario> buscarPorEmail(String email);
    
    /**
     * Lista todos os usuários
     * 
     * @return Lista com todos os usuários
     */
    List<Usuario> listarTodos();
    
    /**
     * Lista apenas usuários ativos
     * 
     * @return Lista com usuários ativos
     */
    List<Usuario> listarAtivos();
    
    /**
     * Verifica se existe um usuário com o login especificado
     * 
     * @param login Login a ser verificado
     * @return true se existir usuário com o login
     */
    boolean existeLogin(String login);
    
    /**
     * Verifica se existe um usuário com o email especificado
     * 
     * @param email Email a ser verificado
     * @return true se existir usuário com o email
     */
    boolean existeEmail(String email);
    
    /**
     * Conta o número total de usuários
     * 
     * @return Número total de usuários
     */
    long contar();
    
    /**
     * Conta o número de usuários ativos
     * 
     * @return Número de usuários ativos
     */
    long contarAtivos();
    
    /**
     * Limpa todos os usuários do repositório
     * CUIDADO: Esta operação remove todos os dados!
     */
    void limparTodos();
}