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

    public void ebaixarPDF() {

    }
}
