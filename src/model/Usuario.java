// Caminho: src/main/java/com/sistema/usuario/model/Usuario.java
package com.sistema.usuario.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade Usuario - Representa um usuário do sistema
 * 
 * @author Sistema
 * @version 1.0
 */
public class Usuario {
    private Long id;
    private String login;
    private String senha;
    private String email;
    private String nomeCompleto;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimoLogin;
    private boolean ativo;
    
    // Contador estático para gerar IDs únicos
    private static Long proximoId = 1L;
    
    // Construtor padrão
    public Usuario() {
        this.id = proximoId++;
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
    }
    
    // Construtor com parâmetros principais
    public Usuario(String login, String senha, String email, String nomeCompleto) {
        this();
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.nomeCompleto = nomeCompleto;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNomeCompleto() {
        return nomeCompleto;
    }
    
    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public LocalDateTime getDataUltimoLogin() {
        return dataUltimoLogin;
    }
    
    public void setDataUltimoLogin(LocalDateTime dataUltimoLogin) {
        this.dataUltimoLogin = dataUltimoLogin;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    // Métodos de negócio
    
    /**
     * Valida se a senha fornecida corresponde à senha do usuário
     * 
     * @param senha Senha a ser validada
     * @return true se a senha estiver correta
     */
    public boolean validarSenha(String senha) {
        return this.senha != null && this.senha.equals(senha);
    }
    
    /**
     * Registra o último login do usuário
     */
    public void registrarLogin() {
        this.dataUltimoLogin = LocalDateTime.now();
    }
    
    /**
     * Verifica se o usuário está válido para login
     * 
     * @return true se o usuário estiver ativo e com dados válidos
     */
    public boolean isValidoParaLogin() {
        return ativo && login != null && !login.trim().isEmpty() 
               && senha != null && !senha.trim().isEmpty();
    }
    
    // Métodos equals e hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return Objects.equals(id, usuario.id) || 
               Objects.equals(login, usuario.login);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }
    
    // Método toString
    @Override
    public String toString() {
        return String.format(
            "Usuario{id=%d, login='%s', nomeCompleto='%s', email='%s', ativo=%s, dataCriacao=%s}",
            id, login, nomeCompleto, email, ativo, dataCriacao
        );
    }
    
    /**
     * Retorna uma representação simplificada do usuário para exibição
     * 
     * @return String formatada com informações básicas
     */
    public String toStringSimples() {
        return String.format("ID: %d | Login: %s | Nome: %s | Email: %s | Status: %s",
            id, login, nomeCompleto, email, ativo ? "Ativo" : "Inativo");
    }
}