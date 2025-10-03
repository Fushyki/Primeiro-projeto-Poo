
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ProdutoCSV {

    private static String nomeArquivo = "dados.csv";

    public static void AddProduto(Produto p, String tipo, int quantidade) {
        try {
            File arquivo = new File(nomeArquivo);
            boolean arquivoExiste = arquivo.exists();

            FileWriter escritor = new FileWriter(nomeArquivo, StandardCharsets.ISO_8859_1, true);

            if (!arquivoExiste) {
                escritor.write("Nome;Preço;Volume;Temperatura;Tipo;Quantidade\n");
            } else if (arquivo.length() > 0) {
                escritor.write("\n"); // Garante quebra de linha
            }

            String linha = p.getNome() + ";" + p.getPreco() + ";" + p.getVolume() + ";";

            if (p instanceof BebidaAlcoolica) {
                linha += ((BebidaAlcoolica) p).getTemperatura() + ";Alcoolica;";
            } else if (p instanceof BebidaNaoAlcoolica) {
                linha += ((BebidaNaoAlcoolica) p).getTemperatura() + ";NaoAlcoolica;";
            }

            linha += quantidade;
            escritor.write(linha);
            escritor.flush();
            escritor.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Produto> ListarProduto(LojaDeBebidas loja) {
        ArrayList<Produto> lista = new ArrayList<>();
        try (BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = leitor.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                if (linha.trim().isEmpty()) {
                    continue; // Pula linha em branco
                }

                String[] partes = linha.split(";");
                if (partes.length < 6) {
                    System.out.println("Linha inválida: " + linha);
                    continue; // Ignora linha com menos campos do que o esperado
                }

                String nome = partes[0];
                double preco = Double.parseDouble(partes[1]);
                int volume = Integer.parseInt(partes[2]);
                String temperatura = partes[3];
                String tipo = partes[4];
                int quantidade = Integer.parseInt(partes[5]);

                Produto p;
                if (tipo.equalsIgnoreCase("Alcoolica")) {
                    p = new BebidaAlcoolica(nome, preco, volume, temperatura);
                } else {
                    p = new BebidaNaoAlcoolica(nome, preco, volume, temperatura);
                }

                lista.add(p);
                loja.adicionarProdutoAoEstoque(p, quantidade);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
