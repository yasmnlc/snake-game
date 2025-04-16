public class Jogador extends Elementos {
    private String nome;
    private int pontuacao;

    public Jogador(String nome, int pontuacao, int x, int y) {
        super(x, y);
        this.nome = nome;
        this.pontuacao = pontuacao;
    }

    public String getNome() {
        return nome;
    }

    public int getPontuacao() {
        return pontuacao;
    }
}
