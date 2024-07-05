
package br.com.senac.kjdistribuidorasoftware.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public class Compra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private Date data;
    
    private String fornecedor;
    
    @Column(name="forma_pagto")
    private String formaPagto;
    
    @Column(name="valor_total")
    private double valorTotal;
    
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<CompraProduto> produtos;

    public Compra() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getFormaPagto() {
        return formaPagto;
    }

    public void setFormaPagto(String formaPagto) {
        this.formaPagto = formaPagto;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    
    public List<CompraProduto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<CompraProduto> produtos) {
        this.produtos = produtos;
    }
}
