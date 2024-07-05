package br.com.senac.kjdistribuidorasoftware.gui;

import br.com.senac.kjdistribuidorasoftware.model.Cliente;
import br.com.senac.kjdistribuidorasoftware.model.Produto;
import br.com.senac.kjdistribuidorasoftware.model.Venda;
import br.com.senac.kjdistribuidorasoftware.model.VendaProduto;
import br.com.senac.kjdistribuidorasoftware.model.Vendedor;
import br.com.senac.kjdistribuidorasoftware.persistence.ProdutoDAO;
import br.com.senac.kjdistribuidorasoftware.persistence.VendaDAO;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TelaPedido extends javax.swing.JFrame {

    private Venda vendaAtual = null;

    public Cliente clienteVenda = null;
    public Vendedor vendedorVenda = null;
    public Date dataAtual = new Date();

    public TelaPedido() {
        initComponents();
        setJMenuBar(BarraMenu.criarBarraMenu(this));
        this.desabilitaComponentes();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = dateFormat.format(dataAtual);
        txtData.setText(dataFormatada);
    }

    public TelaPedido(Venda venda, Cliente cliente, Vendedor vendedor) {
        this.vendaAtual = venda;
        this.clienteVenda = cliente;
        this.vendedorVenda = vendedor;
        initComponents();
        setJMenuBar(BarraMenu.criarBarraMenu(this));
        this.preencheDadosEdicao();
        this.preencheTabelaProdutos(ProdutoDAO.listar());
    }

    public void preencheDadosEdicao() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = dateFormat.format(dataAtual);
        txtData.setText(dataFormatada);
        txtBuscaCliente.setText(vendaAtual.getCliente().getNome());
        txtBuscaVendedor.setText(vendaAtual.getVendedor().getNome());
        this.preencheTabelaVenda(vendaAtual.getProdutos());
        if (vendaAtual.isConcluido()) {
            jcbStatus.setSelectedIndex(1);
        }

        lblSubTotal.setText("R$" + String.valueOf(vendaAtual.getSubTotal()));
        lblFrete.setText("R$" + String.valueOf(vendaAtual.getFrete()));
        lblTotal.setText("R$" + String.valueOf(vendaAtual.getTotal()));

    }

    public Venda getVendaAtual() {
        return vendaAtual;
    }

    public void setVendaAtual(Venda vendaAtual) {
        this.vendaAtual = vendaAtual;
    }

    public void setClienteVenda(Cliente cliente) {
        clienteVenda = cliente;
        desabilitaComponentes();
    }

    public void setVendedorVenda(Vendedor vendedor) {
        vendedorVenda = vendedor;
        desabilitaComponentes();
    }

    private void atualizarTotal() {
        try {
            double frete = vendaAtual.getFrete();
            double subtotal = vendaAtual.calculaTotalVenda(vendaAtual.getProdutos());
            double total = subtotal + frete;

            DecimalFormat df = new DecimalFormat("#.##");
            lblSubTotal.setText("R$ " + df.format(subtotal));
            lblTotal.setText("R$ " + df.format(total));
        } catch (NumberFormatException e) {
            lblTotal.setText("R$ " + vendaAtual.calculaTotalVenda(vendaAtual.getProdutos()));
        }
    }

    public void atualizaCampos() {
        if (clienteVenda != null) {
            txtBuscaCliente.setText(clienteVenda.getNome());
        }

        if (vendedorVenda != null) {
            txtBuscaVendedor.setText(vendedorVenda.getNome());
        }
        this.preencheTabelaProdutos(ProdutoDAO.listar());
    }

    private void desabilitaComponentes() {
        if (clienteVenda == null || vendedorVenda == null) {
            jcbFiltroProduto.setEnabled(false);
            txtBuscaProduto.setEnabled(false);
            btnAdicionar.setEnabled(false);
            btnRemover.setEnabled(false);
            btnSalvar.setEnabled(false);
        } else {
            jcbFiltroProduto.setEnabled(true);
            txtBuscaProduto.setEnabled(true);
            btnAdicionar.setEnabled(true);
            btnRemover.setEnabled(true);
            btnSalvar.setEnabled(true);
        }
    }

    private void preencheTabelaProdutos(List<Produto> produtos) {
        String colunas[] = {"ID", "SKU", "Nome", "Marca", "Preço Unitário", "Estoque"};
        String dados[][] = new String[produtos.size()][colunas.length];
        int i = 0;
        for (Produto p : produtos) {
            double precoVenda;
            if (clienteVenda.isIsBarbearia()) {
                precoVenda = p.getPrecoAtacado();
            } else {
                precoVenda = p.getPrecoVarejo();
            }
            dados[i] = new String[]{
                String.valueOf(p.getId()),
                p.getSku(),
                p.getNome(),
                p.getMarca(),
                "R$" + String.valueOf(precoVenda),
                String.valueOf(p.getEstoque())
            };
            i++;
        }
        DefaultTableModel model = new DefaultTableModel(dados, colunas);

        this.tblProdutos.setModel(model);
    }

    public void preencheTabelaVenda(List<VendaProduto> vendaProdutos) {
        String colunas[] = {"ID", "SKU", "Nome", "Marca", "Preço Unitário", "Quantidade", "Total do Item"};
        String dados[][] = new String[vendaProdutos.size()][colunas.length];
        for (int i = 0; i < vendaProdutos.size(); i++) {
            VendaProduto vp = vendaProdutos.get(i);
            Produto p = vp.getProduto();
            Venda v = vp.getVenda();
            Double precoUnitario;

            if (v.getCliente().isIsBarbearia()) {
                precoUnitario = p.getPrecoAtacado();
            } else {
                precoUnitario = p.getPrecoVarejo();
            }

            dados[i][0] = String.valueOf(p.getId());
            dados[i][1] = p.getSku();
            dados[i][2] = p.getNome();
            dados[i][3] = p.getMarca();
            dados[i][4] = String.valueOf(precoUnitario);
            dados[i][5] = String.valueOf(vp.getQuantidade());
            double totalItem = vp.getQuantidade() * precoUnitario;
            dados[i][6] = String.valueOf(totalItem);
        }

        DefaultTableModel model = new DefaultTableModel(dados, colunas);
        tblItensPedido.setModel(model);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btgTipoCliente = new javax.swing.ButtonGroup();
        jLabel12 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jcbFiltroProduto = new javax.swing.JComboBox<>();
        txtBuscaProduto = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        txtData = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        txtBuscaCliente = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtBuscaVendedor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jcbFormaPagto = new javax.swing.JComboBox<>();
        btnSelecionarCliente = new javax.swing.JButton();
        btnSelecionarVendedor = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProdutos = new javax.swing.JTable();
        btnAdicionar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblItensPedido = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        lblSubTotal = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jcbStatus = new javax.swing.JComboBox<>();
        btnSalvar = new javax.swing.JButton();
        btnFrete = new javax.swing.JButton();
        lblFrete = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();

        jLabel12.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 0, 0));
        jLabel12.setText("* Preencha todos os campos");

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Novo Pedido de Venda");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Copperplate Gothic Bold", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("PEDIDO DE VENDA");

        jLabel7.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Buscar Produto Por:");

        jcbFiltroProduto.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jcbFiltroProduto.setForeground(new java.awt.Color(0, 0, 0));
        jcbFiltroProduto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nome", "SKU", "Marca" }));

        txtBuscaProduto.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtBuscaProdutoCaretUpdate(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        txtData.setEditable(false);
        txtData.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        txtData.setFocusable(false);

        jLabel2.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Data");

        txtBuscaCliente.setFocusable(false);

        jLabel3.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Cliente");

        jLabel6.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Vendedor");

        txtBuscaVendedor.setFocusable(false);

        jLabel5.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Forma de pagamento");

        jcbFormaPagto.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jcbFormaPagto.setForeground(new java.awt.Color(0, 0, 0));
        jcbFormaPagto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione...", "Dinheiro", "Pix", "Cartão", "Boleto" }));

        btnSelecionarCliente.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        btnSelecionarCliente.setForeground(new java.awt.Color(0, 0, 0));
        btnSelecionarCliente.setText("Selecionar");
        btnSelecionarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionarClienteActionPerformed(evt);
            }
        });

        btnSelecionarVendedor.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        btnSelecionarVendedor.setForeground(new java.awt.Color(0, 0, 0));
        btnSelecionarVendedor.setText("Selecionar");
        btnSelecionarVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionarVendedorActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 0, 0));
        jLabel14.setText("* Preencha todos os campos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel3)))
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBuscaCliente)
                            .addComponent(txtBuscaVendedor, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSelecionarCliente)
                            .addComponent(btnSelecionarVendedor)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jcbFormaPagto, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addGap(20, 20, 20)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSelecionarCliente)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSelecionarVendedor)
                    .addComponent(jLabel6)
                    .addComponent(txtBuscaVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jcbFormaPagto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addContainerGap())
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        tblProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "SKU", "Nome", "Marca", "Preço Unitário", "Estoque"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblProdutos);

        btnAdicionar.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        btnAdicionar.setForeground(new java.awt.Color(0, 0, 0));
        btnAdicionar.setText("Adicionar");
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnRemover.setBackground(new java.awt.Color(255, 0, 0));
        btnRemover.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        btnRemover.setForeground(new java.awt.Color(0, 0, 0));
        btnRemover.setText("Remover");
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        tblItensPedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "SKU", "Nome", "Marca", "Preço Unitário", "Quantidade", "Total do Item"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblItensPedido);

        jLabel8.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Subtotal :");

        lblSubTotal.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        lblSubTotal.setForeground(new java.awt.Color(0, 0, 0));
        lblSubTotal.setText("R$ 0,00");

        jLabel11.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Total :");

        lblTotal.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(0, 0, 0));
        lblTotal.setText("R$ 0,00");

        jLabel13.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Status");

        jcbStatus.setFont(new java.awt.Font("Copperplate Gothic Light", 0, 12)); // NOI18N
        jcbStatus.setForeground(new java.awt.Color(0, 0, 0));
        jcbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Em Aberto", "Concluído" }));

        btnSalvar.setBackground(new java.awt.Color(0, 153, 0));
        btnSalvar.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        btnSalvar.setForeground(new java.awt.Color(0, 0, 0));
        btnSalvar.setText("Salvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnFrete.setBackground(new java.awt.Color(204, 204, 204));
        btnFrete.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        btnFrete.setForeground(new java.awt.Color(0, 0, 0));
        btnFrete.setText("Adicionar Frete");
        btnFrete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFreteActionPerformed(evt);
            }
        });

        lblFrete.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        lblFrete.setForeground(new java.awt.Color(0, 0, 0));
        lblFrete.setText("R$ 0,00");

        jLabel15.setFont(new java.awt.Font("Copperplate Gothic Light", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Frete :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbFiltroProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscaProduto))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(252, 252, 252)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25)
                                .addComponent(btnFrete)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jcbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)
                                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFrete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jcbFiltroProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscaProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdicionar)
                    .addComponent(btnRemover))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lblSubTotal)
                    .addComponent(jLabel13)
                    .addComponent(jcbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalvar)
                    .addComponent(btnFrete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(lblFrete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11)
                    .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSelecionarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionarClienteActionPerformed
        TabelaClientes tela = new TabelaClientes(this);
        tela.setVisible(true);

    }//GEN-LAST:event_btnSelecionarClienteActionPerformed

    private void btnSelecionarVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionarVendedorActionPerformed
        TabelaVendedores tabelaVendedores = new TabelaVendedores(this);
        tabelaVendedores.setVisible(true);
    }//GEN-LAST:event_btnSelecionarVendedorActionPerformed

    private void txtBuscaProdutoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtBuscaProdutoCaretUpdate
        String parametro = jcbFiltroProduto.getSelectedItem().toString().toLowerCase();
        String termoBusca = txtBuscaProduto.getText();
        this.preencheTabelaProdutos(ProdutoDAO.buscaProdutos(parametro, termoBusca));
    }//GEN-LAST:event_txtBuscaProdutoCaretUpdate

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        if (tblProdutos.getSelectedRow() != -1) {
            Integer id = Integer.parseInt(tblProdutos.getValueAt(tblProdutos.getSelectedRow(), 0).toString());
            ProdutoDAO produtoDao = new ProdutoDAO();
            Produto produtoSelecionado = produtoDao.obter(id);

            if (vendaAtual == null) {
                vendaAtual = new Venda();
            }

            vendaAtual.setCliente(clienteVenda);
            vendaAtual.setVendedor(vendedorVenda);
            vendaAtual.setFormaPagto(jcbFormaPagto.getSelectedItem().toString());

            String quantidadeStr = JOptionPane.showInputDialog(null, "Informe a quantidade para o produto selecionado:", "Quantidade do Produto", JOptionPane.QUESTION_MESSAGE);

            if (quantidadeStr != null && !quantidadeStr.isEmpty()) {
                try {
                    int quantidade = Integer.parseInt(quantidadeStr);

                    vendaAtual.addProduto(produtoSelecionado, quantidade);

                    JOptionPane.showMessageDialog(null, "Produto adicionado à venda com sucesso!");

                    this.preencheTabelaVenda(vendaAtual.getProdutos());
                    this.atualizarTotal();

                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Quantidade inválida. Por favor, insira um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "A quantidade não pode ser vazia.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione na tabela um produto para adicionar no pedido!", "Erro", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        try {
            if (tblItensPedido.getSelectedRow() >= 0) {
                Integer id = Integer.parseInt(tblItensPedido.getValueAt(tblItensPedido.getSelectedRow(), 0).toString());
                int resposta = JOptionPane.showConfirmDialog(this, "Deseja mesmo excluir o produto selecionado do pedido ?");
                if (resposta == 0) {

                    if (vendaAtual != null) {
                        vendaAtual.removeProduto(id);
                        JOptionPane.showMessageDialog(null, "Produto removido da venda com sucesso!");

                        this.preencheTabelaVenda(vendaAtual.getProdutos());
                        this.atualizarTotal();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione na tabela um registro para excluir!", "Erro", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu uma falha:\n" + e.getMessage());
        }
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnFreteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFreteActionPerformed
        String valorFreteStr = JOptionPane.showInputDialog(null, "Informe o valor do frete:", "Frete", JOptionPane.QUESTION_MESSAGE);

        if (valorFreteStr != null && !valorFreteStr.isEmpty()) {
            String valorValidado = valorFreteStr.replaceAll(",", ".");
            try {
                double valorFrete = Double.parseDouble(valorValidado);
                vendaAtual.setFrete(valorFrete);
                lblFrete.setText("R$ " + valorFreteStr);

                atualizarTotal();

                JOptionPane.showMessageDialog(null, "Frete adicionado com sucesso!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Valor de frete inválido. Por favor, insira um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else if (valorFreteStr != null && valorFreteStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "O valor do frete não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnFreteActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (txtData.getText().isEmpty() || txtBuscaCliente.getText().isEmpty() || txtBuscaVendedor.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira todos os dados obrigatórios.", "Erro de validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (jcbFormaPagto.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Selecione a forma de pagamento.", "Erro de validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Venda venda;
        VendaDAO vendaDao = new VendaDAO();

        if (vendaAtual != null) {
            venda = vendaAtual;
        } else {
            venda = new Venda();
        }

        venda.setData(dataAtual);
        venda.setFormaPagto(String.valueOf(jcbFormaPagto.getSelectedItem()));

        venda.setSubTotal(venda.calculaTotalVenda(venda.getProdutos()));
        venda.setTotal(venda.calculaTotalVenda(venda.getProdutos()) + venda.getFrete());

        if (jcbStatus.getSelectedIndex() == 1) {
            venda.setConcluido(true);
        } else if (jcbStatus.getSelectedIndex() == 0) {
            venda.setConcluido(false);
        }

        if (!vendaAtual.getProdutos().isEmpty()) {

            try {
                if (venda.getId() == 0) {
                    vendaDao.salvar(venda);
                    JOptionPane.showMessageDialog(this, "Venda salva com sucesso!.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    vendaDao.atualizar(venda);
                    JOptionPane.showMessageDialog(this, "Venda atualizada com sucesso!.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
                vendaAtual = null;
                clienteVenda = null;
                vendedorVenda = null;
                TelaListaVendas tela = new TelaListaVendas();
                tela.setVisible(true);
                this.dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar a venda: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Insira pelo menos um produto para salvar a venda.", "Erro", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_btnSalvarActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaPedido.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPedido.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPedido.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPedido.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaPedido().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btgTipoCliente;
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnFrete;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionarCliente;
    private javax.swing.JButton btnSelecionarVendedor;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> jcbFiltroProduto;
    private javax.swing.JComboBox<String> jcbFormaPagto;
    private javax.swing.JComboBox<String> jcbStatus;
    private javax.swing.JLabel lblFrete;
    private javax.swing.JLabel lblSubTotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tblItensPedido;
    private javax.swing.JTable tblProdutos;
    public static javax.swing.JTextField txtBuscaCliente;
    private javax.swing.JTextField txtBuscaProduto;
    public static javax.swing.JTextField txtBuscaVendedor;
    private javax.swing.JFormattedTextField txtData;
    // End of variables declaration//GEN-END:variables

}
