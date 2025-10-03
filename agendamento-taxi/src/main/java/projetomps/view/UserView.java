package projetomps.view;

import projetomps.business_logic.controller.FacadeSingletonController;
import projetomps.business_logic.model.Admin;
import projetomps.business_logic.model.Taxist;
import projetomps.business_logic.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Slf4j
@AllArgsConstructor
public class UserView {
    private final FacadeSingletonController controller;
    private final Scanner scanner;

    public void exibirMenuPrincipal() {
        boolean continuar = true;

        limparTela();
        exibirCabecalho();

        while (continuar) {
            try {
                exibirOpcoes();
                int opcao = lerOpcao();

                switch (opcao) {
                    case 1:
                        criarUsuario();
                        break;
                    case 2:
                        listarUsuarios();
                        break;
                    case 3:
                        buscarUsuarioPorLogin();
                        break;
                    case 4:
                        buscarUsuarioPorId();
                        break;
                    case 5:
                        atualizarUsuario();
                        break;
                    case 6:
                        deletarUsuario();
                        break;
                    case 7:
                        exibirEstatisticas();
                        break;
                    // NOVO: Casos para Undo e Redo
                    case 8:
                        controller.getUserController().undo();
                        exibirSucesso("Última ação desfeita com sucesso!");
                        pausar();
                        break;
                    case 9:
                        controller.getUserController().redo();
                        exibirSucesso("Última ação refeita com sucesso!");
                        pausar();
                        break;
                    case 0:
                        continuar = false;
                        exibirMensagemSaida();
                        break;
                    default:
                        exibirErro("Opção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                exibirErro("Por favor, digite um número válido.");
            } catch (Exception e) {
                exibirErro("Erro inesperado: " + e.getMessage());
                pausar();
            }
        }
    }

    private void exibirCabecalho() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║              SISTEMA DE GERENCIAMENTO DE USUÁRIOS            ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private void exibirOpcoes() {
        // MODIFICADO: Adicionadas opções de Undo/Redo
        System.out.println("┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│                      MENU DE USUÁRIOS                        │");
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Criar Usuário                                            │");
        System.out.println("│  2. Listar Usuários                                          │");
        System.out.println("│  3. Buscar por Login                                         │");
        System.out.println("│  4. Buscar por ID                                            │");
        System.out.println("│  5. Atualizar Usuário                                        │");
        System.out.println("│  6. Deletar Usuário                                          │");
        System.out.println("│  7. Estatísticas                                             │");
        System.out.println("├╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┤");
        System.out.println("│  8. Desfazer última ação                                     │");
        System.out.println("│  9. Refazer última ação                                      │");
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println("│  0. Sair                                                     │");
        System.out.println("└──────────────────────────────────────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }

    private void criarUsuario() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                       CRIAR USUÁRIO                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            System.out.println("Escolha o tipo de usuário a criar:");
            System.out.println("1. Administrador");
            System.out.println("2. Taxista");
            System.out.print("Opção: ");

            int tipoOpcao = lerOpcao();

            System.out.print("Login: ");
            String login = lerEntrada();

            System.out.print("Senha: ");
            String senha = lerEntrada();

            if (login.isEmpty() || senha.isEmpty()) {
                exibirErro("Login e senha são obrigatórios!");
                pausar();
                return;
            }

            switch (tipoOpcao) {
                case 1:
                    criarAdmin(login, senha);
                    break;
                case 2:
                    criarTaxista(login, senha);
                    break;
                default:
                    exibirErro("Opção inválida!");
            }

        } catch (Exception e) {
            exibirErro("Erro ao criar usuário: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void criarAdmin(String login, String senha) {
        Optional<Admin> admin = controller.getUserController().criarAdmin(login, senha);

        if (admin.isPresent()) {
            exibirSucesso("Administrador criado com sucesso!");
            System.out.println("ID: " + admin.get().getId());
            System.out.println("Login: " + admin.get().getLogin());
            System.out.println("Tipo: ADMIN");
        } else {
            exibirErro("Erro ao criar administrador. Verifique os dados informados.");
        }
    }

    private void criarTaxista(String login, String senha) {
        System.out.print("Nome completo: ");
        String nome = lerEntrada();

        System.out.print("Email: ");
        String email = lerEntrada();

        Optional<Taxist> taxist = controller.getUserController().criarTaxista(
                login,
                senha,
                nome.isEmpty() ? null : nome,
                email.isEmpty() ? null : email
        );

        if (taxist.isPresent()) {
            exibirSucesso("Taxista criado com sucesso!");
            System.out.println("ID: " + taxist.get().getId());
            System.out.println("Login: " + taxist.get().getLogin());
            System.out.println("Nome: " + (taxist.get().getName() != null ? taxist.get().getName() : "Não informado"));
            System.out.println("Email: " + (taxist.get().getEmail() != null ? taxist.get().getEmail() : "Não informado"));
            System.out.println("Tipo: TAXIST");
        } else {
            exibirErro("Erro ao criar taxista. Verifique os dados informados.");
        }
    }

    private void listarUsuarios() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                     LISTA DE USUÁRIOS                        ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            List<User> usuarios = controller.getUserController().getAllUsuarios();

            if (usuarios.isEmpty()) {
                System.out.println("Nenhum usuário cadastrado.");
            } else {
                System.out.println("┌─────┬─────────────────┬─────────────────┬──────────────────────┐");
                System.out.println("│ ID  │     LOGIN       │      TIPO       │        DETALHES      │");
                System.out.println("├─────┼─────────────────┼─────────────────┼──────────────────────┤");

                for (User user : usuarios) {
                    String tipo = controller.getUserController().getTipoUsuario(user);
                    String detalhes = "";

                    if (user instanceof Taxist) {
                        Taxist taxist = (Taxist) user;
                        detalhes = taxist.getName() != null ? taxist.getName() : "Nome não informado";
                    } else if (user instanceof Admin) {
                        detalhes = "Administrador";
                    } else {
                        detalhes = "Usuário padrão";
                    }

                    System.out.printf("│ %-3d │ %-15s │ %-15s │ %-20s │%n",
                            user.getId(), user.getLogin(), tipo, detalhes);
                }

                System.out.println("└─────┴─────────────────┴─────────────────┴──────────────────────┘");
                System.out.println("\nTotal de usuários: " + usuarios.size());
            }

        } catch (Exception e) {
            exibirErro("Erro ao listar usuários: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void buscarUsuarioPorLogin() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                   BUSCAR POR LOGIN                           ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            System.out.print("Digite o login do usuário: ");
            String login = lerEntrada();

            if (login.isEmpty()) {
                exibirErro("Login é obrigatório!");
                pausar();
                return;
            }

            Optional<User> usuario = controller.getUserController().buscarUsuarioPorLogin(login);

            if (usuario.isPresent()) {
                exibirDetalhesUsuario(usuario.get());
            } else {
                System.out.println("Usuário não encontrado com login: " + login);
            }

        } catch (Exception e) {
            exibirErro("Erro ao buscar usuário: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void buscarUsuarioPorId() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                     BUSCAR POR ID                            ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            System.out.print("Digite o ID do usuário: ");
            int id = lerOpcao();

            if (id <= 0) {
                exibirErro("ID deve ser um número positivo!");
                pausar();
                return;
            }

            Optional<User> usuario = controller.getUserController().buscarUsuario(id);

            if (usuario.isPresent()) {
                exibirDetalhesUsuario(usuario.get());
            } else {
                System.out.println("Usuário não encontrado com ID: " + id);
            }

        } catch (NumberFormatException e) {
            exibirErro("ID inválido. Digite apenas números.");
        } catch (Exception e) {
            exibirErro("Erro ao buscar usuário: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void exibirDetalhesUsuario(User usuario) {
        System.out.println("\n══════════════════════════════════════════════════════");
        System.out.println("                   DETALHES DO USUÁRIO");
        System.out.println("══════════════════════════════════════════════════════");
        System.out.println("ID: " + usuario.getId());
        System.out.println("Login: " + usuario.getLogin());
        System.out.println("Tipo: " + controller.getUserController().getTipoUsuario(usuario));

        if (usuario instanceof Taxist) {
            Taxist taxist = (Taxist) usuario;
            System.out.println("Nome: " + (taxist.getName() != null ? taxist.getName() : "Não informado"));
            System.out.println("Email: " + (taxist.getEmail() != null ? taxist.getEmail() : "Não informado"));
        } else if (usuario instanceof Admin) {
            System.out.println("Função: Administrador do Sistema");
        }

        System.out.println("Status: " + (usuario.isValidUser() ? "Ativo" : "Inválido"));
        System.out.println("══════════════════════════════════════════════════════");
    }

    private void atualizarUsuario() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                   ATUALIZAR USUÁRIO                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            System.out.print("Digite o ID do usuário a ser atualizado: ");
            int id = lerOpcao();

            Optional<User> usuarioOpt = controller.getUserController().buscarUsuario(id);
            if (usuarioOpt.isEmpty()) {
                exibirErro("Usuário não encontrado!");
                pausar();
                return;
            }

            User usuario = usuarioOpt.get();
            exibirDetalhesUsuario(usuario);

            System.out.print("\nNovo login (deixe vazio para manter): ");
            String novoLogin = lerEntrada();

            System.out.print("Nova senha (deixe vazio para manter): ");
            String novaSenha = lerEntrada();

            // Aplicar mudanças
            if (!novoLogin.isEmpty()) {
                usuario.setLogin(novoLogin);
            }
            if (!novaSenha.isEmpty()) {
                usuario.setSenha(novaSenha);
            }

            // Atualizar campos específicos se for taxista
            if (usuario instanceof Taxist) {
                Taxist taxist = (Taxist) usuario;

                System.out.print("Novo nome (deixe vazio para manter): ");
                String novoNome = lerEntrada();

                System.out.print("Novo email (deixe vazio para manter): ");
                String novoEmail = lerEntrada();

                if (!novoNome.isEmpty()) {
                    taxist.setName(novoNome);
                }
                if (!novoEmail.isEmpty()) {
                    taxist.setEmail(novoEmail);
                }

                Optional<Taxist> resultado = controller.getUserController().atualizarTaxista(taxist);
                if (resultado.isPresent()) {
                    exibirSucesso("Taxista atualizado com sucesso!");
                } else {
                    exibirErro("Erro ao atualizar taxista.");
                }
            } else if (usuario instanceof Admin) {
                Optional<Admin> resultado = controller.getUserController().atualizarAdmin((Admin) usuario);
                if (resultado.isPresent()) {
                    exibirSucesso("Administrador atualizado com sucesso!");
                } else {
                    exibirErro("Erro ao atualizar administrador.");
                }
            }

        } catch (NumberFormatException e) {
            exibirErro("ID inválido.");
        } catch (Exception e) {
            exibirErro("Erro ao atualizar usuário: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void deletarUsuario() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    DELETAR USUÁRIO                           ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            System.out.print("Digite o ID do usuário a ser deletado: ");
            int id = lerOpcao();

            Optional<User> usuarioOpt = controller.getUserController().buscarUsuario(id);
            if (usuarioOpt.isEmpty()) {
                exibirErro("Usuário não encontrado!");
                pausar();
                return;
            }

            User usuario = usuarioOpt.get();
            exibirDetalhesUsuario(usuario);

            System.out.print("\nConfirma a exclusão? (S/N): ");
            String confirmacao = lerEntrada();

            if ("S".equalsIgnoreCase(confirmacao) || "SIM".equalsIgnoreCase(confirmacao)) {
                if (controller.getUserController().deleteUsuario(id)) {
                    exibirSucesso("Usuário deletado com sucesso!");
                } else {
                    exibirErro("Erro ao deletar usuário.");
                }
            } else {
                System.out.println("Operação cancelada.");
            }

        } catch (NumberFormatException e) {
            exibirErro("ID inválido.");
        } catch (Exception e) {
            exibirErro("Erro ao deletar usuário: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void exibirEstatisticas() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    ESTATÍSTICAS DO SISTEMA                   ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            List<User> usuarios = controller.getUserController().getAllUsuarios();

            long totalUsuarios = usuarios.size();
            long totalAdmins = controller.getUserController().contarUsuariosPorTipo(Admin.class);
            long totalTaxistas = controller.getUserController().contarUsuariosPorTipo(Taxist.class);
            long totalUsuariosBase = totalUsuarios - totalAdmins - totalTaxistas;

            System.out.println("┌──────────────────────────────────────────────────────────────┐");
            System.out.println("│                         RESUMO GERAL                         │");
            System.out.println("├──────────────────────────────────────────────────────────────┤");
            System.out.printf("│ Total de usuários: %-37s │%n", totalUsuarios);
            System.out.printf("│ Administradores: %-39s │%n", totalAdmins);
            System.out.printf("│ Taxistas: %-46s │%n", totalTaxistas);
            System.out.printf("│ Usuários base: %-42s │%n", totalUsuariosBase);
            System.out.println("└──────────────────────────────────────────────────────────────┘");

            if (totalUsuarios > 0) {
                double percentualAdmins = (totalAdmins * 100.0) / totalUsuarios;
                double percentualTaxistas = (totalTaxistas * 100.0) / totalUsuarios;
                double percentualUsuariosBase = (totalUsuariosBase * 100.0) / totalUsuarios;

                System.out.println();
                System.out.println("┌──────────────────────────────────────────────────────────────┐");
                System.out.println("│                      DISTRIBUIÇÃO (%)                        │");
                System.out.println("├──────────────────────────────────────────────────────────────┤");
                System.out.printf("│ Administradores: %-35.1f%% │%n", percentualAdmins);
                System.out.printf("│ Taxistas: %-42.1f%% │%n", percentualTaxistas);
                System.out.printf("│ Usuários base: %-38.1f%% │%n", percentualUsuariosBase);
                System.out.println("└──────────────────────────────────────────────────────────────┘");
            }

        } catch (Exception e) {
            exibirErro("Erro ao exibir estatísticas: " + e.getMessage());
        }

        pausar();
        limparTela();
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
        System.out.println("║                Sistema de Usuários Encerrado                ║");
        System.out.println("║                                                              ║");
        System.out.println("║                      Até logo!                              ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private void pausar() {
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    private void limparTela() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}