package projetomps.view;

import lombok.AllArgsConstructor;
import projetomps.controller.FacadeSingletonController;
import projetomps.model.User;
import projetomps.util.exception.LoginException;
import projetomps.util.exception.RepositoryException;

import java.util.Scanner;

@AllArgsConstructor
public class LoginView {
    private final FacadeSingletonController controller;
    private final Scanner scanner;
    private int tentativasLogin = 0;
    private static final int MAX_TENTATIVAS = 3;

    public LoginView(FacadeSingletonController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public User exibirTelaLogin() {
        while (tentativasLogin < MAX_TENTATIVAS) {
            try {
                exibirFormularioLogin();

                System.out.print("Login: ");
                String login = lerEntrada();

                System.out.print("Senha: ");
                String senha = lerEntrada();

                if (login.isEmpty() || senha.isEmpty()) {
                    exibirErro("Por favor, preencha todos os campos!");
                    continue;
                }

                User usuario = controller.autenticarUsuario(login, senha);
                String tipoUsuario = controller.getTipoUsuario(usuario);

                exibirSucessoLogin(usuario.getLogin(), tipoUsuario);
                tentativasLogin = 0; // Reset contador de tentativas

                return usuario;

            } catch (LoginException e) {
                tentativasLogin++;

                if (tentativasLogin >= MAX_TENTATIVAS) {
                    exibirBloqueioTemporario();
                    try {
                        Thread.sleep(5000); // 5 segundos de bloqueio
                        tentativasLogin = 0; // Reset após bloqueio
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        System.out.println("✗ Bloqueio interrompido");
                    }
                } else {
                    exibirErroLogin(e.getMessage(), MAX_TENTATIVAS - tentativasLogin);
                }

            } catch (RepositoryException e) {
                exibirErroSistema(e.getMessage());
                break;
            } catch (Exception e) {
                exibirErro("Erro inesperado: " + e.getMessage());
                break;
            }
        }

        return null;
    }

    private void exibirFormularioLogin() {
        System.out.println("┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│                       FAZER LOGIN                            │");
        System.out.println("└──────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("Por favor, insira suas credenciais:");
        System.out.println();
    }

    private void exibirSucessoLogin(String login, String tipoUsuario) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                     LOGIN REALIZADO!                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("✓ Bem-vindo(a), " + login);
        System.out.println("✓ Tipo de usuário: " + tipoUsuario);
        System.out.println();
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
        limparTela();
    }

    private void exibirErroLogin(String mensagem, int tentativasRestantes) {
        System.out.println();
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│                         ERRO DE LOGIN                          │");
        System.out.println("└────────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("✗ " + mensagem);

        if (tentativasRestantes > 0) {
            System.out.println("ℹ Tentativas restantes: " + tentativasRestantes);
            System.out.println();
            System.out.println("Dicas:");
            System.out.println("• Verifique se o login e senha estão corretos");
            System.out.println("• Certifique-se de não usar espaços extras");
            System.out.println("• Login e senha são case-sensitive");
        }

        System.out.println();
        System.out.print("Pressione ENTER para tentar novamente...");
        scanner.nextLine();
        System.out.println();
    }

    private void exibirBloqueioTemporario() {
        System.out.println();
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│                    SISTEMA BLOQUEADO                           │");
        System.out.println("└────────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("⚠ Número máximo de tentativas excedido!");
        System.out.println("⏳ Sistema bloqueado por 5 segundos por segurança...");
        System.out.println();

        // Barra de progresso do bloqueio
        System.out.print("Aguarde: ");
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(250);
                System.out.print("█");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println();
        System.out.println();
        System.out.println("✓ Bloqueio removido! Você pode tentar fazer login novamente.");
        System.out.println();
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
        limparTela();
    }

    private void exibirErroSistema(String mensagem) {
        System.out.println();
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│                      ERRO DO SISTEMA                           │");
        System.out.println("└────────────────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("✗ " + mensagem);
        System.out.println();
        System.out.println("Por favor, tente novamente mais tarde ou contate o suporte.");
        System.out.println();
        System.out.print("Pressione ENTER para voltar...");
        scanner.nextLine();
        limparTela();
    }

    private void exibirErro(String mensagem) {
        System.out.println();
        System.out.println("✗ " + mensagem);
        System.out.println();
    }

    private String lerEntrada() {
        String entrada = scanner.nextLine();
        return entrada != null ? entrada.trim() : "";
    }

    private void limparTela() {
        // Simula limpeza de tela no terminal
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}