package projetomps.business_logic.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import projetomps.app_logic.dao.RotationDAO;
import projetomps.app_logic.log.AppLogger;
import projetomps.app_logic.log.AppLoggerFactory;
import projetomps.business_logic.model.Relatorio;
import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.model.Taxist;
import projetomps.util.exception.RepositoryException;

public class RelatorioService {
    private static final AppLogger log = AppLoggerFactory.getLogger(RelatorioService.class);
    private final RotationDAO rotationDAO;

    public RelatorioService(RotationDAO rotationDAO) {
        this.rotationDAO = rotationDAO;
    }

    public Relatorio gerarRelatorioGeral() throws RepositoryException {
        log.info("Gerando relatório geral.");

        List<Rotation> rotations = buscarDados();
        String conteudo = processarRelatorioGeral(rotations);

        Relatorio relatorio = new Relatorio();
        relatorio.setDate(LocalDate.now());
        relatorio.setTipo("GERAL");
        relatorio.setTitulo("Relatório Geral do Sistema");
        relatorio.setConteudo(conteudo);

        return relatorio;
    }

    public Relatorio gerarRelatorioSemanal(LocalDate dataReferencia) throws RepositoryException {
        log.info("Gerando relatório semanal para a semana de: {}", dataReferencia);

        LocalDate inicioSemana = dataReferencia.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate fimSemana = inicioSemana.plusDays(6);

        List<Rotation> rotations = buscarDados().stream()
                .filter(r -> !r.getDate().isBefore(inicioSemana) && !r.getDate().isAfter(fimSemana))
                .collect(Collectors.toList());

        String conteudo = processarRelatorioSemanal(rotations, inicioSemana, fimSemana);

        Relatorio relatorio = new Relatorio();
        relatorio.setDate(LocalDate.now());
        relatorio.setTipo("SEMANAL");
        relatorio.setTitulo("Relatório Semanal - " + inicioSemana.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " a " + fimSemana.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        relatorio.setConteudo(conteudo);

        return relatorio;
    }

    public Relatorio gerarRelatorioMensal(int mes, int ano) throws RepositoryException {
        log.info("Gerando relatório mensal para: {}/{}", mes, ano);

        LocalDate inicioMes = LocalDate.of(ano, mes, 1);
        LocalDate fimMes = inicioMes.with(TemporalAdjusters.lastDayOfMonth());

        List<Rotation> rotations = buscarDados().stream()
                .filter(r -> !r.getDate().isBefore(inicioMes) && !r.getDate().isAfter(fimMes))
                .collect(Collectors.toList());

        String conteudo = processarRelatorioMensal(rotations, inicioMes, fimMes);

        Relatorio relatorio = new Relatorio();
        relatorio.setDate(LocalDate.now());
        relatorio.setTipo("MENSAL");
        relatorio.setTitulo("Relatório Mensal - " + inicioMes.format(DateTimeFormatter.ofPattern("MMMM/yyyy")));
        relatorio.setConteudo(conteudo);

        return relatorio;
    }

    public void exportarRelatorio(Relatorio relatorio, String formato, String caminhoArquivo) {
        RelatorioTemplate template;

        if ("HTML".equalsIgnoreCase(formato)) {
            template = new RelatorioHTML();
        } else if ("PDF".equalsIgnoreCase(formato)) {
            template = new RelatorioPDF();
        } else {
            throw new IllegalArgumentException("Formato não suportado: " + formato + ". Use HTML ou PDF.");
        }

        template.exportar(relatorio, caminhoArquivo);
        log.info("Relatório exportado em {} para: {}", formato, caminhoArquivo);
    }

    private List<Rotation> buscarDados() throws RepositoryException {
        return rotationDAO.buscarTodos();
    }

    private String processarRelatorioGeral(List<Rotation> rotations) {
        StringBuilder sb = new StringBuilder();

        // Estatísticas gerais
        int totalRotacoes = rotations.size();
        long rotacoesConfirmadas = rotations.stream().filter(r -> "CONFIRMED".equals(r.getStatus())).count();
        long rotacoesPendentes = rotations.stream().filter(r -> "PENDING".equals(r.getStatus())).count();
        long rotacoesCanceladas = rotations.stream().filter(r -> "CANCELLED".equals(r.getStatus())).count();

        // Horas trabalhadas
        long totalHoras = calcularHorasTrabalhadas(rotations);

        // Taxistas únicos
        long taxistasAtivos = rotations.stream()
                .filter(r -> r.getTaxist() != null)
                .map(r -> r.getTaxist().getId())
                .distinct()
                .count();

        // Construir relatório
        sb.append("=== ESTATÍSTICAS GERAIS ===\n");
        sb.append("Total de rotações cadastradas: ").append(totalRotacoes).append("\n");
        sb.append("Rotações confirmadas: ").append(rotacoesConfirmadas).append("\n");
        sb.append("Rotações pendentes: ").append(rotacoesPendentes).append("\n");
        sb.append("Rotações canceladas: ").append(rotacoesCanceladas).append("\n");
        sb.append("Taxistas ativos: ").append(taxistasAtivos).append("\n");
        sb.append("Total de horas trabalhadas: ").append(totalHoras).append("h\n\n");

        // Percentuais
        if (totalRotacoes > 0) {
            sb.append("=== PERCENTUAIS ===\n");
            sb.append("Taxa de confirmação: ").append(String.format("%.1f%%", (rotacoesConfirmadas * 100.0) / totalRotacoes)).append("\n");
            sb.append("Taxa de cancelamento: ").append(String.format("%.1f%%", (rotacoesCanceladas * 100.0) / totalRotacoes)).append("\n");
            sb.append("Taxa pendente: ").append(String.format("%.1f%%", (rotacoesPendentes * 100.0) / totalRotacoes)).append("\n\n");
        }

        // Top taxistas
        sb.append(gerarTopTaxistas(rotations));

        return sb.toString();
    }

    private String processarRelatorioSemanal(List<Rotation> rotations, LocalDate inicio, LocalDate fim) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== PERÍODO ===\n");
        sb.append("Início: ").append(inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy (EEEE)"))).append("\n");
        sb.append("Fim: ").append(fim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy (EEEE)"))).append("\n\n");

        // Estatísticas da semana
        sb.append("=== ESTATÍSTICAS DA SEMANA ===\n");
        sb.append("Total de rotações: ").append(rotations.size()).append("\n");
        sb.append("Horas trabalhadas: ").append(calcularHorasTrabalhadas(rotations)).append("h\n");

        long finsDeSemanaTrabalhados = rotations.stream()
                .filter(r -> r.getDate().getDayOfWeek() == DayOfWeek.SATURDAY || r.getDate().getDayOfWeek() == DayOfWeek.SUNDAY)
                .count();
        sb.append("Rotações em fins de semana: ").append(finsDeSemanaTrabalhados).append("\n\n");

        // Rotações por dia da semana
        sb.append("=== ROTAÇÕES POR DIA ===\n");
        Map<DayOfWeek, Long> rotacoesPorDia = rotations.stream()
                .collect(Collectors.groupingBy(r -> r.getDate().getDayOfWeek(), Collectors.counting()));

        for (DayOfWeek dia : DayOfWeek.values()) {
            long count = rotacoesPorDia.getOrDefault(dia, 0L);
            sb.append(dia.name()).append(": ").append(count).append(" rotações\n");
        }

        sb.append("\n").append(gerarDetalhesRotacoes(rotations));

        return sb.toString();
    }

    private String processarRelatorioMensal(List<Rotation> rotations, LocalDate inicio, LocalDate fim) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== PERÍODO ===\n");
        sb.append("Mês: ").append(inicio.format(DateTimeFormatter.ofPattern("MMMM/yyyy"))).append("\n");
        sb.append("Total de dias: ").append(fim.getDayOfMonth()).append("\n\n");

        // Estatísticas do mês
        sb.append("=== ESTATÍSTICAS DO MÊS ===\n");
        sb.append("Total de rotações: ").append(rotations.size()).append("\n");
        sb.append("Horas trabalhadas: ").append(calcularHorasTrabalhadas(rotations)).append("h\n");

        // Média diária
        long diasUteis = contarDiasUteis(inicio, fim);
        if (diasUteis > 0) {
            double mediaRotacoesPorDia = rotations.size() / (double) diasUteis;
            sb.append("Média de rotações por dia útil: ").append(String.format("%.1f", mediaRotacoesPorDia)).append("\n");
        }

        // Rotações por status
        sb.append("\n=== ROTAÇÕES POR STATUS ===\n");
        Map<String, Long> rotacoesPorStatus = rotations.stream()
                .collect(Collectors.groupingBy(Rotation::getStatus, Collectors.counting()));

        rotacoesPorStatus.forEach((status, count) ->
                sb.append(status).append(": ").append(count).append("\n"));

        sb.append("\n").append(gerarTopTaxistas(rotations));
        sb.append("\n").append(gerarResumoSemanal(rotations, inicio));

        return sb.toString();
    }

    private String gerarTopTaxistas(List<Rotation> rotations) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== TOP TAXISTAS ===\n");

        Map<String, Long> rotacoesPorTaxista = rotations.stream()
                .filter(r -> r.getTaxist() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getTaxist().getName() != null ? r.getTaxist().getName() : r.getTaxist().getLogin(),
                        Collectors.counting()
                ));

