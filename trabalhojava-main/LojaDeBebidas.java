
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LojaDeBebidas {

    private ArrayList<Produto> catalogo;
    private Map<String, Integer> quantidadesEstoque;

    public LojaDeBebidas() {
        catalogo = new ArrayList<>();
        quantidadesEstoque = new HashMap<>();
        carregarProdutos();
    }

    private void carregarProdutos() {
        ProdutoCSV.ListarProduto(this); // A loja se preenche automaticamente com os dados do CSV
    }

    public void cadastrarNovoProduto(Scanner sc) {
        System.out.print("Nome da bebida: ");
        String nome = sc.nextLine();

        System.out.print("Preço: ");
        double preco = sc.nextDouble();

        System.out.print("Volume (ml): ");
        int volume = sc.nextInt();

        sc.nextLine(); // consumir quebra de linha

        String temperatura = selecionarTemperaturaPorIndice(sc);
        String tipo;
        Produto produto;

        System.out.println("Tipo da bebida:");
        System.out.println("1 - Alcoólica");
        System.out.println("2 - Não Alcoólica");
        System.out.print("Escolha: ");
        int escolhaTipo = sc.nextInt();
        sc.nextLine();

        if (escolhaTipo == 1) {
            tipo = "Alcoolica";
            produto = new BebidaAlcoolica(nome, preco, volume, temperatura);
        } else {
            tipo = "NaoAlcoolica";
            produto = new BebidaNaoAlcoolica(nome, preco, volume, temperatura);
        }

        System.out.print("Quantidade em estoque: ");
        int quantidade = sc.nextInt();
        sc.nextLine();

        adicionarProdutoAoEstoque(produto, quantidade);
        ProdutoCSV.AddProduto(produto, tipo, quantidade);
        System.out.println("Produto cadastrado com sucesso!\n");
    }

    public void adicionarProdutoAoEstoque(Produto produto, int quantidade) {
        catalogo.add(produto);
        quantidadesEstoque.put(produto.getNome(), quantidade);
    }

    public void verificarDisponibilidade(Produto produto, int quantidadeDesejada) throws EstoqueInsu {
        int estoqueAtual = quantidadesEstoque.getOrDefault(produto.getNome(), 0);

        if (quantidadeDesejada > estoqueAtual) {
            throw new EstoqueInsu(
                    "Estoque insuficiente para '" + produto.getNome() + "'. Disponível: " + estoqueAtual
            );
        }
    }

    public ArrayList<Produto> listarBebidas(String tipo, String temperatura) {
        ArrayList<Produto> listaFiltrada = new ArrayList<>();
        for (Produto p : catalogo) {
            if ((tipo.equals("alcoolica") && p instanceof BebidaAlcoolica)
                    || (tipo.equals("naoalcoolica") && p instanceof BebidaNaoAlcoolica)) {
                if (p instanceof BebidaAlcoolica ba && ba.getTemperatura().equalsIgnoreCase(temperatura)) {
                    listaFiltrada.add(ba);
                } else if (p instanceof BebidaNaoAlcoolica bna && bna.getTemperatura().equalsIgnoreCase(temperatura)) {
                    listaFiltrada.add(bna);
                }
            }
        }
        // Sempre faz escolha - 1 porque, internamente, a lista ainda começa em 0, mas o usuário enxerga ela começando em 1.
        for (int i = 0; i < listaFiltrada.size(); i++) { 
            System.out.print((i + 1) + " - ");
            listaFiltrada.get(i).exibirInfo();
            System.out.println("    (Estoque: " + quantidadesEstoque.get(listaFiltrada.get(i).getNome()) + ")");
        }
        return listaFiltrada;
    }

    public String selecionarTemperaturaPorIndice(Scanner sc) {
        System.out.println("Selecione a temperatura da bebida:");
        System.out.println("1 - Gelada");
        System.out.println("2 - Ambiente (Quente)");
        System.out.print("Opção: ");
        int escolha = sc.nextInt();
        sc.nextLine();

        if (escolha == 1) {
            return "Gelada"; 
        }else if (escolha == 2) {
            return "Ambiente"; 
        }else {
            return "";
        }
    }

    public Produto buscarProduto(String nome) {
        for (Produto p : catalogo) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }
}
