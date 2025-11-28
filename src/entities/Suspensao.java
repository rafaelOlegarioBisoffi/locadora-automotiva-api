package entities;

import java.time.LocalDate;

public class Suspensao {
    private Long id;
    private Long clienteId;
    private Long aluguelId;
    private String diasSuspensao;
    private String dataInicio;
    private String dataFim;
    private String motivo;
    private String dataRegistro;
    
    // Construtores
    public Suspensao() {}
    
    public Suspensao(Long clienteId, Long aluguelId, int diasSuspensao, String motivo) {
        this.clienteId = clienteId;
        this.aluguelId = aluguelId;
        this.diasSuspensao = String.valueOf(diasSuspensao);
        LocalDate dataInicioLD = LocalDate.now();
        this.dataInicio = dataInicioLD.toString();
        this.dataFim = dataInicioLD.plusDays(diasSuspensao).toString();
        this.motivo = motivo;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public Long getAluguelId() {
        return aluguelId;
    }
    
    public void setAluguelId(Long aluguelId) {
        this.aluguelId = aluguelId;
    }
    
    public String getDiasSuspensao() {
        return diasSuspensao;
    }
    
    public void setDiasSuspensao(String diasSuspensao) {
        this.diasSuspensao = diasSuspensao;
    }
    
    public String getDataInicio() {
        return dataInicio;
    }
    
    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }
    
    public String getDataFim() {
        return dataFim;
    }
    
    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public String getDataRegistro() {
        return dataRegistro;
    }
    
    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
    
    // Método auxiliar
public boolean estaAtiva() {
    LocalDate hoje = LocalDate.now();
    LocalDate dataFimLD = LocalDate.parse(dataFim);
    return hoje.isBefore(dataFimLD) || hoje.isEqual(dataFimLD);
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