package projetomps.view;

import projetomps.controller.UserController;
import projetomps.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Scanner;

@Slf4j
@AllArgsConstructor
public class UserView {
    private final UserController userController;
    private final Scanner scanner;
    
    public void exibirFormularioLogin() {
        try {
            log.info("=== Criar Novo Usuário ===");
            
            log.info("Digite o login:");
            String login = lerEntrada();
            
            log.info("Digite a senha:");
            String senha = lerEntrada();
            
            User novoUsuario = userController.createUser(login, senha);
            
            if (novoUsuario != null) {
                log.info("Usuário criado com sucesso! ID: {}", novoUsuario.getId());
            } else {
                exibirErro("Erro ao criar usuário. Verifique os dados informados.");
            }
        } catch (Exception e) {
            exibirErro("Erro inesperado: " + e.getMessage());
        }
    }
    
    public void exibirUsuarios(List<User> usuarios) {
        if (usuarios == null || usuarios.isEmpty()) {
            log.info("Nenhum usuário cadastrado.");
            return;
        }
        
        log.info("=== Lista de Usuários ===");
        usuarios.forEach(user -> 
            log.info("ID: {}, Login: {}", user.getId(), user.getLogin())
        );
    }
    
    public void lerDadosUsuario() {
        List<User> usuarios = userController.getAllUsuarios();
        exibirUsuarios(usuarios);
    }
    
    public void exibirMensagem(String mensagem) {
        log.info(mensagem);
    }
    
    public void exibirErro(String erro) {
        log.error("ERRO: {}", erro);
    }
    
    private String lerEntrada() {
        String entrada = scanner.nextLine();
        return entrada != null ? entrada.trim() : "";
    }
    
    public void exibirMenuPrincipal() {
        log.info("\n=== Sistema de Gerenciamento de Usuários ===");
        log.info("1. Criar usuário");
        log.info("2. Listar usuários");
        log.info("3. Buscar usuário por login");
        log.info("4. Atualizar usuário");
        log.info("5. Deletar usuário");
        log.info("0. Sair");
        log.info("Escolha uma opção:");
    }
    
    public void buscarUsuarioPorLogin() {
        log.info("Digite o login do usuário:");
        String login = lerEntrada();
        
        User usuario = userController.buscarUsuarioPorLogin(login);
        if (usuario != null) {
            log.info("Usuário encontrado - ID: {}, Login: {}", usuario.getId(), usuario.getLogin());
        } else {
            exibirErro("Usuário não encontrado.");
        }
    }
    
    public void atualizarUsuario() {
        log.info("Digite o ID do usuário a ser atualizado:");
        try {
            int id = Integer.parseInt(lerEntrada());
            
            User usuarioExistente = userController.buscarUsuario(id);
            if (usuarioExistente == null) {
                exibirErro("Usuário não encontrado.");
                return;
            }
            
            log.info("Usuário atual: {}", usuarioExistente.getLogin());
            log.info("Digite o novo login (ou pressione Enter para manter o atual):");
            String novoLogin = lerEntrada();
            
            log.info("Digite a nova senha:");
            String novaSenha = lerEntrada();
            
            if (novoLogin.isEmpty()) {
                novoLogin = usuarioExistente.getLogin();
            }
            
            User usuarioAtualizado = new User(id, novoLogin, novaSenha);
            User resultado = userController.updateUsuario(usuarioAtualizado);
            
            if (resultado != null) {
                log.info("Usuário atualizado com sucesso!");
            } else {
                exibirErro("Erro ao atualizar usuário.");
            }
        } catch (NumberFormatException e) {
            exibirErro("ID inválido.");
        }
    }
    
    public void deletarUsuario() {
        log.info("Digite o ID do usuário a ser deletado:");
        try {
            int id = Integer.parseInt(lerEntrada());
            
            if (userController.deleteUsuario(id)) {
                log.info("Usuário deletado com sucesso!");
            } else {
                exibirErro("Usuário não encontrado ou erro ao deletar.");
            }
        } catch (NumberFormatException e) {
            exibirErro("ID inválido.");
        }
    }
}