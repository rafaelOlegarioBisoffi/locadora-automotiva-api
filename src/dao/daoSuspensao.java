package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import entities.Suspensao;
import util.ConnectionFactory;
import util.GlobalBrDate;

public class daoSuspensao {

    // ------------------------------------
    // READ - Buscar todos
    // ------------------------------------
    public List<Suspensao> buscarTodos() {
        List<Suspensao> suspensoes = new ArrayList<>();
        String sql = "SELECT id, cliente_id, aluguel_id, dias_suspensao, data_inicio, " +
                     "data_fim, motivo, data_registro FROM suspensoes";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Suspensao suspensao = new Suspensao();
                    suspensao.setId(rs.getLong("id"));
                    suspensao.setClienteId(rs.getLong("cliente_id"));
                    suspensao.setAluguelId(rs.getLong("aluguel_id"));
                    suspensao.setDiasSuspensao(rs.getString("dias_suspensao"));
                    suspensao.setDataInicio(rs.getDate("data_inicio") != null ? rs.getDate("data_inicio").toString() : null);
                    suspensao.setDataFim(rs.getDate("data_fim") != null ? rs.getDate("data_fim").toString() : null);
                    suspensao.setMotivo(rs.getString("motivo"));
                    suspensao.setDataRegistro(rs.getTimestamp("data_registro") != null ? GlobalBrDate.formatTimestamp(rs.getTimestamp("data_registro")) : null);
                    suspensoes.add(suspensao);
                }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar suspensões: " + e.getMessage());
            e.printStackTrace();
        }
        return suspensoes;
    }

    // ------------------------------------
    // READ BY ID
    // ------------------------------------
    public Suspensao buscarPorId(Long id) {
        Suspensao suspensao = null;
        String sql = "SELECT id, cliente_id, aluguel_id, dias_suspensao, data_inicio, " +
                     "data_fim, motivo, data_registro FROM suspensoes WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    suspensao = new Suspensao();
                    suspensao.setId(rs.getLong("id"));
                    suspensao.setClienteId(rs.getLong("cliente_id"));
                    suspensao.setAluguelId(rs.getLong("aluguel_id"));
                    suspensao.setDiasSuspensao(rs.getString("dias_suspensao"));
                    suspensao.setDataInicio(rs.getDate("data_inicio") != null ? rs.getDate("data_inicio").toString() : null);
                    suspensao.setDataFim(rs.getDate("data_fim") != null ? rs.getDate("data_fim").toString() : null);
                    suspensao.setMotivo(rs.getString("motivo"));
                    suspensao.setDataRegistro(rs.getTimestamp("data_registro") != null ? GlobalBrDate.formatTimestamp(rs.getTimestamp("data_registro")) : null);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar suspensão por ID: " + id + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
        return suspensao;
    }

    // ------------------------------------
    // READ BY CLIENTE_ID
    // ------------------------------------
    public List<Suspensao> buscarPorClienteId(Long clienteId) {
        List<Suspensao> suspensoes = new ArrayList<>();
        String sql = "SELECT id, cliente_id, aluguel_id, dias_suspensao, data_inicio, " +
                     "data_fim, motivo, data_registro FROM suspensoes WHERE cliente_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, clienteId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Suspensao suspensao = new Suspensao();
                    suspensao.setId(rs.getLong("id"));
                    suspensao.setClienteId(rs.getLong("cliente_id"));
                    suspensao.setAluguelId(rs.getLong("aluguel_id"));
                    suspensao.setDiasSuspensao(rs.getString("dias_suspensao"));
                    suspensao.setDataInicio(rs.getDate("data_inicio") != null ? rs.getDate("data_inicio").toString() : null);
                    suspensao.setDataFim(rs.getDate("data_fim") != null ? rs.getDate("data_fim").toString() : null);
                    suspensao.setMotivo(rs.getString("motivo"));
                    suspensao.setDataRegistro(rs.getTimestamp("data_registro") != null ? GlobalBrDate.formatTimestamp(rs.getTimestamp("data_registro")) : null);
                    suspensoes.add(suspensao);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar suspensões por Cliente ID: " + clienteId + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
        return suspensoes;
    }

    // ------------------------------------
    // Verificar se cliente está suspenso
    // ------------------------------------
    public boolean clienteEstaSuspenso(Long clienteId) {
        String sql = "SELECT COUNT(*) FROM suspensoes WHERE cliente_id = ? AND data_fim >= ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, clienteId);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar suspensão do cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ------------------------------------
    // Buscar suspensões ativas de um cliente
    // ------------------------------------
    public List<Suspensao> buscarSuspensoesAtivas(Long clienteId) {
        List<Suspensao> suspensoes = new ArrayList<>();
        String sql = "SELECT id, cliente_id, aluguel_id, dias_suspensao, data_inicio, " +
                     "data_fim, motivo, data_registro FROM suspensoes WHERE cliente_id = ? AND data_fim >= ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, clienteId);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Suspensao suspensao = new Suspensao();
                    suspensao.setId(rs.getLong("id"));
                    suspensao.setClienteId(rs.getLong("cliente_id"));
                    suspensao.setAluguelId(rs.getLong("aluguel_id"));
                    suspensao.setDiasSuspensao(rs.getString("dias_suspensao"));
                    suspensao.setDataInicio(rs.getDate("data_inicio") != null ? rs.getDate("data_inicio").toString() : null);
                    suspensao.setDataFim(rs.getDate("data_fim") != null ? rs.getDate("data_fim").toString() : null);
                    suspensao.setMotivo(rs.getString("motivo"));
                    suspensao.setDataRegistro(rs.getTimestamp("data_registro") != null ? GlobalBrDate.formatTimestamp(rs.getTimestamp("data_registro")) : null);
                    suspensoes.add(suspensao);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar suspensões ativas. Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
        return suspensoes;
    }

    // ------------------------------------
    // CREATE
    // ------------------------------------
    public void inserir(Suspensao suspensao) {
        String sql = "INSERT INTO suspensoes (cliente_id, aluguel_id, dias_suspensao, data_inicio, " +
                     "data_fim, motivo, data_registro) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, suspensao.getClienteId());
            stmt.setLong(2, suspensao.getAluguelId());
            stmt.setString(3, suspensao.getDiasSuspensao());
            stmt.setDate(4, Date.valueOf(suspensao.getDataInicio()));
            stmt.setDate(5, Date.valueOf(suspensao.getDataFim()));
            stmt.setString(6, suspensao.getMotivo());
            stmt.setTimestamp(7, GlobalBrDate.now());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    suspensao.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir suspensão. Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------------------
    // UPDATE
    // ------------------------------------
    public void atualizar(Suspensao suspensao) {
        String sql = "UPDATE suspensoes SET dias_suspensao = ?, data_fim = ?, motivo = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, suspensao.getDiasSuspensao());
            stmt.setDate(2, Date.valueOf(suspensao.getDataFim()));
            stmt.setString(3, suspensao.getMotivo());
            stmt.setLong(4, suspensao.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar suspensão ID: " + suspensao.getId() + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------------------
    // DELETE
    // ------------------------------------
    public void deletar(Long id) {
        String sql = "DELETE FROM suspensoes WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar suspensão ID: " + id + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}