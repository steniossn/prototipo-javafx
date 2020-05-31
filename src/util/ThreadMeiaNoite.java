/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import condominio.fx.FXMLPrincipal;

/**
 * essa thread é utilizada para fazer o programa criar um arquivo novo na pasta
 * de relatorio com a data atual do sistema quando der meia noite ela serve para
 * evitar um erro na criação do relatorio que nao registrava uma entrada sem
 * saida ao dar meia noite caso o botão: entrada/saida/tabRelatorio não fossem
 * usados
 *
 * @author portaria
 */
public class ThreadMeiaNoite extends Thread {

    public static Boolean EXECUTAR = true;
    private String nome;
    int tempo;
    Boolean controlador;
    
    public ThreadMeiaNoite(){
    }

    public ThreadMeiaNoite(String nome) {
        this.nome = nome;
        ThreadMeiaNoite t = new ThreadMeiaNoite();
        t.start();
        
    }

    @Override
    public void run() {
//criar ação da thread
//pegar hora do sistema e calcular tempo restante para meia noite 
//desiginar tempo restante em milesegundos
//loop de checagem 

        if (EXECUTAR) {
            try {

                String dataHora = Metodos.DataHora("apenasHora");
                int minutos = Integer.parseInt(Metodos.DataHora("minutos")) * 60000;

                int esperar = 60000;

                if (!dataHora.equals("0")) {
                    //subtrai de 24 horas o tempo que resta para dar meia noite
                    esperar = (24 - Integer.parseInt(dataHora)) * 3600000 - minutos;
                }

                while (EXECUTAR) {
                    System.out.println("esperando dar tempo do sleep " + esperar / 60000);
                    //fica parado aqui pelo tempo do esperar
                    Thread.sleep(esperar);
                    //vai checar se existe o arquivo 
                    controlador = true;
                    while (controlador && EXECUTAR) {
                        System.out.println("thread executando..." + esperar++);
                        Thread.sleep(60000);
                        dataHora = Metodos.DataHora("apenasHora");
                        if (dataHora.equals("0")) {
                            //vai checar se existe o arquivo 
                            FXMLPrincipal.RAIZ.criarArquivoEntradaSaida();
                            //muda o tempo de pausa no primeiro loop
                            esperar = 23 * 3600000;
                            //sair desse loop
                            controlador = false;
                        }

                    }

                }

            } catch (InterruptedException | NumberFormatException e) {

                //criar log aqui
                Metodos.menssageErro(e);
            }
        }
        System.out.println("fechou a thread");

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
    
}
