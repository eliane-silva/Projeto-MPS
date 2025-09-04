package projetomps.view;

import projetomps.controller.FacadeSingletonController;
import projetomps.model.Rotation;
import projetomps.model.Taxist;
import projetomps.util.exception.RepositoryException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class TaxistView {
    private final FacadeSingletonController controller;
    private final Scanner scanner;
    private final Taxist taxist;

    public TaxistView(FacadeSingletonController controller, Scanner scanner, Taxist taxist) {
        this.controller = controller;
        this.scanner = scanner;
        this.taxist = taxist;
    }

    public void exibirMenuPrincipal() {
        boolean continuar = true;

        limparTela();
        exibirCabecalhoTaxista();

        while (continuar) {
            try {
                exibirOpcoes();
                int opcao = lerOpcao();

                switch (opcao) {
                    case 1:
                        agendarRodizio();
                        break;
                    case 2:
                        consultarHorariosDisponiveis();
                        break;
                    case 3:
                        listarMinhasRotacoes();
                        break;
                    case 4:
                        cancelarAgendamento();
                        break;
                    case 5:
                        consultarMinhasEstatisticas();
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
        System.out.println("║                    PAINEL DO TAXISTA                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("👤 Usuário: " + taxist.getLogin() + " (Taxista)");
        if (taxist.getName() != null && !taxist.getName().isEmpty()) {
            System.out.println("📝 Nome: " + taxist.getName());
        }
        System.out.println();
    }

    private void exibirOpcoes() {
        System.out.println("┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│                        MENU TAXISTA                          │");
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Agendar Rodízio                                          │");
        System.out.println("│  2. Consultar Horários Disponíveis                           │");
        System.out.println("│  3. Minhas Rotações                                          │");
        System.out.println("│  4. Cancelar Agendamento                                     │");
        System.out.println("│  5. Consultar Minhas Estatísticas                            │");
        System.out.println("│  0. Logout                                                   │");
        System.out.println("└──────────────────────────────────────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }

    private void agendarRodizio() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                     AGENDAR RODÍZIO                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("ℹ Instruções:");
            System.out.println("• A data deve ser futura (não pode ser hoje ou no passado)");
            System.out.println("• Use o formato AAAA-MM-DD para a data");
            System.out.println("• Use o formato HH:MM para os horários");
            System.out.println();

            System.out.print("📅 Digite a data (AAAA-MM-DD): ");
            String dataStr = lerEntrada();
            LocalDate data = LocalDate.parse(dataStr);

            // Validar se a data é futura
            if (!data.isAfter(LocalDate.now())) {
                exibirErro("Não é possível agendar para o dia atual ou passado.");
                pausar();
                return;
            }

            // Mostrar horários disponíveis para a data
            mostrarHorariosDisponiveis(data);
            System.out.println();

            System.out.print("🕐 Digite o horário de início (HH:MM): ");
            String inicioStr = lerEntrada();
            LocalTime inicio = LocalTime.parse(inicioStr);

            System.out.print("🕐 Digite o horário de fim (HH:MM): ");
            String fimStr = lerEntrada();
            LocalTime fim = LocalTime.parse(fimStr);

            // Verificar se o horário está disponível
            if (!verificarHorarioDisponivel(data, inicio)) {
                exibirErro("Horário indisponível. Consulte os horários disponíveis primeiro.");
                pausar();
                return;
            }

            // Verificar agendamento duplicado
            if (verificarAgendamentoDuplicado(data, inicio)) {
                exibirErro("Você já possui um agendamento para este horário.");
                pausar();
                return;
            }

            // Criar rotação
            Rotation novaRotacao = new Rotation();
            novaRotacao.setTaxist(taxist);
            novaRotacao.setDate(data);
            novaRotacao.setStartTime(inicio);
            novaRotacao.setEndTime(fim);
            novaRotacao.setStatus("PENDING");

            if (controller.getRotationController().createRotation(novaRotacao)) {
                exibirSucesso("Rodízio agendado com sucesso!");
                System.out.println("📋 Detalhes do agendamento:");
                System.out.println("   • Data: " + data);
                System.out.println("   • Horário: " + inicio + " às " + fim);
                System.out.println("   • Status: PENDENTE");
            } else {
                exibirErro("Erro ao agendar rodízio.");
            }

        } catch (DateTimeParseException e) {
            exibirErro("Formato de data/hora inválido. Use AAAA-MM-DD para data e HH:MM para hora.");
        } catch (Exception e) {
            exibirErro("Erro ao agendar rodízio: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void consultarHorariosDisponiveis() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                 CONSULTAR HORÁRIOS DISPONÍVEIS               ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            System.out.print("📅 Digite a data para consulta (AAAA-MM-DD): ");
            String dataStr = lerEntrada();
            LocalDate data = LocalDate.parse(dataStr);

            // Validar se a data é futura
            if (!data.isAfter(LocalDate.now())) {
                exibirErro("Consulte apenas datas futuras.");
                pausar();
                return;
            }

            mostrarHorariosDisponiveis(data);

        } catch (DateTimeParseException e) {
            exibirErro("Formato de data inválido. Use AAAA-MM-DD.");
        } catch (Exception e) {
            exibirErro("Erro ao consultar horários: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void mostrarHorariosDisponiveis(LocalDate data) throws RepositoryException {
        System.out.println();
        System.out.println("📅 Horários para " + data + ":");
        System.out.println();

        // Horários candidatos (8h às 20h, de 2 em 2 horas)
        List<LocalTime> horariosCandidatos = List.of(
                LocalTime.of(8, 0), LocalTime.of(10, 0), LocalTime.of(12, 0),
                LocalTime.of(14, 0), LocalTime.of(16, 0), LocalTime.of(18, 0), LocalTime.of(20, 0)
        );

        List<Rotation> todasRotacoes = controller.getRotationController().getAllRotations();
        List<LocalTime> horariosOcupados = todasRotacoes.stream()
                .filter(r -> data.equals(r.getDate()))
                .map(Rotation::getStartTime)
                .filter(t -> t != null)
                .toList();

        System.out.println("┌─────────────┬─────────────────────┐");
        System.out.println("│   HORÁRIO   │       STATUS        │");
        System.out.println("├─────────────┼─────────────────────┤");

        boolean temDisponivel = false;
        for (LocalTime horario : horariosCandidatos) {
            String status = horariosOcupados.contains(horario) ? "🔴 OCUPADO" : "🟢 DISPONÍVEL";
            if (!horariosOcupados.contains(horario)) {
                temDisponivel = true;
            }
            System.out.printf("│    %s    │ %-20s │%n", horario, status);
        }

        System.out.println("└─────────────┴─────────────────────┘");

        if (!temDisponivel) {
            System.out.println();
            System.out.println("ℹ Não há horários disponíveis para esta data.");
        }
    }

    private boolean verificarHorarioDisponivel(LocalDate data, LocalTime inicio) throws RepositoryException {
        List<Rotation> todasRotacoes = controller.getRotationController().getAllRotations();
        return todasRotacoes.stream()
                .noneMatch(r -> data.equals(r.getDate()) && inicio.equals(r.getStartTime()));
    }

    private boolean verificarAgendamentoDuplicado(LocalDate data, LocalTime inicio) throws RepositoryException {
        List<Rotation> minhasRotacoes = controller.getRotationController().getAllRotations()
                .stream()
                .filter(r -> r.getTaxist() != null && r.getTaxist().getId() == taxist.getId())
                .toList();

        return minhasRotacoes.stream()
                .anyMatch(r -> data.equals(r.getDate()) && inicio.equals(r.getStartTime()));
    }

    private void listarMinhasRotacoes() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                      MINHAS ROTAÇÕES                         ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            List<Rotation> minhasRotacoes = controller.getRotationController().getAllRotations()
                    .stream()
                    .filter(r -> r.getTaxist() != null && r.getTaxist().getId() == taxist.getId())
                    .toList();

            if (minhasRotacoes.isEmpty()) {
                System.out.println("ℹ Você não possui rotações agendadas.");
            } else {
                System.out.println("┌─────┬────────────┬─────────┬─────────┬─────────────┐");
                System.out.println("│  #  │    DATA    │ INÍCIO  │   FIM   │   STATUS    │");
                System.out.println("├─────┼────────────┼─────────┼─────────┼─────────────┤");

                for (int i = 0; i < minhasRotacoes.size(); i++) {
                    Rotation r = minhasRotacoes.get(i);
                    String endTime = (r.getEndTime() != null) ? r.getEndTime().toString() : "N/A";
                    String statusIcon = getStatusIcon(r.getStatus());

                    System.out.printf("│ %-3d │ %-10s │ %-7s │ %-7s │ %-11s │%n",
                            i, r.getDate(), r.getStartTime(), endTime, statusIcon + r.getStatus());
                }

                System.out.println("└─────┴────────────┴─────────┴─────────┴─────────────┘");
                System.out.println();
                System.out.println("Total de rotações: " + minhasRotacoes.size());
            }

        } catch (Exception e) {
            exibirErro("Erro ao listar rotações: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void cancelarAgendamento() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    CANCELAR AGENDAMENTO                      ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            List<Rotation> minhasRotacoes = controller.getRotationController().getAllRotations()
                    .stream()
                    .filter(r -> r.getTaxist() != null && r.getTaxist().getId() == taxist.getId())
                    .toList();

            if (minhasRotacoes.isEmpty()) {
                System.out.println("ℹ Você não possui rotações para cancelar.");
                pausar();
                return;
            }

            System.out.println("Seus agendamentos:");
            System.out.println();
            System.out.println("┌─────┬────────────┬─────────┬─────────┬─────────────┐");
            System.out.println("│  #  │    DATA    │ INÍCIO  │   FIM   │   STATUS    │");
            System.out.println("├─────┼────────────┼─────────┼─────────┼─────────────┤");

            for (int i = 0; i < minhasRotacoes.size(); i++) {
                Rotation r = minhasRotacoes.get(i);
                String endTime = (r.getEndTime() != null) ? r.getEndTime().toString() : "N/A";
                String statusIcon = getStatusIcon(r.getStatus());

                System.out.printf("│ %-3d │ %-10s │ %-7s │ %-7s │ %-11s │%n",
                        i, r.getDate(), r.getStartTime(), endTime, statusIcon + r.getStatus());
            }

            System.out.println("└─────┴────────────┴─────────┴─────────┴─────────────┘");
            System.out.println();

            System.out.print("Digite o número do agendamento a ser cancelado (0-" + (minhasRotacoes.size() - 1) + ") ou -1 para voltar: ");
            int index = lerOpcao();

            if (index == -1) {
                System.out.println("ℹ Operação cancelada.");
                pausar();
                return;
            }

            if (index < 0 || index >= minhasRotacoes.size()) {
                exibirErro("Número inválido.");
                pausar();
                return;
            }

            Rotation rotacao = minhasRotacoes.get(index);

            // Verificar se pode cancelar (não pode cancelar agendamentos passados)
            if (rotacao.getDate().isBefore(LocalDate.now()) ||
                    (rotacao.getDate().equals(LocalDate.now()) && rotacao.getStartTime().isBefore(LocalTime.now()))) {
                exibirErro("Não é possível cancelar agendamentos passados.");
                pausar();
                return;
            }

            if (controller.getRotationController().deleteRotation(rotacao)) {
                exibirSucesso("Agendamento cancelado com sucesso!");
            } else {
                exibirErro("Erro ao cancelar agendamento.");
            }

        } catch (NumberFormatException e) {
            exibirErro("Número inválido.");
        } catch (Exception e) {
            exibirErro("Erro ao cancelar agendamento: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private void consultarMinhasEstatisticas() {
        try {
            limparTela();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                    MINHAS ESTATÍSTICAS                       ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            List<Rotation> minhasRotacoes = controller.getRotationController().getAllRotations()
                    .stream()
                    .filter(r -> r.getTaxist() != null && r.getTaxist().getId() == taxist.getId())
                    .toList();

            long total = minhasRotacoes.size();
            long confirmadas = minhasRotacoes.stream().filter(r -> "CONFIRMED".equalsIgnoreCase(r.getStatus())).count();
            long pendentes = minhasRotacoes.stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count();
            long canceladas = minhasRotacoes.stream().filter(r -> "CANCELLED".equalsIgnoreCase(r.getStatus())).count();

            System.out.println("┌──────────────────────────────────────────────────────────────┐");
            System.out.println("│                    RESUMO ESTATÍSTICO                        │");
            System.out.println("├──────────────────────────────────────────────────────────────┤");
            System.out.printf("│ 📊 Total de rotações: %-39s │%n", total);
            System.out.printf("│ ✅ Confirmadas: %-45s │%n", confirmadas);
            System.out.printf("│ ⏳ Pendentes: %-47s │%n", pendentes);
            System.out.printf("│ ❌ Canceladas: %-46s │%n", canceladas);
            System.out.println("└──────────────────────────────────────────────────────────────┘");

            if (total > 0) {
                double percentualConfirmadas = (confirmadas * 100.0) / total;
                System.out.println();
                System.out.printf("📈 Taxa de confirmação: %.1f%%\n", percentualConfirmadas);
            }

        } catch (Exception e) {
            exibirErro("Erro ao consultar estatísticas: " + e.getMessage());
        }

        pausar();
        limparTela();
    }

    private String getStatusIcon(String status) {
        return switch (status.toUpperCase()) {
            case "CONFIRMED" -> "✅ ";
            case "PENDING" -> "⏳ ";
            case "CANCELLED" -> "❌ ";
            default -> "❓ ";
        };
    }

    private void exibirMensagemLogout() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║                     LOGOUT REALIZADO                         ║");
        System.out.println("║                                                              ║");
        System.out.println("║               Obrigado por usar o sistema!                   ║");
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
        // Simula limpeza de tela no terminal
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}