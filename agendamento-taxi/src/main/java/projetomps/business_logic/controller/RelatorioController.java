package projetomps.business_logic.controller;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import projetomps.business_logic.model.Relatorio;
import projetomps.business_logic.service.RelatorioService;
import projetomps.util.exception.RepositoryException;

@AllArgsConstructor
public class RelatorioController {
    private final RelatorioService relatorioService;

    public Relatorio gerarRelatorioGeral() throws RepositoryException {
        return relatorioService.gerarRelatorioGeral();
    }

    public Relatorio gerarRelatorioSemanal(LocalDate dataReferencia) throws RepositoryException {
        return relatorioService.gerarRelatorioSemanal(dataReferencia);
    }

    public Relatorio gerarRelatorioMensal(int mes, int ano) throws RepositoryException {
        return relatorioService.gerarRelatorioMensal(mes, ano);
    }

    //Exporta relatório em formato HTML ou PDF
    public void exportarRelatorio(Relatorio relatorio, String formato, String caminhoArquivo)
            throws RepositoryException {
        try {
            relatorioService.exportarRelatorio(relatorio, formato, caminhoArquivo);
        } catch (Exception e) {
            throw new RepositoryException("Erro ao exportar relatório: " + e.getMessage(), e);
        }
    }
}