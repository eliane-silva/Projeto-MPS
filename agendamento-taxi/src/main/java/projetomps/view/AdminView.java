package projetomps.view;

import lombok.AllArgsConstructor;
import projetomps.business_logic.controller.FacadeSingletonController;
import projetomps.business_logic.model.Admin;
import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.model.Taxist;
import projetomps.business_logic.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@AllArgsConstructor
public class AdminView {
    private final FacadeSingletonController controller;
    private final Scanner scanner;
    private final Admin admin;

    public void exibirMenuPrincipal() {
        boolean continuar = true;

        limparTela();
        exibirCabecalhoAdmin();

        while (continuar) {
            try {
                exibirOpcoes();
                int opcao = lerOpcao();

                switch (opcao) {
                    case 1:
                        menuGerenciarUsuarios();
                        break;
                    case 2:
                        listarTodosUsuarios();
                        break;
                    case 3:
                        removerUsuario();
                        break;
                    case 4:
                        consultarEstatisticasGerais();
                        break;
                    case 5:
                        listarTodasRotacoes();
                        break;
                    case 6:
                        removerRotacao();
                        break;
                    case 0:
                        continuar = false;
                        exibirMensagemLogout();
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

    private void exibirCabecalhoAdmin() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                  PAINEL ADMINISTRATIVO                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("👤 Usuário: " + admin.getLogin() + " (Administrador)");
        System.out.println();
    }

    private void exibirOpcoes() {
        System.out.println("┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│                         MENU ADMIN                           │");
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Gerenciar Usuários                                       │");
        System.out.println("│  2. Listar Todos os Usuários                                 │");
        System.out.println("│  3. Remover Usuário                                          │");
        System.out.println("│  4. Consultar Estatísticas Gerais                            │");
        System.out.println("│  5. Listar Todas as Rotações                                 │");
        System.out.println("│  6. Remover Rotação                                          │");
        System.out.println("│  0. Logout                                                   │");
        System.out.println("└──────────────────────────────────────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }

    private void menuGerenciarUsuarios() {
        boolean continuar = true;

        while (continuar) {
            try {
                limparTela();
                System.out.println("╔══════════════════════════════════════════════════════════════╗");
                System.out.println("║                   GERENCIAR USUÁRIOS                         ║");
                System.out.println("╚══════════════════════════════════════════════════════════════╝");
                System.out.println("┌──────────────────────────────────────────────────────────────┐");
                System.out.println("│  1. Cadastrar Administrador                                  │");
                System.out.println("│  2. Cadastrar Taxista                                        │");
                System.out.println("│  3. Atualizar Usuário                                        │");
                System.out.println("│  4. Visualizar Detalhes do Usuário                           │");
                System.out.println("│  0. Voltar                                                   │");
                System.out.println("└──────────────────────────────────────────────────────────────┘");
                System.out.print("Escolha uma opção: ");

                int opcao = lerOpcao();

                switch (opcao) {
                    case 1:
                        cadastrarAdmin();
                        break;
                    case 2:
                        cadastrarTaxista();
                        break;
                    case 3:
                        atualizarUsuario();
                        break;
                    case 4:
                        visualizarDetalhesUsuario();
                        break;
                    case 0:
                        continuar = false;
                        break;
                    default:
                        exibirErro("Opção inválida! Tente novamente.");
                        pausar();
                }
            } catch (NumberFormatException e) {
                exibirErro("Por favor, digite um número válido.");
                pausar();
            } catch (Exception e) {
                exibirErro("Erro inesperado: " + e.getMessage());
                pausar();
            }
        }
        limparTela();
    }

    private void cadastrarAdmin() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                CADASTRAR NOVO ADMINISTRADOR                  ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            System.out.print("Login do administrador: ");
            String login = lerEntrada();

            System.out.print("Senha do administrador: ");
            String senha = lerEntrada();

            if (login.isEmpty() || senha.isEmpty()) {
                exibirErro("Por favor, preencha todos os campos obrigatórios!");
                pausar();
                return;
            }

            Optional<Admin> adminSalvo = controller.criarAdmin(login, senha, admin);

            if (adminSalvo.isPresent()) {
                exibirSucesso("Administrador cadastrado com sucesso!");
                System.out.println("📋 ID do usuário: " + adminSalvo.get().getId());
                System.out.println("👤 Login: " + adminSalvo.get().getLogin());
                System.out.println("🛡️ Tipo: Administrador");
            } else {
                exibirErro("Erro ao cadastrar administrador. Verifique se o login já não está sendo usado.");
            }

        } catch (Exception e) {
            exibirErro("Erro ao cadastrar administrador: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void cadastrarTaxista() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                   CADASTRAR NOVO TAXISTA                     ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            System.out.print("Login do taxista: ");
            String login = lerEntrada();

            System.out.print("Senha do taxista: ");
            String senha = lerEntrada();

            System.out.print("Nome completo do taxista: ");
            String nome = lerEntrada();

            System.out.print("Email do taxista: ");
            String email = lerEntrada();

            if (login.isEmpty() || senha.isEmpty()) {
                exibirErro("Por favor, preencha pelo menos login e senha!");
                pausar();
                return;
            }

            Optional<Taxist> taxistaSalvo = controller.criarTaxista(login, senha,
                    nome.isEmpty() ? null : nome, email.isEmpty() ? null : email, admin);

            if (taxistaSalvo.isPresent()) {
                exibirSucesso("Taxista cadastrado com sucesso!");
                System.out.println("📋 ID do usuário: " + taxistaSalvo.get().getId());
                System.out.println("👤 Login: " + taxistaSalvo.get().getLogin());
                System.out.println("📛 Nome: " + (taxistaSalvo.get().getName() != null ? taxistaSalvo.get().getName() : "Não informado"));
                System.out.println("📧 Email: " + (taxistaSalvo.get().getEmail() != null ? taxistaSalvo.get().getEmail() : "Não informado"));
                System.out.println("🚖 Tipo: Taxista");
            } else {
                exibirErro("Erro ao cadastrar taxista. Verifique se o login já não está sendo usado.");
            }

        } catch (Exception e) {
            exibirErro("Erro ao cadastrar taxista: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void atualizarUsuario() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    ATUALIZAR USUÁRIO                         ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            // Mostrar lista primeiro
            List<User> usuarios = controller.getUserController().getAllUsuarios();

            if (usuarios.isEmpty()) {
                System.out.println("ℹ Nenhum usuário cadastrado para atualizar.");
                pausar();
                return;
            }

            System.out.println("Usuários disponíveis:");
            System.out.println();
            System.out.println("┌─────┬─────────────────┬─────────────────┐");
            System.out.println("│ ID  │     LOGIN       │      TIPO       │");
            System.out.println("├─────┼─────────────────┼─────────────────┤");

            for (User user : usuarios) {
                String tipo = controller.getUserController().getTipoUsuario(user);
                System.out.printf("│ %-3d │ %-15s │ %-15s │%n",
                        user.getId(), user.getLogin(), tipo);
            }

            System.out.println("└─────┴─────────────────┴─────────────────┘");
            System.out.println();

            System.out.print("Digite o ID do usuário a ser atualizado (0 para cancelar): ");
            int id = lerOpcao();

            if (id == 0) {
                System.out.println("ℹ Operação cancelada.");
                pausar();
                return;
            }

            Optional<User> usuarioOpt = controller.getUserController().buscarUsuario(id);
            if (usuarioOpt.isEmpty()) {
                exibirErro("Usuário não encontrado!");
                pausar();
                return;
            }

            User usuario = usuarioOpt.get();

            // Mostrar dados atuais
            System.out.println("\nDados atuais:");
            System.out.println("Login: " + usuario.getLogin());
            System.out.println("Tipo: " + controller.getUserController().getTipoUsuario(usuario));

            if (usuario instanceof Taxist) {
                Taxist taxista = (Taxist) usuario;
                System.out.println("Nome: " + (taxista.getName() != null ? taxista.getName() : "Não informado"));
                System.out.println("Email: " + (taxista.getEmail() != null ? taxista.getEmail() : "Não informado"));
            }

            System.out.println();
            System.out.print("Novo login (deixe vazio para manter atual): ");
            String novoLogin = lerEntrada();

            System.out.print("Nova senha (deixe vazio para manter atual): ");
            String novaSenha = lerEntrada();

            // Aplicar alterações
            if (!novoLogin.isEmpty()) {
                usuario.setLogin(novoLogin);
            }
            if (!novaSenha.isEmpty()) {
                usuario.setSenha(novaSenha);
            }

            // Se for taxista, permitir alterar nome e email
            if (usuario instanceof Taxist) {
                Taxist taxista = (Taxist) usuario;

                System.out.print("Novo nome (deixe vazio para manter atual): ");
                String novoNome = lerEntrada();

                System.out.print("Novo email (deixe vazio para manter atual): ");
                String novoEmail = lerEntrada();

                if (!novoNome.isEmpty()) {
                    taxista.setName(novoNome);
                }
                if (!novoEmail.isEmpty()) {
                    taxista.setEmail(novoEmail);
                }

                Optional<Taxist> taxistaAtualizado = controller.getUserController().atualizarTaxista(taxista);
                if (taxistaAtualizado.isPresent()) {
                    exibirSucesso("Taxista atualizado com sucesso!");
                } else {
                    exibirErro("Erro ao atualizar taxista.");
                }
            } else if (usuario instanceof Admin) {
                Optional<Admin> adminAtualizado = controller.getUserController().atualizarAdmin((Admin) usuario);
                if (adminAtualizado.isPresent()) {
                    exibirSucesso("Administrador atualizado com sucesso!");
                } else {
                    exibirErro("Erro ao atualizar administrador.");
                }
            }

        } catch (NumberFormatException e) {
            exibirErro("ID inválido. Digite apenas números.");
        } catch (Exception e) {
            exibirErro("Erro ao atualizar usuário: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void visualizarDetalhesUsuario() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                  DETALHES DO USUÁRIO                         ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            List<User> usuarios = controller.getUserController().getAllUsuarios();

            if (usuarios.isEmpty()) {
                System.out.println("ℹ Nenhum usuário cadastrado.");
                pausar();
                return;
            }

            System.out.println("Usuários disponíveis:");
            System.out.println();
            System.out.println("┌─────┬─────────────────┬─────────────────┐");
            System.out.println("│ ID  │     LOGIN       │      TIPO       │");
            System.out.println("├─────┼─────────────────┼─────────────────┤");

            for (User user : usuarios) {
                String tipo = controller.getUserController().getTipoUsuario(user);
                System.out.printf("│ %-3d │ %-15s │ %-15s │%n",
                        user.getId(), user.getLogin(), tipo);
            }

            System.out.println("└─────┴─────────────────┴─────────────────┘");
            System.out.println();

            System.out.print("Digite o ID do usuário para ver detalhes (0 para cancelar): ");
            int id = lerOpcao();

            if (id == 0) {
                System.out.println("ℹ Operação cancelada.");
                pausar();
                return;
            }

            Optional<User> usuarioOpt = controller.getUserController().buscarUsuario(id);
            if (usuarioOpt.isEmpty()) {
                exibirErro("Usuário não encontrado!");
                pausar();
                return;
            }

            User usuario = usuarioOpt.get();

            System.out.println();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                     DETALHES COMPLETOS                       ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("📋 ID: " + usuario.getId());
            System.out.println("👤 Login: " + usuario.getLogin());
            System.out.println("🏷️ Tipo: " + controller.getUserController().getTipoUsuario(usuario));

            if (usuario instanceof Admin) {
                System.out.println("🛡️ Permissões: Administrador do sistema");
            } else if (usuario instanceof Taxist) {
                Taxist taxista = (Taxist) usuario;
                System.out.println("📛 Nome: " + (taxista.getName() != null ? taxista.getName() : "Não informado"));
                System.out.println("📧 Email: " + (taxista.getEmail() != null ? taxista.getEmail() : "Não informado"));
                System.out.println("🚖 Função: Motorista de táxi");
            }

            System.out.println("✅ Status: Usuário válido - " + usuario.isValidUser());

        } catch (NumberFormatException e) {
            exibirErro("ID inválido. Digite apenas números.");
        } catch (Exception e) {
            exibirErro("Erro ao visualizar usuário: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void listarTodosUsuarios() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                   LISTA DE USUÁRIOS                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            List<User> usuarios = controller.getUserController().getAllUsuarios();

            if (usuarios.isEmpty()) {
                System.out.println("ℹ Nenhum usuário cadastrado no sistema.");
            } else {
                System.out.println("┌─────┬─────────────────┬─────────────────┬──────────────────────┐");
                System.out.println("│ ID  │     LOGIN       │      TIPO       │        DETALHES      │");
                System.out.println("├─────┼─────────────────┼─────────────────┼──────────────────────┤");

                for (User user : usuarios) {
                    String tipo = controller.getUserController().getTipoUsuario(user);
                    String detalhes = "";

                    if (user instanceof Taxist) {
                        Taxist taxista = (Taxist) user;
                        if (taxista.getName() != null && !taxista.getName().isEmpty()) {
                            detalhes = taxista.getName();
                        } else {
                            detalhes = "Nome não informado";
                        }
                    } else if (user instanceof Admin) {
                        detalhes = "Administrador";
                    } else {
                        detalhes = "Usuário base";
                    }

                    System.out.printf("│ %-3d │ %-15s │ %-15s │ %-20s │%n",
                            user.getId(), user.getLogin(), tipo, detalhes);
                }

                System.out.println("└─────┴─────────────────┴─────────────────┴──────────────────────┘");
                System.out.println();
                System.out.println("Total de usuários: " + usuarios.size());

                // Estatísticas por tipo usando contadores do UserController
                long totalAdmins = controller.getUserController().contarUsuariosPorTipo(Admin.class);
                long totalTaxistas = controller.getUserController().contarUsuariosPorTipo(Taxist.class);
                long totalUsuarios = usuarios.size() - totalAdmins - totalTaxistas;

                System.out.println("📊 Estatísticas:");
                System.out.println("   • Administradores: " + totalAdmins);
                System.out.println("   • Taxistas: " + totalTaxistas);
                System.out.println("   • Usuários base: " + totalUsuarios);
            }

        } catch (Exception e) {
            exibirErro("Erro ao listar usuários: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void removerUsuario() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                     REMOVER USUÁRIO                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            // Mostrar lista primeiro
            List<User> usuarios = controller.getUserController().getAllUsuarios();

            if (usuarios.isEmpty()) {
                System.out.println("ℹ Nenhum usuário cadastrado para remover.");
                pausar();
                return;
            }

            System.out.println("Usuários disponíveis:");
            System.out.println();
            System.out.println("┌─────┬─────────────────┬─────────────────┐");
            System.out.println("│ ID  │     LOGIN       │      TIPO       │");
            System.out.println("├─────┼─────────────────┼─────────────────┤");

            for (User user : usuarios) {
                String tipo = controller.getUserController().getTipoUsuario(user);
                System.out.printf("│ %-3d │ %-15s │ %-15s │%n",
                        user.getId(), user.getLogin(), tipo);
            }

            System.out.println("└─────┴─────────────────┴─────────────────┘");
            System.out.println();

            System.out.print("Digite o ID do usuário a ser removido (0 para cancelar): ");
            int id = lerOpcao();

            if (id == 0) {
                System.out.println("ℹ Operação cancelada.");
                pausar();
                return;
            }

            // Verificar se é o próprio admin tentando se remover
            if (id == admin.getId()) {
                exibirErro("Você não pode remover sua própria conta de administrador!");
                pausar();
                return;
            }

            if (controller.getUserController().deleteUsuario(id)) {
                exibirSucesso("Usuário removido com sucesso!");
            } else {
                exibirErro("Usuário não encontrado ou erro ao remover.");
            }

        } catch (NumberFormatException e) {
            exibirErro("ID inválido. Digite apenas números.");
        } catch (Exception e) {
            exibirErro("Erro ao remover usuário: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void consultarEstatisticasGerais() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                 ESTATÍSTICAS DO SISTEMA                      ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            List<User> usuarios = controller.getUserController().getAllUsuarios();
            List<Rotation> rotacoes = controller.getRotationController().getAllRotations();

            long totalUsuarios = usuarios.size();
            long totalAdmins = controller.getUserController().contarUsuariosPorTipo(Admin.class);
            long totalTaxistas = controller.getUserController().contarUsuariosPorTipo(Taxist.class);
            long totalUsuariosBase = totalUsuarios - totalAdmins - totalTaxistas;

            long totalRotacoes = rotacoes.size();
            long rotacoesConfirmadas = rotacoes.stream()
                    .filter(r -> "CONFIRMED".equalsIgnoreCase(r.getStatus())).count();
            long rotacoesPendentes = rotacoes.stream()
                    .filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count();
            long rotacoesCanceladas = rotacoes.stream()
                    .filter(r -> "CANCELLED".equalsIgnoreCase(r.getStatus())).count();

            System.out.println("┌──────────────────────────────────────────────────────────────┐");
            System.out.println("│                         USUÁRIOS                             │");
            System.out.println("├──────────────────────────────────────────────────────────────┤");
            System.out.printf("│ Total de usuários: %-41s │%n", totalUsuarios);
            System.out.printf("│ Administradores: %-43s │%n", totalAdmins);
            System.out.printf("│ Taxistas: %-50s │%n", totalTaxistas);
            System.out.printf("│ Usuários base: %-45s │%n", totalUsuariosBase);
            System.out.println("└──────────────────────────────────────────────────────────────┘");

            System.out.println("┌──────────────────────────────────────────────────────────────┐");
            System.out.println("│                        ROTAÇÕES                              │");
            System.out.println("├──────────────────────────────────────────────────────────────┤");
            System.out.printf("│ Total de rotações: %-41s │%n", totalRotacoes);
            System.out.printf("│ Confirmadas: %-47s │%n", rotacoesConfirmadas);
            System.out.printf("│ Pendentes: %-49s │%n", rotacoesPendentes);
            System.out.printf("│ Canceladas: %-48s │%n", rotacoesCanceladas);
            System.out.println("└──────────────────────────────────────────────────────────────┘");

        } catch (Exception e) {
            exibirErro("Erro ao consultar estatísticas: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void listarTodasRotacoes() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                   LISTA DE ROTAÇÕES                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            List<Rotation> rotacoes = controller.getRotationController().getAllRotations();

            if (rotacoes.isEmpty()) {
                System.out.println("ℹ Nenhuma rotação cadastrada no sistema.");
            } else {
                System.out.println("┌─────┬────────────┬─────────┬─────────┬───────────┬─────────────────┐");
                System.out.println("│ ID  │    DATA    │ INÍCIO  │   FIM   │  STATUS   │     TAXISTA     │");
                System.out.println("├─────┼────────────┼─────────┼─────────┼───────────┼─────────────────┤");

                for (Rotation r : rotacoes) {
                    String taxistaInfo = (r.getTaxist() != null) ? r.getTaxist().getLogin() : "N/A";
                    String endTime = (r.getEndTime() != null) ? r.getEndTime().toString() : "N/A";

                    System.out.printf("│ %-3d │ %-10s │ %-7s │ %-7s │ %-9s │ %-15s │%n",
                            r.getIdRotation(),
                            r.getDate(),
                            r.getStartTime(),
                            endTime,
                            r.getStatus(),
                            taxistaInfo);
                }

                System.out.println("└─────┴────────────┴─────────┴─────────┴───────────┴─────────────────┘");
                System.out.println();
                System.out.println("Total de rotações: " + rotacoes.size());
            }

        } catch (Exception e) {
            exibirErro("Erro ao listar rotações: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void removerRotacao() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                     REMOVER ROTAÇÃO                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            List<Rotation> rotacoes = controller.getRotationController().getAllRotations();

            if (rotacoes.isEmpty()) {
                System.out.println("ℹ Nenhuma rotação cadastrada para remover.");
                pausar();
                return;
            }

            System.out.println("Rotações disponíveis:");
            System.out.println();
            System.out.println("┌─────┬────────────┬─────────┬───────────┬─────────────────┐");
            System.out.println("│ ID  │    DATA    │ INÍCIO  │  STATUS   │     TAXISTA     │");
            System.out.println("├─────┼────────────┼─────────┼───────────┼─────────────────┤");

            for (Rotation r : rotacoes) {
                String taxistaInfo = (r.getTaxist() != null) ? r.getTaxist().getLogin() : "N/A";

                System.out.printf("│ %-3d │ %-10s │ %-7s │ %-9s │ %-15s │%n",
                        r.getIdRotation(),
                        r.getDate(),
                        r.getStartTime(),
                        r.getStatus(),
                        taxistaInfo);
            }

            System.out.println("└─────┴────────────┴─────────┴───────────┴─────────────────┘");
            System.out.println();

            System.out.print("Digite o ID da rotação a ser removida (0 para cancelar): ");
            int id = lerOpcao();

            if (id == 0) {
                System.out.println("ℹ Operação cancelada.");
                pausar();
                return;
            }

            Rotation rotacao = rotacoes.stream()
                    .filter(r -> r.getIdRotation() == id)
                    .findFirst()
                    .orElse(null);

            if (rotacao != null && controller.getRotationController().deleteRotation(rotacao)) {
                exibirSucesso("Rotação removida com sucesso!");
            } else {
                exibirErro("Rotação não encontrada ou erro ao remover.");
            }

        } catch (NumberFormatException e) {
            exibirErro("ID inválido. Digite apenas números.");
        } catch (Exception e) {
            exibirErro("Erro ao remover rotação: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void exibirMensagemLogout() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║                    LOGOUT REALIZADO                         ║");
        System.out.println("║                                                              ║");
        System.out.println("║            Obrigado por usar o sistema, Admin!              ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.print("Pressione ENTER para continuar...");
        scanner.nextLine();
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