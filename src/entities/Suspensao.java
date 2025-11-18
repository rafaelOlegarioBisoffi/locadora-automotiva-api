package entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Suspensao {
    private int id;
    private int clienteId;
    private int aluguelId;
    private int diasSuspensao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String motivo;
    private LocalDateTime dataRegistro;
    
    // Construtores
    public Suspensao() {}
    
    public Suspensao(int clienteId, int aluguelId, int diasSuspensao, String motivo) {
        this.clienteId = clienteId;
        this.aluguelId = aluguelId;
        this.diasSuspensao = diasSuspensao;
        this.dataInicio = LocalDate.now();
        this.dataFim = this.dataInicio.plusDays(diasSuspensao);
        this.motivo = motivo;
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
    
    public int getAluguelId() {
        return aluguelId;
    }
    
    public void setAluguelId(int aluguelId) {
        this.aluguelId = aluguelId;
    }
    
    public int getDiasSuspensao() {
        return diasSuspensao;
    }
    
    public void setDiasSuspensao(int diasSuspensao) {
        this.diasSuspensao = diasSuspensao;
    }
    
    public LocalDate getDataInicio() {
        return dataInicio;
    }
    
    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }
    
    public LocalDate getDataFim() {
        return dataFim;
    }
    
    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }
    
    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
    
    // Método auxiliar
    public boolean estaAtiva() {
        return LocalDate.now().isBefore(dataFim) || LocalDate.now().isEqual(dataFim);
    }
    
    @Override
    public String toString() {
        return "Suspensao{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", aluguelId=" + aluguelId +
                ", diasSuspensao=" + diasSuspensao +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", motivo='" + motivo + '\'' +
                '}';
    }
}