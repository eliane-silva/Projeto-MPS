package projetomps.business_logic.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import projetomps.business_logic.model.Relatorio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RelatorioPDF extends RelatorioTemplate {

    // Paleta de cores
    private static final DeviceRgb COR_PRINCIPAL = new DeviceRgb(45, 62, 80);
    private static final DeviceRgb COR_FUNDO_SECAO = new DeviceRgb(234, 239, 242);
    private static final DeviceRgb COR_BORDA_CABECALHO = new DeviceRgb(200, 200, 200);

    private Document document;
    private PdfDocument pdfDoc;
    private Relatorio relatorioAtual;

    @Override
    protected boolean usarAbordagemString() {
        return false; // PDF usa a abordagem de construção direta
    }

    @Override
    protected void inicializarDocumento(Relatorio relatorio, String caminhoArquivo) {
        try {
            this.relatorioAtual = relatorio;
            PdfWriter writer = new PdfWriter(caminhoArquivo);
            this.pdfDoc = new PdfDocument(writer);
            this.document = new Document(pdfDoc);

            // Adiciona o handler para o rodapé em todas as páginas
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new FooterEventHandler());

            // Define margens, deixando espaço para o rodapé
            document.setMargins(36, 36, 50, 36);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar documento PDF: " + e.getMessage(), e);
        }
    }

    @Override
    protected void construirCabecalho(Relatorio relatorio) {
        try {
            Table tabelaCabecalho = new Table(UnitValue.createPercentArray(new float[]{70, 30}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(20)
                    .setBorderBottom(new SolidBorder(COR_BORDA_CABECALHO, 1));

            // Célula do Título
            Paragraph titulo = new Paragraph("RELATÓRIO GERENCIAL")
                    .setFontColor(COR_PRINCIPAL)
                    .setFontSize(16)
                    .setBold();
            Paragraph subtitulo = new Paragraph("SISTEMA AGENDATÁXI")
                    .setFontColor(ColorConstants.GRAY)
                    .setFontSize(10);

            Cell cellTitulo = new Cell().add(titulo).add(subtitulo)
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);

            tabelaCabecalho.addCell(cellTitulo);

            // Célula da Data
            String dataFormatada = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            Paragraph dataGeracao = new Paragraph("Gerado em:\n" + dataFormatada)
                    .setFontColor(ColorConstants.GRAY)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.RIGHT);

            Cell cellData = new Cell().add(dataGeracao)
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);

            tabelaCabecalho.addCell(cellData);

            document.add(tabelaCabecalho);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao construir cabeçalho PDF: " + e.getMessage(), e);
        }
    }

    @Override
    protected void construirCorpo(Relatorio relatorio) {
        try {
            String[] linhas = relatorio.getConteudo().split("\n");

            for (String linha : linhas) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;

                if (linha.startsWith("===") && linha.endsWith("===")) {
                    adicionarSecao(linha);
                } else if (linha.contains(":") && !linha.startsWith("Data:") && !linha.startsWith("Semana")) {
                    adicionarLinhaDados(linha);
                } else {
                    adicionarParagrafo(linha);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao construir corpo PDF: " + e.getMessage(), e);
        }
    }

    @Override
    protected void construirRodape(Relatorio relatorio) {
        // O rodapé é tratado pelo FooterEventHandler automaticamente
        // Não há necessidade de construção manual
    }

    @Override
    protected void finalizarDocumento() {
        try {
            if (document != null) {
                document.close();
            }
            if (pdfDoc != null) {
                pdfDoc.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao finalizar documento PDF: " + e.getMessage(), e);
        }
    }

    // Métodos auxiliares para construção do PDF
    private void adicionarSecao(String linha) {
        String tituloSecao = linha.replace("===", "").trim().toUpperCase();
        Paragraph secao = new Paragraph(tituloSecao)
                .setFontSize(12)
                .setBold()
                .setFontColor(COR_PRINCIPAL)
                .setBackgroundColor(COR_FUNDO_SECAO)
                .setPadding(8)
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginTop(20)
                .setMarginBottom(10);
        document.add(secao);
    }

    private void adicionarLinhaDados(String linha) {
        String[] partes = linha.split(":", 2);
        if (partes.length == 2) {
            Table tabela = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(5);

            // Célula do Rótulo
            Cell celulaLabel = new Cell()
                    .add(new Paragraph(partes[0].trim()))
                    .setBorder(Border.NO_BORDER)
                    .setPadding(4)
                    .setFontColor(ColorConstants.DARK_GRAY)
                    .setFontSize(10);

            // Célula do Valor
            Cell celulaValue = new Cell()
                    .add(new Paragraph(partes[1].trim()).setBold())
                    .setBorder(Border.NO_BORDER)
                    .setPadding(4)
                    .setFontColor(COR_PRINCIPAL)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.RIGHT);

            tabela.addCell(celulaLabel);
            tabela.addCell(celulaValue);
            document.add(tabela);
        }
    }

    private void adicionarParagrafo(String linha) {
        Paragraph paragrafo = new Paragraph(linha)
                .setFontSize(10)
                .setMultipliedLeading(1.2f)
                .setMarginBottom(5);
        document.add(paragrafo);
    }

    // Classe interna para gerenciar o rodapé
    private static class FooterEventHandler implements IEventHandler {
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            int pageNum = pdfDoc.getPageNumber(page);
            int totalPages = pdfDoc.getNumberOfPages();

            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

            try (Canvas canvas = new Canvas(pdfCanvas, pageSize)) {
                // Informação do sistema no canto inferior esquerdo
                canvas.showTextAligned(
                        "Sistema AgendaTáxi v1.0",
                        pageSize.getLeft() + 36,
                        pageSize.getBottom() + 20,
                        TextAlignment.LEFT
                ).setFontSize(8).setFontColor(ColorConstants.GRAY);

                // Numeração da página no canto inferior direito
                canvas.showTextAligned(
                        String.format("Página %d de %d", pageNum, totalPages),
                        pageSize.getRight() - 36,
                        pageSize.getBottom() + 20,
                        TextAlignment.RIGHT
                ).setFontSize(8).setFontColor(ColorConstants.GRAY);
            }
        }
    }
}