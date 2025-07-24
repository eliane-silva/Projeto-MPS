package controller;

import model.Usuario;
import com.sistema.usuario.service.UsuarioService;
import com.sistema.usuario.exception.UsuarioException;
import java.util.List;
import java.util.Optional;

/**
 * Controller para gerenciar operações de usuários
 * Coordena as interações entre View e Service
 * 
 * @author Sistema
 * @version 1.0
 */
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    /**
     * Construtor que inicializa o service
     */
    public UsuarioController() {
        this.usuarioService = new UsuarioService();
    }
    
    /**
     * Construtor para injeção de dependência (útil para testes)
     * 
     * @param usuarioService Service a ser usado
     */
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    /**
     * Adiciona um novo usuário
     * 
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @param email Email do usuário
     * @param nomeCompleto Nome completo do usuário
     * @return Resultado da operação
     */
    public ResultadoOperacao adicionarUsuario(String login, String senha, String email, String nomeCompleto) {
        try {
            // Trim nos dados de entrada
            login = login != null ? login.trim() : null;
            email = email != null ? email.trim() : null;
            nomeCompleto = nomeCompleto != null ? nomeCompleto.trim() : null;
            
            Usuario usuario = usuarioService.adicionarUsuario(login, senha, email, nomeCompleto);
            
            return new ResultadoOperacao(
                true, 
                "Usuário adicionado com sucesso!", 
                "ID: " + usuario.getId() + " | Login: " + usuario.getLogin()
            );
            
        } catch (UsuarioException e) {
            return new ResultadoOperacao(false, "Erro ao adicionar usuário", e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacao(false, "Erro interno do sistema", 
                "Erro inesperado: " + e.getMessage());
        }
    }
    
    /**
     * Lista todos os usuários
     * 
     * @return Lista de usuários e resultado da operação
     */
    public ResultadoListagem listarTodosUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
            
            if (usuarios.isEmpty()) {
                return new ResultadoListagem(true, "Nenhum usuário cadastrado", usuarios);
            }
            
            return new ResultadoListagem(true, 
                "Encontrados " + usuarios.size() + " usuário(s)", usuarios);
            
        } catch (Exception e) {
            return new ResultadoListagem(false, "Erro ao listar usuários: " + e.getMessage(), null);
        }
    }
    
    /**
     * Lista apenas usuários ativos
     * 
     * @return Lista de usuários ativos e resultado da operação
     */
    public ResultadoListagem listarUsuariosAtivos() {
        try {
            List<Usuario> usuarios = usuarioService.listarUsuariosAtivos();
            
            if (usuarios.isEmpty()) {
                return new ResultadoListagem(true, "Nenhum usuário ativo encontrado", usuarios);
            }
            
            return new ResultadoListagem(true, 
                "Encontrados " + usuarios.size() + " usuário(s) ativo(s)", usuarios);
            
        } catch (Exception e) {
            return new ResultadoListagem(false, "Erro ao listar usuários ativos: " + e.getMessage(), null);
        }
    }
    
    /**
     * Busca um usuário por ID
     * 
     * @param id ID do usuário
     * @return Resultado da busca
     */
    public ResultadoBusca buscarUsuarioPorId(Long id) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorId(id);
            
            if (usuarioOpt.isPresent()) {
                return new ResultadoBusca(true, "Usuário encontrado", usuarioOpt.get());
            } else {
                return new ResultadoBusca(false, "Usuário não encontrado com ID: " + id, null);
            }
            
        } catch (Exception e) {
            return new ResultadoBusca(false, "Erro ao buscar usuário: " + e.getMessage(), null);
        }
    }
    
    /**
     * Busca um usuário por login
     * 
     * @param login Login do usuário
     * @return Resultado da busca
     */
    public ResultadoBusca buscarUsuarioPorLogin(String login) {
        try {
            if (login == null || login.trim().isEmpty()) {
                return new ResultadoBusca(false, "Login não pode ser vazio", null);
            }
            
            Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorLogin(login.trim());
            
            if (usuarioOpt.isPresent()) {
                return new ResultadoBusca(true, "Usuário encontrado", usuarioOpt.get());
            } else {
                return new ResultadoBusca(false, "Usuário não encontrado com login: " + login, null);
            }
            
        } catch (Exception e) {
            return new ResultadoBusca(false, "Erro ao buscar usuário: " + e.getMessage(), null);
        }
    }
    
    /**
     * Autentica um usuário
     * 
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @return Resultado da autenticação
     */
    public ResultadoAutenticacao autenticarUsuario(String login, String senha) {
        try {
            if (login == null || login.trim().isEmpty()) {
                return new ResultadoAutenticacao(false, "Login é obrigatório", null);
            }
            
            if (senha == null || senha.isEmpty()) {
                return new ResultadoAutenticacao(false, "Senha é obrigatória", null);
            }
            
            Optional<Usuario> usuarioOpt = usuarioService.autenticarUsuario(login.trim(), senha);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                return new ResultadoAutenticacao(true, 
                    "Autenticação realizada com sucesso! Bem-vindo, " + usuario.getNomeCompleto(), 
                    usuario);
            } else {
                return new ResultadoAutenticacao(false, "Login ou senha inválidos", null);
            }
            
        } catch (Exception e) {
            return new ResultadoAutenticacao(false, "Erro na autenticação: " + e.getMessage(), null);
        }
    }
    
    /**
     * Remove um usuário
     * 
     * @param id ID do usuário a ser removido
     * @return Resultado da operação
     */
    public ResultadoOperacao removerUsuario(Long id) {
        try {
            if (id == null || id <= 0) {
                return new ResultadoOperacao(false, "ID inválido", "ID deve ser um número positivo");
            }
            
            // Busca o usuário antes de remover para mostrar informações
            Optional<Usuario> usuarioOpt = usuarioService.buscarUsuarioPorId(id);
            if (!usuarioOpt.isPresent()) {
                return new ResultadoOperacao(false, "Usuário não encontrado", "ID: " + id);
            }
            
            Usuario usuario = usuarioOpt.get();
            boolean removido = usuarioService.removerUsuario(id);
            
            if (removido) {
                return new ResultadoOperacao(true, "Usuário removido com sucesso", 
                    "Login: " + usuario.getLogin());
            } else {
                return new ResultadoOperacao(false, "Falha ao remover usuário", "ID: " + id);
            }
            
        } catch (UsuarioException e) {
            return new ResultadoOperacao(false, "Erro ao remover usuário", e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacao(false, "Erro interno do sistema", 
                "Erro inesperado: " + e.getMessage());
        }
    }
    
    /**
     * Obtém estatísticas do sistema
     * 
     * @return Estatísticas formatadas
     */
    public String obterEstatisticas() {
        try {
            return usuarioService.obterEstatisticas();
        } catch (Exception e) {
            return "Erro ao obter estatísticas: " + e.getMessage();
        }
    }
    
    /**
     * Verifica se um login está disponível
     * 
     * @param login Login a ser verificado
     * @return true se disponível
     */
    public boolean isLoginDisponivel(String login) {
        try {
            return usuarioService.isLoginDisponivel(login);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Verifica se um email está disponível
     * 
     * @param email Email a ser verificado
     * @return true se disponível
     */
    public boolean isEmailDisponivel(String email) {
        try {
            return usuarioService.isEmailDisponivel(email);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Classes internas para resultados
    
    /**
     * Resultado de operações básicas
     */
    public static class ResultadoOperacao {
        private final boolean sucesso;
        private final String mensagem;
        private final String detalhes;
        
        public ResultadoOperacao(boolean sucesso, String mensagem, String detalhes) {
            this.sucesso = sucesso;
            this.mensagem = mensagem;
            this.detalhes = detalhes;
        }
        
        public boolean isSucesso() { return sucesso; }
        public String getMensagem() { return mensagem; }
        public String getDetalhes() { return detalhes; }
    }
    
    /**
     * Resultado de operações de listagem
     */
    public static class ResultadoListagem {
        private final boolean sucesso;
        private final String mensagem;
        private final List<Usuario> usuarios;
        
        public ResultadoListagem(boolean sucesso, String mensagem, List<Usuario> usuarios) {
            this.sucesso = sucesso;
            this.mensagem = mensagem;
            this.usuarios = usuarios;
        }
        
        public boolean isSucesso() { return sucesso; }
        public String getMensagem() { return mensagem; }
        public List<Usuario> getUsuarios() { return usuarios; }
    }
    
    /**
     * Resultado de operações de busca
     */
    public static class ResultadoBusca {
        private final boolean sucesso;
        private final String mensagem;
        private final Usuario usuario;
        
        public ResultadoBusca(boolean sucesso, String mensagem, Usuario usuario) {
            this.sucesso = sucesso;
            this.mensagem = mensagem;
            this.usuario = usuario;
        }
        
        public boolean isSucesso() { return sucesso; }
        public String getMensagem() { return mensagem; }
        public Usuario getUsuario() { return usuario; }
    }
    
    /**
     * Resultado de operações de autenticação
     */
    public static class ResultadoAutenticacao {
        private final boolean sucesso;
        private final String mensagem;
        private final Usuario usuario;
        
        public ResultadoAutenticacao(boolean sucesso, String mensagem, Usuario usuario) {
            this.sucesso = sucesso;
            this.mensagem = mensagem;
            this.usuario = usuario;
        }
        
        public boolean isSucesso() { return sucesso; }
        public String getMensagem() { return mensagem; }
        public Usuario getUsuario() { return usuario; }
    }
}