// REQUISITO: Classe Abstrata 
// REQUISITO: Método Abstrato 
// REQUISITO: Encapsulamento

public abstract class Produto {

    protected String nome;
    protected double preco;
    protected int volume;

    public Produto(String nome, double preco, int volume) {
        this.nome = nome;
        this.preco = preco;
        this.volume = volume;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public int getVolume() {
        return volume;
    }

    public abstract void exibirInfo();
}
