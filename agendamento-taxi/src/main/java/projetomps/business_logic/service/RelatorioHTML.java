package projetomps.business_logic.service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import projetomps.business_logic.model.Relatorio;

public class RelatorioHTML extends RelatorioTemplate {

    @Override
    protected boolean usarAbordagemString() {
        return true; // HTML usa a abordagem baseada em strings
    }

    @Override
    protected String gerarCabecalho(Relatorio relatorio) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"pt-BR\">\n");
        sb.append("<head>\n");
        sb.append("    <meta charset=\"UTF-8\">\n");
        sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        sb.append("    <title>").append(escaparHTML(relatorio.getTitulo())).append("</title>\n");
        sb.append("    <style>\n");
        sb.append(CSS_STYLES);
        sb.append("    </style>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("    <div class=\"container\">\n");
        sb.append("        <header class=\"header\">\n");
        sb.append("            <div class=\"header-title\">\n");
        sb.append("                 <h1>Relatório Gerencial</h1>\n");
        sb.append("                 <h2>SISTEMA AGENDATÁXI</h2>\n");
        sb.append("            </div>\n");
        sb.append("            <div class=\"header-info\">\n");
        sb.append("                <p><strong>Tipo:</strong> ").append(escaparHTML(relatorio.getTipo())).append("</p>\n");
        sb.append("                <p><strong>Gerado em:</strong> ").append(relatorio.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("</p>\n");
        sb.append("            </div>\n");
        sb.append("        </header>\n");
        return sb.toString();
    }

    @Override
    protected String gerarCorpo(Relatorio relatorio) {
        StringBuilder sb = new StringBuilder();
        sb.append("        <main class=\"content\">\n");

        String[] linhas = relatorio.getConteudo().split("\n");
        boolean emSecao = false;

        for (String linha : linhas) {
            linha = linha.trim();
            if (linha.isEmpty()) continue;

            if (linha.startsWith("===") && linha.endsWith("===")) {
                if (emSecao) {
                    sb.append("            </div>\n");
                }
                String titulo = escaparHTML(linha.replace("===", "").trim());
                sb.append("            <div class=\"section\">\n");
                sb.append("                <h2>").append(titulo).append("</h2>\n");
                emSecao = true;
            } else if (linha.contains(":") && !linha.startsWith("Data:") && !linha.startsWith("Semana")) {
                String[] partes = linha.split(":", 2);
                if (partes.length == 2) {
                    sb.append("                <div class=\"data-row\">\n");
                    sb.append("                    <span class=\"label\">").append(escaparHTML(partes[0].trim())).append(":</span>\n");
                    sb.append("                    <span class=\"value\">").append(escaparHTML(partes[1].trim())).append("</span>\n");
                    sb.append("                </div>\n");
                }
            } else {
                sb.append("                <p class=\"paragraph\">").append(escaparHTML(linha)).append("</p>\n");
            }
        }

        if (emSecao) {
            sb.append("            </div>\n");
        }

        sb.append("        </main>\n");
        return sb.toString();
    }

    @Override
    protected String gerarRodape(Relatorio relatorio) {
        StringBuilder sb = new StringBuilder();
        sb.append("        <footer class=\"footer\">\n");
        sb.append("            <p>&copy; ").append(java.time.Year.now().getValue()).append(" Sistema AgendaTáxi. Todos os direitos reservados.</p>\n");
        sb.append("        </footer>\n");
        sb.append("    </div>\n");
        sb.append("</body>\n");
        sb.append("</html>");
        return sb.toString();
    }

    @Override
    protected void escreverArquivo(String conteudo, String caminhoArquivo) {
        try (FileWriter writer = new FileWriter(caminhoArquivo, StandardCharsets.UTF_8)) {
            writer.write(conteudo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever arquivo HTML: " + e.getMessage(), e);
        }
    }

    private String escaparHTML(String texto) {
        if (texto == null) return "";
        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static final String CSS_STYLES = """
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap');
        
        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f7f9;
            color: #333;
            line-height: 1.6;
        }
        .container {
            max-width: 900px;
            margin: 0 auto;
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
            overflow: hidden;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 24px 32px;
            border-bottom: 1px solid #e0e0e0;
            background-color: #ffffff;
        }
        .header-title h1 {
            margin: 0;
            font-size: 1.5em;
            font-weight: 700;
            color: #2d3e50;
        }
        .header-title h2 {
            margin: 4px 0 0 0;
            font-size: 0.9em;
            font-weight: 400;
            color: #888;
        }
        .header-info {
            text-align: right;
            font-size: 0.85em;
            color: #555;
        }
        .header-info p {
            margin: 0;
        }
        .content {
            padding: 32px;
        }
        .section {
            margin-bottom: 32px;
            padding: 24px;
            background-color: #fdfdfd;
            border: 1px solid #e0e0e0;
            border-left: 4px solid #3498db;
            border-radius: 6px;
        }
        .section:last-child {
            margin-bottom: 0;
        }
        .section h2 {
            margin: 0 0 20px 0;
            font-size: 1.2em;
            font-weight: 600;
            color: #2d3e50;
            padding-bottom: 10px;
            border-bottom: 1px solid #eee;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .data-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid #f0f0f0;
            font-size: 0.95em;
        }
        .data-row:last-child {
            border-bottom: none;
        }
        .label {
            color: #555;
        }
        .value {
            font-weight: 600;
            color: #2d3e50;
        }
        .paragraph {
             font-size: 0.95em;
             color: #444;
        }
        .footer {
            background-color: #2d3e50;
            color: #ffffff;
            padding: 16px;
            text-align: center;
            font-size: 0.8em;
        }
        .footer p {
            margin: 0;
        }
        @media (max-width: 680px) {
            body {
                padding: 0;
            }
            .container {
                margin: 0;
                border-radius: 0;
                box-shadow: none;
            }
            .header {
                flex-direction: column;
                align-items: flex-start;
                gap: 16px;
            }
            .header-info {
                text-align: left;
                width: 100%;
            }
            .content, .section, .header {
                padding: 20px;
            }
        }
        """;
}