// Caminho: src/main/java/com/sistema/usuario/view/UsuarioView.java
package view;

import usuario.controller.UsuarioController;
import usuario.controller.UsuarioController.*;
import com.sistema.usuario.model.Usuario;
import java.util.List;
import java.util.Scanner;

/**
 * View para interface de usuário em console
 * Responsável pela apresentação e captura de dados do usuário
 * 
 * @author Sistema
 * @version 1.0
 */
public class UsuarioView {
    
    private final UsuarioController usuarioController;
    private final Scanner scanner;
    
    // Constantes para formatação
    private static final String LINHA_SEPARADORA = "=" + "=".repeat(60);
    private static final String LINHA_FINA = "-" + "-".repeat(60);
    
    /**
     * Construtor que inicializa controller e scanner
     */
    public UsuarioView() {
        this.usuarioController = new UsuarioController();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Exibe o menu principal do sistema
     */
    public void exibirMenuPrincipal() {
        int opcao;
        
        do {
            limparTela();
            exibirCabecalho();
            exibirOpcoesMenu();
            
            opcao = lerOpcaoMenu();
            
            switch (opcao) {
                case 1:
                    menuAdicionarUsuario();
                    break;
                case 2:
                    menuListarTodosUsuarios();
                    break;
                case 3:
                    menuListarUsuariosAtivos();
                    break;
                case 4:
                    menuBuscarUsuario();
                    break;
                case 5:
                    menuAutenticarUsuario();
                    break;
                case 6:
                    menuRemoverUsuario();
                    break;
                case 7:
                    exibirEstatisticas();
                    break;
                case 0:
                    exibirMensagem("Obrigado por usar o Sistema de Usuários! Até logo!");
                    break;
                default:
                    exibirMensagemErro("Opção inválida! Tente novamente.");
                    pausar();
            }
            
        } while (opcao != 0);
    }
    
    /**
     * Menu para adicionar novo usuário
     */
    private void menuAdicionarUsuario() {
        limparTela();
        exibirTitulo("ADICIONAR NOVO USUÁRIO");
        
        try {
            System.out.print("Login (3-20 caracteres, apenas letras, números e _): ");
            String login = scanner.nextLine().trim();
            
            if (login.isEmpty()) {
                exibirMensagemErro("Login não pode ser vazio!");
                pausar();
                return;
            }
            
            // Verifica se login está disponível
            if (!usuarioController.isLoginDisponivel(login)) {
                exibirMensagemErro("Login já existe: " + login);
                pausar();
                return;
            }
            
            System.out.print("Senha (mínimo 6 caracteres): ");
            String senha = scanner.nextLine();
            
            if (senha.length() < 6) {
                exibirMensagemErro("Senha deve ter pelo menos 6 caracteres!");
                pausar();
                return;
            }
            
            System.out.print("Email (opcional): ");
            String email = scanner.nextLine().trim();
            
            // Se email foi fornecido, verifica disponibilidade
            if (!email.isEmpty() && !usuarioController.isEmailDisponivel(email)) {
                exibirMensagemErro("Email já existe: " + email);
                pausar();
                return;
            }
            
            System.out.print("Nome completo: ");
            String nomeCompleto = scanner.nextLine().trim();
            
            if (nomeCompleto.isEmpty()) {
                exibirMensagemErro("Nome completo não pode ser vazio!");
                pausar();
                return;
            }
            
            // Confirma os dados
            System.out.println("\n" + LINHA_FINA);
            System.out.println("CONFIRMAR DADOS:");
            System.out.println("Login: " + login);
            System.out.println("Email: " + (email.isEmpty() ? "(não informado)" : email));
            System.out.println("Nome: " + nomeCompleto);
            System.out.println(LINHA_FINA);
            
            System.out.print("Confirma a criação do usuário? (S/N): ");
            String confirmacao = scanner.nextLine().trim().toLowerCase();
            
            if (!confirmacao.equals("s") && !confirmacao.equals("sim")) {
                exibirMensagem("Operação cancelada pelo usuário.");
                pausar();
                return;
            }
            
            // Adiciona o usuário
            ResultadoOperacao resultado = usuarioController.adicionarUsuario(login, senha, email, nomeCompleto);
            
            if (resultado.isSucesso()) {
                exibirMensagemSucesso(resultado.getMensagem());
                System.out.println("Detalhes: " + resultado.getDetalhes());
            } else {
                exibirMensagemErro(resultado.getMensagem());
                if (resultado.getDetalhes() != null) {
                    System.out.println("Detalhes: " + resultado.getDetalhes());
                }
            }
            
        } catch (Exception e) {
            exibirMensagemErro("Erro inesperado: " + e.getMessage());
        }
        
        pausar();
    }
    
    /**
     * Menu para listar todos os usuários
     */
    private void menuListarTodosUsuarios() {
        limparTela();
        exibirTitulo("TODOS OS USUÁRIOS");
        
        ResultadoListagem resultado = usuarioController.listarTodosUsuarios();
        
        if (resultado.isSucesso()) {
            List<Usuario> usuarios = resultado.getUsuarios();
            
            if (usuarios.isEmpty()) {
                exibirMensagem("Nenhum usuário cadastrado no sistema.");
            } else {
                exibirMensagem(resultado.getMensagem());
                System.out.println("\n" + LINHA_FINA);
                
                for (Usuario usuario : usuarios) {
                    System.out.println(usuario.toStringSimples());
                    System.out.println("  Criado em: " + formatarDataHora(usuario.getDataCriacao()));
                    if (usuario.getDataUltimoLogin() != null) {
                        System.out.println("  Último login: " + formatarDataHora(usuario.getDataUltimoLogin()));
                    }
                    System.out.println(LINHA_FINA);
                }
            }
        } else {
            exibirMensagemErro(resultado.getMensagem());
        }
        
        pausar();
    }
    
    /**
     * Menu para listar usuários ativos
     */
    private void menuListarUsuariosAtivos() {
        limparTela();
        exibirTitulo("USUÁRIOS ATIVOS");
        
        ResultadoListagem resultado = usuarioController.listarUsuariosAtivos();
        
        if (resultado.isSucesso()) {
            List<Usuario> usuarios = resultado.getUsuarios();
            
            if (usuarios.isEmpty()) {
                exibirMensagem("Nenhum usuário ativo encontrado.");
            } else {
                exibirMensagem(resultado.getMensagem());
                System.out.println("\n" + LINHA_FINA);
                
                for (Usuario usuario : usuarios) {
                    System.out.println(usuario.toStringSimples());
                    System.out.println("  Criado em: " + formatarDataHora(usuario.getDataCriacao()));
                    if (usuario.getDataUltimoLogin() != null) {
                        System.out.println("  Último login: " + formatarDataHora(usuario.getDataUltimoLogin()));
                    }
                    System.out.println(LINHA_FINA);
                }
            }
        } else {
            exibirMensagemErro(resultado.getMensagem());
        }
        
        pausar();
    }
    
    /**
     * Menu para buscar usuário
     */
    private void menuBuscarUsuario() {
        limparTela();
        exibirTitulo("BUSCAR USUÁRIO");
        
        System.out.println("1. Buscar por ID");
        System.out.println("2. Buscar por Login");
        System.out.print("\nEscolha uma opção: ");
        
        try {
            int opcao = Integer.parseInt(scanner.nextLine().trim());
            
            switch (opcao) {
                case 1:
                    buscarPorId();
                    break;
                case 2:
                    buscarPorLogin();
                    break;
                default:
                    exibirMensagemErro("Opção inválida!");
            }
        } catch (NumberFormatException e) {
            exibirMensagemErro("Digite um número válido!");
        }
        
        pausar();
    }
    
    /**
     * Busca usuário por ID
     */
    private void buscarPorId() {
        System.out.print("\nDigite o ID do usuário: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            
            ResultadoBusca resultado = usuarioController.buscarUsuarioPorId(id);
            
            if (resultado.isSucesso()) {
                Usuario usuario = resultado.getUsuario();
                System.out.println("\n" + LINHA_FINA);
                System.out.println("USUÁRIO ENCONTRADO:");
                exibirDetalhesUsuario(usuario);
            } else {
                exibirMensagemErro(resultado.getMensagem());
            }
            
        }