package projetomps.business_logic.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import projetomps.app_logic.dao.RotationDAO;
import projetomps.app_logic.log.AppLogger;
import projetomps.app_logic.log.AppLoggerFactory;
import projetomps.business_logic.model.Relatorio;
import projetomps.business_logic.model.Rotation;
import projetomps.util.exception.RepositoryException;

public class RelatorioService {
    private static final AppLogger log = AppLoggerFactory.getLogger(RotationService.class);
    private final RotationDAO rotationDAO;

    public RelatorioService(RotationDAO rotationDAO) {
        this.rotationDAO = rotationDAO;
    }

    public Relatorio gerarRelatorio(LocalDate dataInicio, LocalDate dataFim) throws RepositoryException {
        log.info("Gerando relatório.");

        List<Rotation> rotations = this.buscarDados();
        List<Integer> dados = this.processar(rotations, dataInicio, dataFim);
        int totalHoras = dados.get(0);
        int totalDiasFinalDeSemana = dados.get(1);

        String conteudo = String.format(
                "Horas trabalhadas: %d%nDias no final de semana: %d",
                totalHoras, totalDiasFinalDeSemana);

        Relatorio relatorio = new Relatorio();
        relatorio.setDate(LocalDate.now());
        relatorio.setConteudo(conteudo);

        return relatorio;
    }

    /** Exporta usando Template Method (HTML/PDF). */
    public void exportarRelatorio(Relatorio relatorio, String tipo, String caminhoArquivo) {
        RelatorioTemplate template;
        if ("HTML".equalsIgnoreCase(tipo)) {
            template = new RelatorioHTML();
        } else if ("PDF".equalsIgnoreCase(tipo)) {
            template = new RelatorioPDF();
        } else {
            throw new IllegalArgumentException("Tipo de relatório não suportado: " + tipo);
        }
        template.exportar(relatorio, caminhoArquivo);
    }

    private List<Rotation> buscarDados() throws RepositoryException {
        return rotationDAO.buscarTodos();
    }

    private List<Integer> processar(List<Rotation> rotations, LocalDate dataInicio, LocalDate dataFim) {
        int totalHoras = 0;
        int totalDiasFinalDeSemana = 0;

        for (Rotation rotation : rotations) {
            if (rotation.getDate().isBefore(dataInicio) || rotation.getDate().isAfter(dataFim)) {
                continue;
            }
            LocalTime inicio = rotation.getStartTime();
            LocalTime fim = rotation.getEndTime();

            if (fim != null) {
                totalHoras += Duration.between(inicio, fim).toHours();
            }
            DayOfWeek dia = rotation.getDate().getDayOfWeek();
            if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
                totalDiasFinalDeSemana++;
            }
        }

        List<Integer> lista = new ArrayList<>();
        lista.add(totalHoras);
        lista.add(totalDiasFinalDeSemana);
        return lista;
    }
}