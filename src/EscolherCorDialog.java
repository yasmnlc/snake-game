import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

class EscolherCorDialog extends JDialog {
    private Color corEscolhida;

    public EscolherCorDialog(JFrame parent, Color corAtual) {
        super(parent, "Escolher Cor", true);
        corEscolhida = corAtual;

        JColorChooser colorChooser = new JColorChooser(corAtual);

        JButton confirmarBotao = new JButton("Confirmar");
        confirmarBotao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                corEscolhida = colorChooser.getColor();
                dispose();
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(colorChooser, BorderLayout.CENTER);
        panel.add(confirmarBotao, BorderLayout.SOUTH);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public Color getCorEscolhida() {
        return corEscolhida;
    }
}