        rotacoesPorTaxista.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" rotações\n"));

        return sb.toString();
    }

    private String gerarDetalhesRotacoes(List<Rotation> rotations) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DETALHES DAS ROTAÇÕES ===\n");

        rotations.stream()
                .sorted((r1, r2) -> r1.getDate().compareTo(r2.getDate()))
                .forEach(r -> {
                    sb.append("Data: ").append(r.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                            .append(" | Horário: ").append(r.getStartTime())
                            .append(" - ").append(r.getEndTime() != null ? r.getEndTime() : "Em andamento")
                            .append(" | Status: ").append(r.getStatus())
                            .append(" | Taxista: ").append(r.getTaxist() != null ? r.getTaxist().getLogin() : "N/A")
                            .append("\n");
                });

        return sb.toString();
    }

    private String gerarResumoSemanal(List<Rotation> rotations, LocalDate inicioMes) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESUMO POR SEMANA ===\n");

        LocalDate inicioSemana = inicioMes.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        int semanaCount = 1;

        while (inicioSemana.getMonth() == inicioMes.getMonth() ||
                (inicioSemana.isBefore(inicioMes.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1)))) {

            LocalDate fimSemana = inicioSemana.plusDays(6);

            // Corrected part: create effectively final copies
            final LocalDate finalInicioSemana = inicioSemana;
            final LocalDate finalFimSemana = fimSemana;

            long rotacoesSemana = rotations.stream()
                    .filter(r -> !r.getDate().isBefore(finalInicioSemana) && !r.getDate().isAfter(finalFimSemana))
                    .count();

            sb.append("Semana ").append(semanaCount).append(" (")
                    .append(inicioSemana.format(DateTimeFormatter.ofPattern("dd/MM")))
                    .append(" - ").append(fimSemana.format(DateTimeFormatter.ofPattern("dd/MM")))
                    .append("): ").append(rotacoesSemana).append(" rotações\n");

            inicioSemana = inicioSemana.plusDays(7);
            semanaCount++;

            if (semanaCount > 6) break;
        }

        return sb.toString();
    }

    private long calcularHorasTrabalhadas(List<Rotation> rotations) {
        return rotations.stream()
                .filter(r -> r.getStartTime() != null && r.getEndTime() != null)
                .mapToLong(r -> Duration.between(r.getStartTime(), r.getEndTime()).toHours())
                .sum();
    }

    private long contarDiasUteis(LocalDate inicio, LocalDate fim) {
        long diasUteis = 0;
        LocalDate atual = inicio;

        while (!atual.isAfter(fim)) {
            if (atual.getDayOfWeek() != DayOfWeek.SATURDAY && atual.getDayOfWeek() != DayOfWeek.SUNDAY) {
                diasUteis++;
            }
            atual = atual.plusDays(1);
        }

        return diasUteis;
    }
}