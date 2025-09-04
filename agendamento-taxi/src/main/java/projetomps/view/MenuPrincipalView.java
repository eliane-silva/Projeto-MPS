package projetomps.view;

import lombok.AllArgsConstructor;
import projetomps.controller.FacadeSingletonController;
import projetomps.model.Admin;
import projetomps.model.Taxist;
import projetomps.model.User;

import java.util.Scanner;

@AllArgsConstructor
public class MenuPrincipalView {
    private final FacadeSingletonController controller;
    private final Scanner scanner;

    public void exibirMenuInicial() {
        limparTela();
        exibirBoasVindas();

        boolean sistemaAtivo = true;

        while (sistemaAtivo) {
            try {
                exibirOpcoesPrincipais();
                int opcao = lerOpcao();

                switch (opcao) {
                    case 1:
                        loginAdmin();
                        break;
                    case 2:
                        menuTaxista();
                        break;
                    case 0:
                        sistemaAtivo = false;
                        exibirMensagemSaida();
                        break;
                    default:
                        exibirErro("Opção inválida! Tente novamente.");
                }
            } catch (Exception e) {
                exibirErro("Erro inesperado: " + e.getMessage());
                pausar();
            }
        }
    }

    private void exibirBoasVindas() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                     SISTEMA AGENDATAXI                       ║");
        System.out.println("║                                                              ║");
        System.out.println("║           Sistema de Gerenciamento de Rodízio de Táxis       ║");
        System.out.println("║                                                              ║");
        System.out.println("║                    Bem-vindo ao sistema!                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private void exibirOpcoesPrincipais() {
        System.out.println("┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│                       MENU PRINCIPAL                         │");
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Entrar como Administrador                                │");
        System.out.println("│  2. Área do Taxista                                          │");
        System.out.println("│  0. Sair do Sistema                                          │");
        System.out.println("└──────────────────────────────────────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }

    private void loginAdmin() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    LOGIN ADMINISTRADOR                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");

        LoginView loginView = new LoginView(controller, scanner);
        User usuario = loginView.exibirTelaLogin();

        if (usuario != null && usuario instanceof Admin) {
            AdminView adminView = new AdminView(controller, scanner, (Admin) usuario);
            adminView.exibirMenuPrincipal();
        } else if (usuario != null) {
            exibirErro("Acesso negado! Apenas administradores podem acessar esta área.");
            pausar();
        }

        limparTela();
    }

    private void menuTaxista() {
        limparTela();

        boolean voltarMenu = false;
        while (!voltarMenu) {
            exibirMenuTaxista();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    loginTaxista();
                    break;
                case 2:
                    cadastrarTaxista();
                    break;
                case 0:
                    voltarMenu = true;
                    break;
                default:
                    exibirErro("Opção inválida! Tente novamente.");
            }
        }

        limparTela();
    }

    private void exibirMenuTaxista() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                       ÁREA DO TAXISTA                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│  1. Fazer Login                                              │");
        System.out.println("│  2. Cadastrar-se como Taxista                                │");
        System.out.println("│  0. Voltar ao Menu Principal                                 │");
        System.out.println("└──────────────────────────────────────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }

    private void loginTaxista() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                       LOGIN TAXISTA                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        LoginView loginView = new LoginView(controller, scanner);
        User usuario = loginView.exibirTelaLogin();

        if (usuario != null && usuario instanceof Taxist) {
            TaxistView taxistView = new TaxistView(controller, scanner, (Taxist) usuario);
            taxistView.exibirMenuPrincipal();
        } else if (usuario != null) {
            exibirErro("Acesso negado! Este usuário não é um taxista.");
            pausar();
        }

        limparTela();
    }

    private void cadastrarTaxista() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                   CADASTRO DE TAXISTA                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();

        try {
            System.out.print("Digite seu login desejado: ");
            String login = lerEntrada();

            System.out.print("Digite sua senha: ");
            String senha = lerEntrada();

            System.out.print("Digite seu nome completo: ");
            String nome = lerEntrada();

            System.out.print("Digite seu email: ");
            String email = lerEntrada();

            // Criar taxista
            Taxist novoTaxista = new Taxist(login, senha);
            novoTaxista.setName(nome);
            novoTaxista.setEmail(email);

            // Salvar através do controller
            User usuarioCriado = controller.getUserController().createUser(login, senha);

            if (usuarioCriado != null) {
                exibirSucesso("Taxista cadastrado com sucesso!");
                System.out.println("Agora você pode fazer login com suas credenciais.");
                pausar();
            } else {
                exibirErro("Erro ao cadastrar taxista. Verifique os dados informados.");
                pausar();
            }

        } catch (Exception e) {
            exibirErro("Erro ao cadastrar taxista: " + e.getMessage());
            pausar();
        }
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String lerEntrada() {
        String entrada = scanner.nextLine();
        return entrada != null ? entrada.trim() : "";
    }

    private void exibirSucesso(String mensagem) {
        System.out.println();
        System.out.println("✓ " + mensagem);
        System.out.println();
    }

    private void exibirErro(String mensagem) {
        System.out.println();
        System.out.println("✗ " + mensagem);
        System.out.println();
    }

    private void exibirMensagemSaida() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║              Obrigado por usar o AgendaTáxi!                 ║");
        System.out.println("║                   Sistema encerrado.                         ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private void pausar() {
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    private void limparTela() {
        // Simula limpeza de tela no terminal
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}