package projetomps.business_logic.service;

import projetomps.business_logic.model.Relatorio;

public abstract class RelatorioTemplate {

    // Template Method principal
    public final void exportar(Relatorio relatorio, String caminhoArquivo) {
        if (usarAbordagemString()) {
            exportarViaString(relatorio, caminhoArquivo);
        } else {
            exportarViaConstrucaoDireta(relatorio, caminhoArquivo);
        }
    }

    // Abordagem baseada em string (para HTML, TXT, CSV, etc.)
    private void exportarViaString(Relatorio relatorio, String caminhoArquivo) {
        String cabecalho = gerarCabecalho(relatorio);
        String corpo = gerarCorpo(relatorio);
        String rodape = gerarRodape(relatorio);

        String conteudoFinal = cabecalho + corpo + rodape;
        escreverArquivo(conteudoFinal, caminhoArquivo);
    }

    // Abordagem de construção direta (para PDF, Excel, etc.)
    private void exportarViaConstrucaoDireta(Relatorio relatorio, String caminhoArquivo) {
        inicializarDocumento(relatorio, caminhoArquivo);
        try {
            construirCabecalho(relatorio);
            construirCorpo(relatorio);
            construirRodape(relatorio);
        } finally {
            finalizarDocumento();
        }
    }

    /**
     * Define qual abordagem usar.
     * @return true para usar strings (HTML, TXT), false para construção direta (PDF)
     */
    protected abstract boolean usarAbordagemString();

    protected String gerarCabecalho(Relatorio relatorio) {
        throw new UnsupportedOperationException("Implementar gerarCabecalho() ou usar abordagem de construção direta");
    }

    protected String gerarCorpo(Relatorio relatorio) {
        throw new UnsupportedOperationException("Implementar gerarCorpo() ou usar abordagem de construção direta");
    }

    protected String gerarRodape(Relatorio relatorio) {
        throw new UnsupportedOperationException("Implementar gerarRodape() ou usar abordagem de construção direta");
    }

    protected void escreverArquivo(String conteudo, String caminhoArquivo) {
        throw new UnsupportedOperationException("Implementar escreverArquivo() ou usar abordagem de construção direta");
    }

    protected void inicializarDocumento(Relatorio relatorio, String caminhoArquivo) {
        throw new UnsupportedOperationException("Implementar inicializarDocumento() ou usar abordagem string");
    }

    protected void construirCabecalho(Relatorio relatorio) {
        throw new UnsupportedOperationException("Implementar construirCabecalho() ou usar abordagem string");
    }

    protected void construirCorpo(Relatorio relatorio) {
        throw new UnsupportedOperationException("Implementar construirCorpo() ou usar abordagem string");
    }

    protected void construirRodape(Relatorio relatorio) {
        throw new UnsupportedOperationException("Implementar construirRodape() ou usar abordagem string");
    }

    protected void finalizarDocumento() {
        throw new UnsupportedOperationException("Implementar finalizarDocumento() ou usar abordagem string");
    }
}