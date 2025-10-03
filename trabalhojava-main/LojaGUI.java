import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LojaGUI extends JFrame {

    private LojaDeBebidas loja;
    private ArrayList<Produto> carrinho = new ArrayList<>();
    private ArrayList<Integer> quantidades = new ArrayList<>();

    private JTextField nomeField;
    private JTextField idadeField;
    private JTextArea outputArea;
    private Cliente cliente;

    public LojaGUI() {
        loja = new LojaDeBebidas();

        setTitle("Distribuidora JavaBebidas");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel de cadastro do cliente
        JPanel cadastroPanel = new JPanel();
        cadastroPanel.setLayout(new FlowLayout());

        nomeField = new JTextField(10);
        idadeField = new JTextField(3);
        JButton cadastrarBtn = new JButton("Cadastrar Cliente");

        cadastroPanel.add(new JLabel("Nome:"));
        cadastroPanel.add(nomeField);
        cadastroPanel.add(new JLabel("Idade:"));
        cadastroPanel.add(idadeField);
        cadastroPanel.add(cadastrarBtn);

        add(cadastroPanel, BorderLayout.NORTH);

        // Área de saída
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);
        add(scroll, BorderLayout.CENTER);

        // Painel de botões
        JPanel botoesPanel = new JPanel();
        botoesPanel.setLayout(new GridLayout(2, 2));

        JButton listarAlcoolicaBtn = new JButton("Listar Alcoólicas");
        JButton listarNaoAlcoolicaBtn = new JButton("Listar Não Alcoólicas");
        JButton finalizarBtn = new JButton("Finalizar Compra");
        JButton verCarrinhoBtn = new JButton("Ver Carrinho");

        botoesPanel.add(listarAlcoolicaBtn);
        botoesPanel.add(listarNaoAlcoolicaBtn);
        botoesPanel.add(verCarrinhoBtn);
        botoesPanel.add(finalizarBtn);

        add(botoesPanel, BorderLayout.SOUTH);

        // Eventos
        cadastrarBtn.addActionListener(e -> cadastrarCliente());
        listarAlcoolicaBtn.addActionListener(e -> listarBebidas(true));
        listarNaoAlcoolicaBtn.addActionListener(e -> listarBebidas(false));
        verCarrinhoBtn.addActionListener(e -> mostrarCarrinho());
        finalizarBtn.addActionListener(e -> finalizarCompra());

        setVisible(true);
    }

    private void cadastrarCliente() {
        String nome = nomeField.getText();
        int idade;

        try {
            idade = Integer.parseInt(idadeField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Idade inválida.");
            return;
        }

        cliente = new Cliente(nome, idade);
        outputArea.setText("Cliente cadastrado:\n" + cliente + "\n");
    }

    private void listarBebidas(boolean alcoolica) {
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Cadastre um cliente primeiro.");
            return;
        }

        if (alcoolica && cliente.getIdade() < 18) {
            JOptionPane.showMessageDialog(this, "Você não pode comprar bebidas alcoólicas.");
            return;
        }

        String temperatura = (String) JOptionPane.showInputDialog(this,
                "Temperatura:", "Escolha",
                JOptionPane.QUESTION_MESSAGE,
                null, new String[]{"Gelada", "Ambiente"}, "Gelada");

        if (temperatura == null) return;

        String tipo = alcoolica ? "alcoolica" : "naoalcoolica";
        ArrayList<Produto> lista = loja.listarBebidas(tipo, temperatura);

        if (lista.isEmpty()) {
            outputArea.setText("Nenhuma bebida disponível.\n");
            return;
        }

        StringBuilder sb = new StringBuilder("Bebidas disponíveis:\n");
        for (int i = 0; i < lista.size(); i++) {
            sb.append((i + 1)).append(" - ").append(lista.get(i).getNome()).append(" - R$")
              .append(lista.get(i).getPreco()).append("\n");
        }

        String input = JOptionPane.showInputDialog(this, sb.toString() + "\nEscolha o número da bebida:");
        if (input == null) return;

        try {
            int escolha = Integer.parseInt(input) - 1;
            if (escolha >= 0 && escolha < lista.size()) {
                Produto produto = lista.get(escolha);
                String qtdStr = JOptionPane.showInputDialog(this, "Quantidade:");
                if (qtdStr == null) return;
                int qtd = Integer.parseInt(qtdStr);

                loja.verificarDisponibilidade(produto, qtd);
                carrinho.add(produto);
                quantidades.add(qtd);
                outputArea.setText("Produto adicionado ao carrinho.\n");

            } else {
                outputArea.setText("Escolha inválida.\n");
            }
        } catch (NumberFormatException | EstoqueInsu ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void mostrarCarrinho() {
        StringBuilder sb = new StringBuilder("Carrinho:\n");
        double total = 0;

        for (int i = 0; i < carrinho.size(); i++) {
            Produto p = carrinho.get(i);
            int qtd = quantidades.get(i);
            sb.append(qtd).append("x ").append(p.getNome()).append(" - R$")
              .append(p.getPreco()).append("\n");
            total += p.getPreco() * qtd;
        }

        sb.append("\nTotal: R$").append(total);
        outputArea.setText(sb.toString());
    }

    private void finalizarCompra() {
        if (carrinho.isEmpty()) {
            outputArea.setText("Carrinho vazio.\n");
            return;
        }

        double totalFinal = 0;
        for (int i = 0; i < carrinho.size(); i++) {
            Produto p = carrinho.get(i);
            int qtd = quantidades.get(i);
            totalFinal += p.getPreco() * qtd;
        }

        carrinho.clear();
        quantidades.clear();

        outputArea.setText("Compra finalizada. Total: R$" + totalFinal + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LojaGUI::new);
    }
}
