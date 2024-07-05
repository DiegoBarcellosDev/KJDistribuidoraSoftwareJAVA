package br.com.senac.kjdistribuidorasoftware.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;
    
    @Temporal(TemporalType.DATE)
    private Date data;
    
    @Column(name="forma_pagto")
    private String formaPagto;   

    private double frete = 0.00;
    
    private double subTotal = 0.00;

    private double total = 0.00;
    
    @Column(name="status")
    private boolean concluido;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<VendaProduto> produtos;

    public Venda() {
        this.produtos = new ArrayList<>();
        this.concluido = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getFormaPagto() {
        return formaPagto;
    }

    public void setFormaPagto(String formaPagto) {
        this.formaPagto = formaPagto;
    }

    public double getFrete() {
        return frete;
    }

    public void setFrete(double frete) {
        this.frete = frete;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
    

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public List<VendaProduto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<VendaProduto> produtos) {
        this.produtos = produtos;
    }

    public void addProduto(Produto produto, int quantidade) {

        for (VendaProduto vp : produtos) {
            if (vp.getProduto().equals(produto)) {
                vp.setQuantidade(vp.getQuantidade() + quantidade);
                return;
            }
        }

        VendaProduto vendaProduto = new VendaProduto();
        vendaProduto.setVenda(this);
        vendaProduto.setProduto(produto);
        vendaProduto.setQuantidade(quantidade);
        produtos.add(vendaProduto);
    }

    public void removeProduto(int produtoId) {
        produtos.removeIf(vp -> vp.getProduto().getId() == produtoId);
    }

    public double calculaTotalVenda(List<VendaProduto> produtos) {
        double valorTotal = 0.00;
        Cliente c = this.cliente;
        int i = 0;
        for (VendaProduto vp : produtos) {
            if (c.isIsBarbearia()) {
                valorTotal = valorTotal + vp.getProduto().getPrecoAtacado() * vp.getQuantidade();
            } else {
                valorTotal = valorTotal + vp.getProduto().getPrecoVarejo() * vp.getQuantidade();
            }
            i++;            
        }
        
        return valorTotal;
    }

}
