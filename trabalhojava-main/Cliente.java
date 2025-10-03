import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Cliente {
    private static int proximoId = 1;
    private int id;
    private LocalDate dataCadastro;

    private String nome;
    private int idade;

    public Cliente(String nome, int idade) {
        this.id = proximoId++;
        this.nome = nome;
        this.idade = idade;
        this.dataCadastro = LocalDate.now();
    }

    public String getNome() { return nome; }
    public int getIdade() { return idade; }
    public int getId() { return id; }
    public LocalDate getDataCadastro() { return dataCadastro; }

    @Override
    public String toString() {
        return "Cliente [ID=" + id + ", Nome=" + nome + ", Idade=" + idade +
               ", Cadastrado em=" + dataCadastro.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "]";
    }
}
