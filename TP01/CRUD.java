import java.io.*;

public class CRUD {
    private RandomAccessFile arq;
    private final String nomeArquivo = "clube.db";

    /**
     * Cria a classe e o arquivo, caso ele não exista, adicionando o ID = -1, para o primeiro objeto ter o ID = 0
     */
    public CRUD() {
        try {
            boolean exists = (new File(nomeArquivo)).exists();

            if (exists) {
                // Arquivo já existe
            } else {
                try {
                    int id = -1;

                    arq = new RandomAccessFile(nomeArquivo, "rw");
                    arq.writeInt(id);
                    arq.close();
                } catch (Exception e) {
                    System.out.println("Erro para criar e inicializar o id no arquivo!");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro! " + e.getMessage());
        }
    }

    /**
     * Criação de um Clube de futebol dentro do arquivo
     * @param c -> objeto criado pelo usuário
     * @param id -> id do objeto criado, para ser adicionado no inicio do arquivo
     */
    public void create(Clube c, int id) {
        byte[] ba;

        try {
            arq = new RandomAccessFile(nomeArquivo, "rw");

            ba = c.toByteArray();
            arq.seek(0); // Movendo o ponteiro para o início do arquivo para atualizar o valor do ID
            arq.writeInt(id);
            arq.seek(arq.length());
            arq.writeChar(' '); // Escritura da Lápide do registro
            arq.writeInt(ba.length);
            arq.write(ba);
            arq.close();
        } catch (IOException e) {
            System.out.println("Erro para inserir o time no arquivo!");
        }
    }

    /**
     * 
     * @param c -> novo objeto Clube para ser substituido
     * @return boolean (true para atualizado, false para erro)
     */
    public boolean update(Clube c) {
        try {
            long pos;
            char lapide;
            byte[] b;
            byte[] novoB;
            int tam;
            Clube objeto;

            arq = new RandomAccessFile(nomeArquivo, "rw");

            arq.seek(4);

            while(arq.getFilePointer() < arq.length()) {
                pos = arq.getFilePointer();
                lapide = arq.readChar();
                tam = arq.readInt();
                b = new byte[tam];
                arq.read(b);
                if (lapide != '*') {
                    objeto = new Clube();
                    objeto.fromByteArray(b);
                    if (objeto.getId() == c.getId()) {
                        novoB = c.toByteArray();

                        if (novoB.length <= tam) {
                            arq.seek(pos + 6); // Utilizado pos + 6 para que não atualize em cima da lápide nem do tamanho
                            arq.write(novoB);
                            arq.close();                            
                        } else {
                            arq.seek(arq.length());
                            arq.writeChar(' ');
                            arq.writeInt(novoB.length);
                            arq.write(novoB);
                            delete(objeto.getId());
                            arq.close();
                        }
                    
                        return true;
                    }
                }
            }
            arq.close();
            return false;

        } catch (Exception e) {
            return false;
        }
        
    }

    /**
     * Deleta cliente do arquivo
     * @param id Id do cliente a ser removido
     */
    public boolean delete(byte id) {
        try {
            long pos;
            char lapide;
            int tam;
            byte[] b;
            Clube clube;

            arq = new RandomAccessFile(nomeArquivo, "rw");
            arq.seek(4); // Pular cabeçalho

            while(arq.getFilePointer() < arq.length()) {
                pos = arq.getFilePointer();
                lapide = arq.readChar();
                tam = arq.readInt();
                b = new byte[tam];
                arq.read(b);
                if(lapide != '*') {
                    clube = new Clube();
                    clube.fromByteArray(b);
                    if(clube.getId() == id) {
                        arq.seek(pos);
                        arq.writeChar('*');
                        arq.close();
                        return true;
                    }
                }
            }
            arq.close();
            return false;
        } catch(IOException e) {
            System.out.println("Erro ao atualizar lápide (deletar) time!");
            return false;
        }
        
    }

    /**
     * Metódo utilizado para retornar o time de futebol e suas caracteristicas desejadas, buscando pelo ID
     * @param id -> id passado pelo usuário para retornar o clube
     */
    public boolean readById(byte id) {
        try {
            arq = new RandomAccessFile(nomeArquivo, "rw");

            char lapide;
            byte[] b;
            int tam;
            Clube objeto;

            arq.seek(4);

            while (arq.getFilePointer() < arq.length()) {
                lapide = arq.readChar();
                tam = arq.readInt();
                b = new byte[tam];
                arq.read(b);

                if (lapide != '*') {
                    objeto = new Clube();
                    objeto.fromByteArray(b);

                    if (objeto.getId() == id) {
                        System.out.println(objeto);
                        return true;
                    }
                }
            }
            return false;

        } catch (IOException e) {
            System.out.println("Não foi possível encontrar o time desejado!");
            return false;
        }
    }

    /**
     * Lê um clube do arquivo pelo nome inserido pelo usuário
     * @param s
     * @return object (clube pesquisado), caso não ache o clube null
     */
    public Clube readByName(String s) {
        try {
            arq = new RandomAccessFile("clube.db", "rw");

            Clube c;
            byte[] ba;
            int tam;

            arq.seek(4);

            while (arq.getFilePointer() < arq.length()) {
                char lapide = arq.readChar();
                tam = arq.readInt();
                ba = new byte[tam];
                arq.read(ba);

                if (lapide != '*') {
                    c = new Clube();
                    c.fromByteArray(ba);
                    if (c.nome.equals(s)) {
                        return c;
                    }
                }
            }

            return null;
        } catch (IOException e) {
            System.out.println("Não foi possível encontrar o time desejado!");
            return null;
        }
    }

    public void matchGenerator(String t1, String t2, int golsT1, int golsT2) {
        Clube c1 = readByName(t1);
        Clube c2 = readByName(t2);

        c1.increaseMatches();
        c2.increaseMatches();

        if (golsT1 > golsT2) {
            c1.updPoints(3);
        } else if (golsT1 < golsT2) {
            c2.updPoints(3);
        } else {
            c1.updPoints(1);
            c2.updPoints(1);
        }
        if (update(c1) && update(c2)) {
            System.out.println("\nPartida registrada e dados alterados com sucesso!");
        } else {
            System.out.println("\nNão foi possível registrar a partida e/ou alterar os dados!");
        }
    }


    /**
     * Imprime todos os clubes do arquivo para o usuário
     */
    public void readAll() {
        try {
            arq = new RandomAccessFile(nomeArquivo, "rw");
            arq.seek(4);

            char lapide;
            byte[] b;
            int tam;
            Clube objeto;

            while (arq.getFilePointer() < arq.length()) {
                lapide = arq.readChar();
                tam = arq.readInt();
                b = new byte[tam];
                arq.read(b);
                if (lapide != '*') {
                    objeto = new Clube();
                    objeto.fromByteArray(b);
                    System.out.println(objeto.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao imprimir todos os times!");
        }
    }

}
