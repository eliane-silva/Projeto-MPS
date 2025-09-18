package projetomps.business_logic.service;

import projetomps.business_logic.model.Relatorio;

/**
 * Template Method para exportação de relatórios.
 */
public abstract class RelatorioTemplate {

    // Template Method
    public final void exportar(Relatorio relatorio, String caminhoArquivo) {
        String cabecalho = gerarCabecalho(relatorio);
        String corpo = gerarCorpo(relatorio);
        String rodape = gerarRodape(relatorio);

        String conteudoFinal = cabecalho + corpo + rodape;
        escreverArquivo(conteudoFinal, caminhoArquivo);
    }

    protected abstract String gerarCabecalho(Relatorio relatorio);
    protected abstract String gerarCorpo(Relatorio relatorio);
    protected abstract String gerarRodape(Relatorio relatorio);

    protected abstract void escreverArquivo(String conteudo, String caminhoArquivo);
}