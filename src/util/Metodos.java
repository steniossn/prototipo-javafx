package util;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.time.Month;
import java.util.Arrays;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author portaria
 */
public class Metodos {

    //Paths 
    //usar essa para testes 
    public static final Path PASTAPRINCIPAL = Paths.get(recuperaRaiz() + "CondominioFxTeste/Dados/" + FileSystems.getDefault().getSeparator());
    
    
    //public static final Path PASTAPRINCIPAL = Paths.get(recuperaRaiz() + "CondominioFx/Dados/" + FileSystems.getDefault().getSeparator());
    public static Path PASTADADOSAPP = Paths.get(PASTAPRINCIPAL + "/DadosApp/" + FileSystems.getDefault().getSeparator());
    public static Path PASTARELATORIO = Paths.get(PASTAPRINCIPAL + "/Relatorio/" + FileSystems.getDefault().getSeparator());
    public static Path CASAS = Paths.get(PASTAPRINCIPAL + "/Casas/" + FileSystems.getDefault().getSeparator());
    public static Path SERVICOS = Paths.get(PASTAPRINCIPAL + "/Servicos/" + FileSystems.getDefault().getSeparator());
    
    public static Calendar CALENDARIO; 
    public static DateFormat DATAFORMAT; 
    public static DateFormat FORMATHORA;
   

    public static String recuperaRaiz() {

        Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();// 
        for (Path path : dirs) { // for percorre todos os diretorios armazenados no array path
            return path.toString();
        }
        return null;
    }

    //retorna o Paths de relatorio C:\Condominio\Dados\Relatorio\ + nome do arquivo
    public static Path relatorio() {
        String[] lbl = dataHora("data").split("/");
        int d = Integer.parseInt(lbl[0]);
        int m = Integer.parseInt(lbl[1]);
        int a = Integer.parseInt(lbl[2]);
        // essa nova string é criada para que o zero a esquerda seja retirado da data
        String dataConvertida = String.valueOf(String.valueOf(Integer.toString(d) + ".") + Integer.toString(m) + ".") + Integer.toString(a);

        Path relatorioF = Paths.get(PASTARELATORIO + FileSystems.getDefault().getSeparator() + dataConvertida + ".txt");
        return relatorioF;

    }

    //cria as pastas do programa
    public static void criarPastas() {

        try {

            //cria a pasta principal "Condominio" e a pasta "Dados"
            if (!Files.exists(PASTAPRINCIPAL)) {
                Files.createDirectories(PASTAPRINCIPAL);
            }
            //cria a pasta "relatorio"
            if (!Files.exists(PASTADADOSAPP)) {
                Files.createDirectory(PASTADADOSAPP);
            }
            //cria a pasta "DadosApp"
            if (!Files.exists(PASTARELATORIO)) {
                Files.createDirectory(PASTARELATORIO);
            }
            if (!Files.exists(CASAS)) {
                Files.createDirectory(CASAS);
            }
            if (!Files.exists(SERVICOS)) {
                Files.createDirectory(SERVICOS);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "pastas do programa não encontradas");
        }

    }

    /**
     *
     * @param string
     * @return hora completa /data / hora / minutos 
     */
    public static String dataHora(String string) {        
        //gera string com a data
        CALENDARIO = Calendar.getInstance();
        DATAFORMAT = DateFormat.getDateInstance();
        FORMATHORA =  DateFormat.getTimeInstance();
        
        String data = DATAFORMAT.format(CALENDARIO.getTime());       
        //string com a hora
        String hora = FORMATHORA.format(CALENDARIO.getTime());
        // inteiro de hora
        int apenasHora = CALENDARIO.get(Calendar.HOUR_OF_DAY);
        // inteiro de minutos
        int minutos = CALENDARIO.get(Calendar.MINUTE);

        switch (string) {
            case "hora":
                return hora;
            case "data":
                return data;
            case "apenasHora":
                return Integer.toString(apenasHora);
            case "minutos":
                return Integer.toString(minutos);

        }
        return null;
    }

    /**
     * recebe uma data e gera uma string com uma data que pode ser usada no
     * programa
     *
     * @param data
     * @return String com data modificada
     */
    public static String mudaDatas(String data) {
        String[] dataRecebida = data.split("/");
        int d = Integer.parseInt(dataRecebida[0]);
        int m = Integer.parseInt(dataRecebida[1]);
        int a = Integer.parseInt(dataRecebida[2]);

        String novaData = Integer.toString(d) + "." + Integer.toString(m) + "." + Integer.toString(a);

        return novaData;
    }

    public static Month verificaMes(int mes) {
        if (mes > 12) {
            return null;
        }
        Month[] month = {null, Month.JANUARY, Month.FEBRUARY, Month.MARCH,
            Month.APRIL, Month.MAY, Month.JUNE, Month.JULY,
            Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER,
            Month.NOVEMBER, Month.DECEMBER};

        return month[mes];

    }

    /**
     * *
     * @param arquivo local do arquivo a ser lido
     * @return uma String dos bytes lidos do arquivo informado
     */
    public static String lerString(String arquivo) {
        try {
            // .../Condominio/Dados/arquivo 
            Path path = Paths.get(PASTAPRINCIPAL + FileSystems.getDefault().getSeparator() + arquivo); //CAMIMNHO

            if (Files.exists(path)) {
                byte[] bytes = Files.readAllBytes(path); // ARRAY DE BYTE QUE RECEBE OS DADOS DO ARQUIVO DE TXT
                String txtbytes = new String(bytes); // PASSA O ARRAY DE BYTE PARA STRING
                return txtbytes;
            } else {
                return null;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro de leitura do arquivo ou arquivo nao existe");

        }
        return null;
    }

    public static Image iconPadrao() {
        URL url = Metodos.class.getResource("/imagens/icon/casa.png");
        Image iconeTitulo = Toolkit.getDefaultToolkit().getImage(url);
        return iconeTitulo;
    }
    
    public static void menssageErro(Exception e) {
        System.out.println("mensagem do erro; "+e.getMessage());
        System.out.println("local do erro; " +Arrays.toString(e.getStackTrace()));
    }
 
}
