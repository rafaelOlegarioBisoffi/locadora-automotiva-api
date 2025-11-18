package entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Aluguel {
    private int id;
    private int clienteId;
    private int carroId;
    private LocalDateTime dataSolicitacao;
    private LocalDateTime dataAprovacao;
    private LocalDate dataInicio;
    private LocalDate dataFimPrevista;
    private LocalDateTime dataDevolucaoReal;
    private StatusAluguel status;
    private int diasAtraso;
    private BigDecimal valorTotal;
    private String motivoRejeicao;
    
    // Enum para o status do aluguel
    public enum StatusAluguel {
        PENDENTE,
        APROVADO,
        REJEITADO
    }
    
    // Construtores
    public Aluguel() {
        this.status = StatusAluguel.PENDENTE;
        this.diasAtraso = 0;
    }
    
    public Aluguel(int clienteId, int carroId, LocalDate dataInicio) {
        this.clienteId = clienteId;
        this.carroId = carroId;
        this.dataInicio = dataInicio;
        this.dataFimPrevista = dataInicio.plusDays(5); // Limite de 5 dias
        this.status = StatusAluguel.PENDENTE;
        this.diasAtraso = 0;
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }
    
    public int getCarroId() {
        return carroId;
    }
    
    public void setCarroId(int carroId) {
        this.carroId = carroId;
    }
    
    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }
    
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }
    
    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }
    
    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }
    
    public LocalDate getDataInicio() {
        return dataInicio;
    }
    
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
        this.dataFimPrevista = dataInicio.plusDays(5);
    }
    
    public LocalDate getDataFimPrevista() {
        return dataFimPrevista;
    }
    
    public void setDataFimPrevista(LocalDate dataFimPrevista) {
        this.dataFimPrevista = dataFimPrevista;
    }
    
    public LocalDateTime getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }
    
    public void setDataDevolucaoReal(LocalDateTime dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }
    
    public StatusAluguel getStatus() {
        return status;
    }
    
    public void setStatus(StatusAluguel status) {
        this.status = status;
    }
    
    public int getDiasAtraso() {
        return diasAtraso;
    }
    
    public void setDiasAtraso(int diasAtraso) {
        this.diasAtraso = diasAtraso;
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public String getMotivoRejeicao() {
        return motivoRejeicao;
    }
    
    public void setMotivoRejeicao(String motivoRejeicao) {
        this.motivoRejeicao = motivoRejeicao;
    }
    
    // Métodos auxiliares
    public boolean estaAtrasado() {
        if (dataDevolucaoReal == null && LocalDate.now().isAfter(dataFimPrevista)) {
            return true;
        }
        if (dataDevolucaoReal != null) {
            return dataDevolucaoReal.toLocalDate().isAfter(dataFimPrevista);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "Aluguel{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", carroId=" + carroId +
                ", dataInicio=" + dataInicio +
                ", dataFimPrevista=" + dataFimPrevista +
                ", status=" + status +
                ", diasAtraso=" + diasAtraso +
                '}';
    }
}