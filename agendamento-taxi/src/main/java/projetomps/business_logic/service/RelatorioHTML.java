package projetomps.business_logic.service;

import java.io.FileWriter;
import java.io.IOException;

import projetomps.business_logic.model.Relatorio;

public class RelatorioHTML extends RelatorioTemplate {

    @Override
    protected String gerarCabecalho(Relatorio relatorio) {
        return "<html><head><meta charset=\"utf-8\"/>"
             + "<title>Relatório</title></head><body>"
             + "<h1>Relatório - " + relatorio.getDate() + "</h1>";
    }

    @Override
    protected String gerarCorpo(Relatorio relatorio) {
        return "<pre style=\"font-family:inherit;white-space:pre-wrap\">"
             + relatorio.getConteudo()
                         .replace("&","&amp;")
                         .replace("<","&lt;")
                         .replace(">","&gt;")
             + "</pre>";
    }

    @Override
    protected String gerarRodape(Relatorio relatorio) {
        return "<hr/><small>Gerado automaticamente pelo sistema.</small></body></html>";
    }

    @Override
    protected void escreverArquivo(String conteudo, String caminhoArquivo) {
        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            writer.write(conteudo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever arquivo HTML", e);
        }
    }
}