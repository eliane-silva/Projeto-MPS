package projetomps.view;

import lombok.AllArgsConstructor;
import projetomps.business_logic.controller.FacadeSingletonController;
import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.model.Taxist;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TaxistView {
    private final FacadeSingletonController controller;
    private final Scanner scanner;
    private final Taxist taxist;

    public void exibirMenuPrincipal() {
        boolean continuar = true;

        limparTela();
        exibirCabecalhoTaxista();

        while (continuar) {
            try {
                exibirOpcoesTaxista();
                int opcao = lerOpcao();

                switch (opcao) {
                    case 1:
                        criarRotacao();
                        break;
                    case 2:
                        listarMinhasRotacoes();
                        break;
                    case 3:
                        atualizarRotacao();
                        break;
                    case 4:
                        cancelarRotacao();
                        break;
                    case 5:
                        visualizarTodasRotacoes();
                        break;
                    case 6:
                        atualizarDadosPessoais();
                        break;
                    case 7:
                        controller.getRotationController().undo();
                        exibirSucesso("Última ação desfeita!");
                        pausar();
                        break;
                    case 8:
                        controller.getRotationController().redo();
                        exibirSucesso("Última ação refeita!");
                        pausar();
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

    private void exibirCabecalhoTaxista() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                      ÁREA DO TAXISTA                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("👤 Taxista: " + taxist.getName() + " (@" + taxist.getLogin() + ")");
        if (taxist.getEmail() != null && !taxist.getEmail().isEmpty()) {
            System.out.println("📧 Email: " + taxist.getEmail());
        }
        System.out.println();
    }

    private void exibirOpcoesTaxista() {
        // MODIFICADO: Adicionadas opções de Desfazer/Refazer
        System.out.println("┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│                      MENU TAXISTA                            │");
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Criar Nova Rotação                                       │");
        System.out.println("│  2. Minhas Rotações                                          │");
        System.out.println("│  3. Atualizar Rotação                                        │");
        System.out.println("│  4. Cancelar Rotação                                         │");
        System.out.println("│  5. Ver Todas as Rotações do Sistema                         │");
        System.out.println("│  6. Atualizar Meus Dados                                     │");
        System.out.println("├╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┤");
        System.out.println("│  7. Desfazer última ação                                     │");
        System.out.println("│  8. Refazer última ação                                      │");
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println("│  0. Logout                                                   │");
        System.out.println("└──────────────────────────────────────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }

    private void criarRotacao() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                     CRIAR NOVA ROTAÇÃO                       ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            System.out.print("Data da rotação (AAAA-MM-DD): ");
            String dataStr = lerEntrada();

            System.out.print("Horário de início (HH:MM): ");
            String inicioStr = lerEntrada();

            System.out.print("Horário de fim (HH:MM) - opcional: ");
            String fimStr = lerEntrada();

            if (dataStr.isEmpty() || inicioStr.isEmpty()) {
                exibirErro("Data e horário de início são obrigatórios!");
                pausar();
                return;
            }

            try {
                LocalDate data = LocalDate.parse(dataStr);
                LocalTime horaInicio = LocalTime.parse(inicioStr);
                LocalTime horaFim = null;

                if (!fimStr.isEmpty()) {
                    horaFim = LocalTime.parse(fimStr);

                    if (horaFim.isBefore(horaInicio)) {
                        exibirErro("Horário de fim não pode ser anterior ao horário de início!");
                        pausar();
                        return;
                    }
                }

                LocalDate dataMinima = LocalDate.now().plusDays(1);
                if (data.isBefore(dataMinima)) {
                    exibirErro("Não é possível criar rotação para hoje ou datas passadas! "
                            + "Data mínima permitida: " + dataMinima);
                    pausar();
                    return;
                }

                Rotation novaRotacao = new Rotation();
                novaRotacao.setDate(data);
                novaRotacao.setStartTime(horaInicio);
                novaRotacao.setEndTime(horaFim);
                novaRotacao.setStatus("PENDING");
                novaRotacao.setTaxist(taxist);

                Boolean sucesso = controller.getRotationController().createRotation(novaRotacao);

                if (sucesso) {
                    exibirSucesso("Rotação criada com sucesso!");
                    System.out.println("📅 Data: " + data);
                    System.out.println("🕐 Início: " + horaInicio);
                    System.out.println("🕐 Fim: " + (horaFim != null ? horaFim : "Não definido"));
                    System.out.println("📋 Status: PENDING");
                } else {
                    exibirErro("Erro ao criar rotação. Verifique os dados informados.");
                }

            } catch (DateTimeParseException e) {
                exibirErro("Formato de data/hora inválido! Use AAAA-MM-DD para data e HH:MM para horário.");
            }

        } catch (Exception e) {
            exibirErro("Erro ao criar rotação: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void listarMinhasRotacoes() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                      MINHAS ROTAÇÕES                         ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            List<Rotation> todasRotacoes = controller.getRotationController().getAllRotations();
            List<Rotation> minhasRotacoes = todasRotacoes.stream()
                    .filter(r -> r.getTaxist() != null && r.getTaxist().getId() == taxist.getId())
                    .collect(Collectors.toList());

            if (minhasRotacoes.isEmpty()) {
                System.out.println("ℹ Você ainda não possui rotações cadastradas.");
            } else {
                System.out.println("┌─────┬────────────┬─────────┬─────────┬───────────┐");
                System.out.println("│ ID  │    DATA    │ INÍCIO  │   FIM   │  STATUS   │");
                System.out.println("├─────┼────────────┼─────────┼─────────┼───────────┤");

                for (Rotation r : minhasRotacoes) {
                    String endTime = (r.getEndTime() != null) ? r.getEndTime().toString() : "N/A";

                    System.out.printf("│ %-3d │ %-10s │ %-7s │ %-7s │ %-9s │%n",
                            r.getIdRotation(),
                            r.getDate(),
                            r.getStartTime(),
                            endTime,
                            r.getStatus());
                }

                System.out.println("└─────┴────────────┴─────────┴─────────┴───────────┘");
                System.out.println();
                System.out.println("Total de suas rotações: " + minhasRotacoes.size());

                // Estatísticas das rotações
                long pendentes = minhasRotacoes.stream()
                        .filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count();
                long confirmadas = minhasRotacoes.stream()
                        .filter(r -> "CONFIRMED".equalsIgnoreCase(r.getStatus())).count();
                long canceladas = minhasRotacoes.stream()
                        .filter(r -> "CANCELLED".equalsIgnoreCase(r.getStatus())).count();

                System.out.println("📊 Estatísticas:");
                System.out.println("   • Pendentes: " + pendentes);
                System.out.println("   • Confirmadas: " + confirmadas);
                System.out.println("   • Canceladas: " + canceladas);
            }

        } catch (Exception e) {
            exibirErro("Erro ao listar rotações: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void atualizarRotacao() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    ATUALIZAR ROTAÇÃO                         ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            List<Rotation> todasRotacoes = controller.getRotationController().getAllRotations();
            List<Rotation> minhasRotacoes = todasRotacoes.stream()
                    .filter(r -> r.getTaxist() != null && r.getTaxist().getId() == taxist.getId())
                    .collect(Collectors.toList());

            if (minhasRotacoes.isEmpty()) {
                System.out.println("ℹ Você não possui rotações para atualizar.");
                pausar();
                return;
            }

            System.out.println("Suas rotações disponíveis:");
            System.out.println();
            System.out.println("┌─────┬────────────┬─────────┬─────────┬───────────┐");
            System.out.println("│ ID  │    DATA    │ INÍCIO  │   FIM   │  STATUS   │");
            System.out.println("├─────┼────────────┼─────────┼─────────┼───────────┤");

            for (Rotation r : minhasRotacoes) {
                String endTime = (r.getEndTime() != null) ? r.getEndTime().toString() : "N/A";
                System.out.printf("│ %-3d │ %-10s │ %-7s │ %-7s │ %-9s │%n",
                        r.getIdRotation(),
                        r.getDate(),
                        r.getStartTime(),
                        endTime,
                        r.getStatus());
            }

            System.out.println("└─────┴────────────┴─────────┴─────────┴───────────┘");
            System.out.println();

            System.out.print("Digite o ID da rotação a ser atualizada (0 para cancelar): ");
            int id = lerOpcao();

            if (id == 0) {
                System.out.println("ℹ Operação cancelada.");
                pausar();
                return;
            }

            Rotation rotacao = minhasRotacoes.stream()
                    .filter(r -> r.getIdRotation() == id)
                    .findFirst()
                    .orElse(null);

            if (rotacao == null) {
                exibirErro("Rotação não encontrada!");
                pausar();
                return;
            }

            System.out.println("\nDados atuais da rotação:");
            System.out.println("Data: " + rotacao.getDate());
            System.out.println("Início: " + rotacao.getStartTime());
            System.out.println("Fim: " + (rotacao.getEndTime() != null ? rotacao.getEndTime() : "Não definido"));
            System.out.println("Status: " + rotacao.getStatus());

            System.out.println();
            System.out.print("Nova data (AAAA-MM-DD) - deixe vazio para manter atual: ");
            String novaDataStr = lerEntrada();

            System.out.print("Novo horário de início (HH:MM) - deixe vazio para manter atual: ");
            String novoInicioStr = lerEntrada();

            System.out.print("Novo horário de fim (HH:MM) - deixe vazio para manter atual: ");
            String novoFimStr = lerEntrada();

            System.out.print("Novo status (PENDING/CONFIRMED/CANCELLED) - deixe vazio para manter atual: ");
            String novoStatus = lerEntrada();

            try {
                // Aplicar as mudanças se fornecidas
                if (!novaDataStr.isEmpty()) {
                    LocalDate novaData = LocalDate.parse(novaDataStr);
                    if (novaData.isBefore(LocalDate.now())) {
                        exibirErro("Não é possível definir data passada!");
                        pausar();
                        return;
                    }
                    rotacao.setDate(novaData);
                }

                if (!novoInicioStr.isEmpty()) {
                    LocalTime novoInicio = LocalTime.parse(novoInicioStr);
                    rotacao.setStartTime(novoInicio);
                }

                if (!novoFimStr.isEmpty()) {
                    if ("null".equalsIgnoreCase(novoFimStr.trim())) {
                        rotacao.setEndTime(null);
                    } else {
                        LocalTime novoFim = LocalTime.parse(novoFimStr);
                        if (novoFim.isBefore(rotacao.getStartTime())) {
                            exibirErro("Horário de fim não pode ser anterior ao horário de início!");
                            pausar();
                            return;
                        }
                        rotacao.setEndTime(novoFim);
                    }
                }

                if (!novoStatus.isEmpty()) {
                    String statusUpper = novoStatus.toUpperCase();
                    if (statusUpper.equals("PENDING") || statusUpper.equals("CONFIRMED") ||
                            statusUpper.equals("CANCELLED")) {
                        rotacao.setStatus(statusUpper);
                    } else {
                        exibirErro("Status inválido! Use PENDING, CONFIRMED ou CANCELLED.");
                        pausar();
                        return;
                    }
                }

                Rotation rotacaoAtualizada = controller.getRotationController().updateRotation(rotacao);

                if (rotacaoAtualizada != null) {
                    exibirSucesso("Rotação atualizada com sucesso!");
                } else {
                    exibirErro("Erro ao atualizar rotação.");
                }

            } catch (DateTimeParseException e) {
                exibirErro("Formato de data/hora inválido! Use AAAA-MM-DD para data e HH:MM para horário.");
            }

        } catch (NumberFormatException e) {
            exibirErro("ID inválido. Digite apenas números.");
        } catch (Exception e) {
            exibirErro("Erro ao atualizar rotação: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void cancelarRotacao() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    CANCELAR ROTAÇÃO                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            List<Rotation> todasRotacoes = controller.getRotationController().getAllRotations();
            List<Rotation> minhasRotacoesAtiveis = todasRotacoes.stream()
                    .filter(r -> r.getTaxist() != null && r.getTaxist().getId() == taxist.getId())
                    .filter(r -> !"CANCELLED".equalsIgnoreCase(r.getStatus()))
                    .collect(Collectors.toList());

            if (minhasRotacoesAtiveis.isEmpty()) {
                System.out.println("ℹ Você não possui rotações ativas para cancelar.");
                pausar();
                return;
            }

            System.out.println("Suas rotações que podem ser canceladas:");
            System.out.println();
            System.out.println("┌─────┬────────────┬─────────┬─────────┬───────────┐");
            System.out.println("│ ID  │    DATA    │ INÍCIO  │   FIM   │  STATUS   │");
            System.out.println("├─────┼────────────┼─────────┼─────────┼───────────┤");

            for (Rotation r : minhasRotacoesAtiveis) {
                String endTime = (r.getEndTime() != null) ? r.getEndTime().toString() : "N/A";
                System.out.printf("│ %-3d │ %-10s │ %-7s │ %-7s │ %-9s │%n",
                        r.getIdRotation(),
                        r.getDate(),
                        r.getStartTime(),
                        endTime,
                        r.getStatus());
            }

            System.out.println("└─────┴────────────┴─────────┴─────────┴───────────┘");
            System.out.println();

            System.out.print("Digite o ID da rotação a ser cancelada (0 para voltar): ");
            int id = lerOpcao();

            if (id == 0) {
                System.out.println("ℹ Operação cancelada.");
                pausar();
                return;
            }

            Rotation rotacao = minhasRotacoesAtiveis.stream()
                    .filter(r -> r.getIdRotation() == id)
                    .findFirst()
                    .orElse(null);

            if (rotacao == null) {
                exibirErro("Rotação não encontrada!");
                pausar();
                return;
            }

            System.out.println();
            System.out.println("⚠️  Confirmar cancelamento da rotação:");
            System.out.println("   Data: " + rotacao.getDate());
            System.out.println("   Horário: " + rotacao.getStartTime() +
                    " - " + (rotacao.getEndTime() != null ? rotacao.getEndTime() : "Indefinido"));
            System.out.println("   Status atual: " + rotacao.getStatus());
            System.out.println();
            System.out.print("Confirma o cancelamento? (S/N): ");

            String confirmacao = lerEntrada();

            if ("S".equalsIgnoreCase(confirmacao) || "SIM".equalsIgnoreCase(confirmacao)) {
                rotacao.setStatus("CANCELLED");
                Rotation rotacaoAtualizada = controller.getRotationController().updateRotation(rotacao);

                if (rotacaoAtualizada != null) {
                    exibirSucesso("Rotação cancelada com sucesso!");
                } else {
                    exibirErro("Erro ao cancelar rotação.");
                }
            } else {
                System.out.println("ℹ Cancelamento não confirmado.");
            }

        } catch (NumberFormatException e) {
            exibirErro("ID inválido. Digite apenas números.");
        } catch (Exception e) {
            exibirErro("Erro ao cancelar rotação: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void visualizarTodasRotacoes() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                 TODAS AS ROTAÇÕES DO SISTEMA                 ║");
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
                    String taxistaInfo = "N/A";
                    if (r.getTaxist() != null) {
                        if (r.getTaxist().getId() == taxist.getId()) {
                            taxistaInfo = ">>> VOCÊ <<<";
                        } else {
                            taxistaInfo = r.getTaxist().getName();
                        }
                    }

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
                System.out.println("Total de rotações no sistema: " + rotacoes.size());

                // Suas rotações vs outras
                long minhasRotacoes = rotacoes.stream()
                        .filter(r -> r.getTaxist() != null && r.getTaxist().getId() == taxist.getId())
                        .count();
                long outrasRotacoes = rotacoes.size() - minhasRotacoes;

                System.out.println("📊 Resumo:");
                System.out.println("   • Suas rotações: " + minhasRotacoes);
                System.out.println("   • Rotações de outros taxistas: " + outrasRotacoes);
            }

        } catch (Exception e) {
            exibirErro("Erro ao visualizar rotações: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void atualizarDadosPessoais() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                   ATUALIZAR MEUS DADOS                       ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            System.out.println("Dados atuais:");
            System.out.println("Login: " + taxist.getLogin());
            System.out.println("Nome: " + (taxist.getName() != null ? taxist.getName() : "Não informado"));
            System.out.println("Email: " + (taxist.getEmail() != null ? taxist.getEmail() : "Não informado"));
            System.out.println();

            System.out.print("Novo nome (deixe vazio para manter atual): ");
            String novoNome = lerEntrada();

            System.out.print("Novo email (deixe vazio para manter atual): ");
            String novoEmail = lerEntrada();

            System.out.print("Nova senha (deixe vazio para manter atual): ");
            String novaSenha = lerEntrada();

            // Aplicar mudanças
            boolean alterado = false;

            if (!novoNome.isEmpty()) {
                taxist.setName(novoNome);
                alterado = true;
            }

            if (!novoEmail.isEmpty()) {
                taxist.setEmail(novoEmail);
                alterado = true;
            }

            if (!novaSenha.isEmpty()) {
                taxist.setSenha(novaSenha);
                alterado = true;
            }

            if (!alterado) {
                System.out.println("ℹ Nenhuma alteração foi feita.");
            } else {
                var taxistaAtualizado = controller.getUserController().atualizarTaxista(taxist);
                if (taxistaAtualizado.isPresent()) {
                    exibirSucesso("Dados atualizados com sucesso!");
                    // Atualizar referência local
                    taxist.setName(taxistaAtualizado.get().getName());
                    taxist.setEmail(taxistaAtualizado.get().getEmail());
                    taxist.setSenha(taxistaAtualizado.get().getSenha());
                } else {
                    exibirErro("Erro ao atualizar dados. Verifique as informações fornecidas.");
                }
            }

        } catch (Exception e) {
            exibirErro("Erro ao atualizar dados: " + e.getMessage());
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
        System.out.println("║            Obrigado por usar o sistema, Taxista!            ║");
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