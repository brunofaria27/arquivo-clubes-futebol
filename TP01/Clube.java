import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Clube {
    protected byte id;
    protected String nome;
    protected String cnpj;
    protected String cidade;
    protected byte partidasJogadas;
    protected byte pontos;

    public Clube(byte i, String n, String cnpj, String c) {
        this.id = i;
        this.nome = n;
        this.cnpj = cnpj;
        this.cidade = c;
        this.partidasJogadas = 0;
        this.pontos = 0;
    }

    public Clube() {
        this.id = -1;
        this.nome = "";
        this.cnpj = "";
        this.cidade = "";
        this.partidasJogadas = 0;
        this.pontos = 0;
    }

    public byte getId() {
        return id;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.write(id);
        dos.writeUTF(nome);
        dos.writeUTF(cnpj);
        dos.writeUTF(cidade);
        dos.write(partidasJogadas);
        dos.write(pontos);
        return baos.toByteArray();
    }
    
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readByte();
        nome = dis.readUTF();
        cnpj = dis.readUTF();
        cidade = dis.readUTF();
        partidasJogadas = dis.readByte();
        pontos = dis.readByte();
    }

    public String toString() {
        return "\nID: " + id + "\nNome clube: " + nome + "\nCNPJ: " + cnpj + "\nCidade: " + cidade + "\nPartidas jogadas: " + partidasJogadas + "\nPontos acumulados: " + pontos;
    }
}