package projetomps.business_logic.controller;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import projetomps.business_logic.model.Relatorio;
import projetomps.business_logic.service.RelatorioService;
import projetomps.util.exception.RepositoryException;

@AllArgsConstructor
public class RelatorioController {
    private RelatorioService relatorioService;

    public Relatorio getRelatorio(LocalDate dataInicio, LocalDate dataFim) throws RepositoryException {
        return relatorioService.gerarRelatorio(dataInicio, dataFim);
    }

    public void baixarRelatorio(LocalDate inicio, LocalDate fim, String tipo, String caminho) throws RepositoryException {
        Relatorio relatorio = relatorioService.gerarRelatorio(inicio, fim);
        relatorioService.exportarRelatorio(relatorio, tipo, caminho);
    }
}