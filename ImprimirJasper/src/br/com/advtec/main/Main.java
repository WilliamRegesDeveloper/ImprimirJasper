package br.com.advtec.main;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import br.com.conexao.SingletonConexaoFactory;
import br.com.relatorio.RelatorioFactory;

/**
* classe integrada a um ERP onde o mesmo envia parametros para 
  o args no objeto Main e esse transforma esse args em parametro
  de entrada para os critérios de uma query
*/
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if (args == null) {
            JOptionPane.showMessageDialog(null, "Nao tem parametro", "Insira parametros de entrada", 0);
            System.exit(0);
            return;
        }
		
        String nomerelatorio = args[1];
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        
        if (args.length > 2) {

            for (int x = 2; x != args.length - 2; ++x) {
                String[] re = null;
                try {
                    re = new Main().valor(args[x]);
                }

                catch (Exception ex) {}

                if (re == null) {
                    break;
                }
                map.put(re[0], re[1]);

            }

        }
        
        Connection connection = SingletonConexaoFactory.get();   
		HashMap<String,String> mapCaminhos = RelatorioFactory.getNomeRelatorioReport(nomerelatorio+".jasper");


		try {
			
			long nomeExport = System.currentTimeMillis();
			/**
			 * Busca o relatório na pasta, associa um map de parametros e conecta ao banco de dados para lançar a query
			 **/
			JasperPrint jasperPrint = JasperFillManager.fillReport(mapCaminhos.get(RelatorioFactory.PATH_ARQUIVO), map, connection);
			Exporter<ExporterInput,PdfReportConfiguration,PdfExporterConfiguration,OutputStreamExporterOutput> jrPdfExporter = new JRPdfExporter();
			jrPdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			jrPdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(mapCaminhos.get(RelatorioFactory.PATH_CACHE)+"/"+nomeExport+".pdf"));
			jrPdfExporter.exportReport();
						
			Runtime.getRuntime().exec("cmd.exe /C "+mapCaminhos.get(RelatorioFactory.PATH_CACHE)+"/"+nomeExport+".pdf");
			

		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public String[] valor(final String s) {

        if (s == null) {
            return null;
        }

        if (s.equals("")) {
            return null;
        }

        System.out.println("Rodando loop");

        final String campo = s.substring(s.indexOf("<") + 1, s.indexOf(">"));
        final String valor = s.substring(s.indexOf("[") + 1, s.indexOf("]"));
        final String[] r = new String[2];

        System.out.println("Campo: " + campo + " valor: " + valor);

        r[0] = campo;
        r[1] = valor;

        return r;

    }

}
