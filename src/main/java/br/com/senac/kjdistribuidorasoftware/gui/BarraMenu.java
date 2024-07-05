package br.com.senac.kjdistribuidorasoftware.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class BarraMenu {

    public static JMenuBar criarBarraMenu(JFrame parentFrame) {
        JMenuBar jmbMenu = new JMenuBar();
        JMenu jMenu1 = new JMenu();
        JMenuItem jMenuItem1 = new JMenuItem();
        JMenuItem jMenuItem2 = new JMenuItem();
        JMenu jMenu2 = new JMenu();
        JMenuItem jMenuItem3 = new JMenuItem();
        JMenuItem jMenuItem4 = new JMenuItem();
        JMenuItem jMenuItem5 = new JMenuItem();
        JMenu jMenu3 = new JMenu();
        JMenuItem jMenuItem6 = new JMenuItem();
        JMenu jMenu4 = new JMenu();
        JMenuItem jMenuItem7 = new JMenuItem();
        JMenu menuSair = new JMenu();

        // Configuração da barra de menu
        jmbMenu.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jmbMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jmbMenu.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jmbMenu.setInheritsPopupMenu(true);

        // Configuração do menu "Vendas"
        jMenu1.setText("Vendas");
        jMenu1.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        jMenu1.setHorizontalTextPosition(SwingConstants.CENTER);
        jMenu1.setMinimumSize(new java.awt.Dimension(150, 19));
        jMenu1.setPreferredSize(new java.awt.Dimension(150, 19));

        jMenuItem1.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jMenuItem1.setText("Pedido de venda");
        jMenu1.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jMenuItem2.setText("Lista de pedidos");
        jMenu1.add(jMenuItem2);

        jmbMenu.add(jMenu1);

        // Configuração do menu "Cadastro"
        jMenu2.setText("Cadastro");
        jMenu2.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        jMenu2.setHorizontalTextPosition(SwingConstants.CENTER);
        jMenu2.setPreferredSize(new java.awt.Dimension(150, 19));

        jMenuItem3.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jMenuItem3.setText("Clientes");
        jMenu2.add(jMenuItem3);

        jMenuItem4.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jMenuItem4.setText("Produtos");
        jMenu2.add(jMenuItem4);

        jMenuItem5.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jMenuItem5.setText("Vendedores");
        jMenu2.add(jMenuItem5);

        jmbMenu.add(jMenu2);

        // Configuração do menu "Finanças"
        jMenu3.setText("Finanças");
        jMenu3.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        jMenu3.setHorizontalTextPosition(SwingConstants.CENTER);
        jMenu3.setPreferredSize(new java.awt.Dimension(150, 19));

        jMenuItem6.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jMenuItem6.setText("Fluxo de Caixa e Comissões");
        jMenu3.add(jMenuItem6);

        jmbMenu.add(jMenu3);

        // Configuração do menu "Controle de Estoque"
        jMenu4.setText("Controle de Estoque");
        jMenu4.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        jMenu4.setHorizontalTextPosition(SwingConstants.CENTER);
        jMenu4.setMinimumSize(new java.awt.Dimension(150, 19));
        jMenu4.setPreferredSize(new java.awt.Dimension(170, 19));

        jMenuItem7.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jMenuItem7.setText("Entrada de Produtos");
        jMenu4.add(jMenuItem7);

        jmbMenu.add(jMenu4);

        // Configuração do menu "Sair"
        menuSair.setText("Sair");
        menuSair.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        menuSair.setHorizontalTextPosition(SwingConstants.CENTER);
        menuSair.setPreferredSize(new java.awt.Dimension(150, 19));
        menuSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int resposta = JOptionPane.showConfirmDialog(parentFrame, "Deseja mesmo Sair?");
                if (resposta == 0) {
                    TelaLogin tela = new TelaLogin();
                    tela.setVisible(true);
                    parentFrame.dispose();
                }
            }
        });

        jmbMenu.add(menuSair);

        // Configurar ações dos menus
        configurarAcoesMenu(parentFrame, jMenuItem1, jMenuItem2, jMenuItem3, jMenuItem4, jMenuItem5, jMenuItem6, jMenuItem7);

        return jmbMenu;
    }

    private static void configurarAcoesMenu(JFrame parentFrame, JMenuItem menuNovoPedido, JMenuItem menuListaPedidos, JMenuItem menuCadastroClientes, JMenuItem menuCadastroProdutos, JMenuItem menuCadastroVendedores, JMenuItem menuFluxoeComissoes, JMenuItem menuEntradaProduto) {
        menuNovoPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                TelaPedido tela = new TelaPedido();
                tela.setVisible(true);
                parentFrame.dispose();
            }
        });

        menuListaPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                TelaListaVendas tela = new TelaListaVendas();
                tela.setVisible(true);
                parentFrame.dispose();
            }
        });

        menuCadastroClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                TelaCadastroClientes tela = new TelaCadastroClientes();
                tela.setVisible(true);
                parentFrame.dispose();
            }
        });

        menuCadastroProdutos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                TelaCadastroProdutos tela = new TelaCadastroProdutos();
                tela.setVisible(true);
                parentFrame.dispose();
            }
        });

        menuCadastroVendedores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                TelaCadastroVendedores tela = new TelaCadastroVendedores();
                tela.setVisible(true);
                parentFrame.dispose();
            }
        });

        menuFluxoeComissoes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                TelaFluxoeComissoes tela = new TelaFluxoeComissoes();
                tela.setVisible(true);
                parentFrame.dispose();
            }
        });

        menuEntradaProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                TelaEntradaProdutos tela = new TelaEntradaProdutos();
                tela.setVisible(true);
                parentFrame.dispose();
            }
        });
    }
}
