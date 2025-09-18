package projetomps.business_logic.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import projetomps.business_logic.model.Relatorio;

public class RelatorioPDF extends RelatorioTemplate {

    @Override
    protected String gerarCabecalho(Relatorio relatorio) {
        return "Relatório - " + relatorio.getDate() + "\n\n";
    }

    @Override
    protected String gerarCorpo(Relatorio relatorio) {
        return relatorio.getConteudo() + "\n\n";
    }

    @Override
    protected String gerarRodape(Relatorio relatorio) {
        return "Gerado automaticamente pelo sistema.";
    }

    @Override
    protected void escreverArquivo(String conteudo, String caminhoArquivo) {
        try (PdfWriter writer = new PdfWriter(caminhoArquivo);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph(conteudo));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}