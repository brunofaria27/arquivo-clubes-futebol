import java.io.*;

import java.util.Scanner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class Main {
    public static void fileCheck() {
        FileOutputStream arq;
        DataOutputStream dos;

        boolean exists = (new File("clube.db")).exists();

        if (exists) {
            // Arquivo já existe
        } else {
            try {
                int id = -1;

                arq = new FileOutputStream("clube.db");
                dos = new DataOutputStream(arq);
                dos.writeInt(id);
                arq.close();
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }

    public static void menu() {
        System.out.println("\n1 - Criar time");
        System.out.println("2 - Criar partida");
        System.out.println("3 - Procurar time");
        System.out.println("4 - Deletar time");
        System.out.println("5 - Atualizar dados do time");
        
        System.out.println("0 - Sair");
    }

    public static void main(String[] args) {
        fileCheck();
        Scanner sc = new Scanner(System.in);
        
        byte opcao;
        
        FileInputStream arq2;
        DataInputStream dis;

        byte id = -1;

        //Faz a leitura do último ID usado que é o primeiro dado escrito no arquivo, se for a primeira vez de uso, o id está inicializado como -1 e construirá o primeiro objeto com id=0
        try {
            arq2 = new FileInputStream("clube.db");
            dis = new DataInputStream(arq2);
            id = dis.readByte();

            arq2.close();
        } catch(IOException e) {

        }

        do {
            menu();

            System.out.print("Digite sua opção: ");
            opcao = sc.nextByte();

            if (opcao != 0) {
                if (opcao == 1) {
                    String nome;
                    String cnpj;
                    String cidade;
                    byte partidasJogadas;
                    byte pontos;
                    
                    id++;

                    sc.nextLine(); //fazendo a captura do "enter" para não ter problema na leitura dos dados de criação

                    System.out.print("Nome: ");
                    nome = sc.nextLine();

                    System.out.print("CNPJ: ");
                    cnpj = sc.nextLine();

                    System.out.print("Cidade: ");
                    cidade = sc.nextLine();

                    Clube c = new Clube(id, nome, cnpj, cidade);

                    byte[] ba;

                    try {
                        FileOutputStream arq = new FileOutputStream("clube.db", true);
                        DataOutputStream dos = new DataOutputStream(arq);

                        ba = c.toByteArray();

                        dos.writeInt(ba.length);
                        dos.write(ba);

                        arq.close();
                    } catch (IOException e) {
                        //: handle exception
                    }
                } else {
                    Clube c2 = new Clube();
                    Clube c3 = new Clube();

                    try {
                        byte[] ba;

                        arq2 = new FileInputStream("clube.db");
                        dis = new DataInputStream(arq2);

                        int tam;

                        dis.readInt();

                        tam = dis.readInt();
                        ba = new byte[tam];
                        dis.read(ba);
                        c2.fromByteArray(ba);

                        tam = dis.readInt();
                        ba = new byte[tam];
                        dis.read(ba);
                        c3.fromByteArray(ba);


                        System.out.println(c2.toString());
                        System.out.println(c3.toString());

                        arq2.close();
                    } catch (IOException e) {
                        //: handle exception
                    }
                }
            }
        } while (opcao != 0);

    }
}