package entities;

import java.math.BigDecimal;

public class Carro {
    private Long id;
    private String placa;
    private String modelo;
    private String marca;
    private int ano;
    private String cor;
    private BigDecimal valorDiaria;
    private StatusCarro status;
    private String dataCadastro;

    // Enum para o status do carro
    public enum StatusCarro {
        DISPONIVEL,
        ALUGADO,
        MANUTENCAO
    }

    // Construtores
    public Carro() {
        this.status = StatusCarro.DISPONIVEL;
    }

    public Carro(
            Long id,
            String placa,
            String modelo,
            String marca,
            int ano,
            String cor,
            BigDecimal valorDiaria,
            StatusCarro status,
            String dataCadastro) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.cor = cor;
        this.valorDiaria = valorDiaria;
        this.status = status;
        this.dataCadastro = dataCadastro;
    }

    public Carro(String placa, String modelo, String marca, int ano, String cor, BigDecimal valorDiaria) {
        this.placa = placa;
        this.modelo = modelo;
        this.marca = marca;
        this.ano = ano;
        this.cor = cor;
        this.valorDiaria = valorDiaria;
        this.status = StatusCarro.DISPONIVEL;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public BigDecimal getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(BigDecimal valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    public StatusCarro getStatus() {
        return status;
    }

    public void setStatus(StatusCarro status) {
        this.status = status;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    // Métodos auxiliares
    public boolean estaDisponivel() {
        return this.status == StatusCarro.DISPONIVEL;
    }

    @Override
    public String toString() {
        return "Carro{" +
                "id=" + id +
                ", placa='" + placa + '\'' +
                ", modelo='" + modelo + '\'' +
                ", marca='" + marca + '\'' +
                ", ano=" + ano +
                ", valorDiaria=" + valorDiaria +
                ", status=" + status +
                '}';
    }
}