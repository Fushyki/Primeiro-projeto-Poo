
//classe derivada de extend

public class EstoqueInsu extends Exception {

    public EstoqueInsu(String mensagem) {
        super(mensagem); // Passa a mensagem de excessão para o pai
    }
}
