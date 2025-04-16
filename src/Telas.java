import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Telas extends JFrame {
    private JTextField nomeField;
    private Color corEscolhida = Color.GREEN;

    public Telas() {
        setTitle("Bem-vindo ao Snake Game");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel painel = new JPanel(new BorderLayout());

        ImageIcon logoIcon = new ImageIcon("assets\\telainicial.png");
        JLabel logoLabel = new JLabel(logoIcon);
        painel.add(logoLabel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));

        JLabel nomeLabel = new JLabel("Nome do Jogador");
        infoPanel.add(nomeLabel);

        nomeField = new JTextField();
        infoPanel.add(nomeField);

        JButton escolherCorBotao = new JButton("Escolher Cor");
        escolherCorBotao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                escolherCor();
            }
        });
        infoPanel.add(escolherCorBotao);

        JButton iniciarBotao = new JButton("Iniciar Jogo");
        iniciarBotao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeJogador = nomeField.getText();

                if (!nomeJogador.isEmpty()) {
                    dispose();
                    iniciarJogo(nomeJogador);
                } else {
                    JOptionPane.showMessageDialog(null, "Digite seu nome antes de iniciar o jogo.");
                }
            }
        });

        infoPanel.add(iniciarBotao);

        painel.add(infoPanel, BorderLayout.SOUTH);

        add(painel);
    }

    private void escolherCor() {
        EscolherCorDialog dialog = new EscolherCorDialog(this, corEscolhida);
        dialog.setVisible(true);

        corEscolhida = dialog.getCorEscolhida();
    }

    private void iniciarJogo(String nomeJogador) {
        Jogo jogo = new Jogo(600, 600, nomeJogador, corEscolhida);
        JFrame frame = new JFrame("Snake Game");
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(jogo);
        frame.pack();
        jogo.requestFocus();
        frame.setVisible(true);
    }
}