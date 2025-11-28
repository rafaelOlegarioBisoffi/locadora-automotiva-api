package api;

import static spark.Spark.*;
import com.google.gson.Gson;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import entities.Aluguel;
import entities.Cliente;
import entities.Carro;
import entities.Suspensao;
import dao.daoAluguel;
import dao.daoCliente;
import dao.daoCarro;
import dao.daoSuspensao;
import entities.Aluguel.StatusAluguel;
import validation.Rod;

public class apiAluguel {

    private static final daoAluguel dao = new daoAluguel();
    private static final daoCliente daoCliente = new daoCliente();
    private static final daoCarro daoCarro = new daoCarro();
    private static final daoSuspensao daoSuspensao = new daoSuspensao();
    private static final Gson gson = new Gson();

    private static final String APPLICATION_JSON = "application/json";

    public static void Execute() {

        after(new Filter() {
            @Override
            public void handle(Request request, Response response) {
                response.type(APPLICATION_JSON);
            }
        });

        // ------------------------------------
        // GET /alugueis - Buscar todos
        // ------------------------------------
        get("/alugueis", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return gson.toJson(dao.buscarTodos());
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao buscar alugueis\"}";
                }
            }
        });

        // ------------------------------------
        // GET /aluguel/:id - Buscar por ID
        // ------------------------------------
        get("/aluguel/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id").replaceAll("\\D", ""));
                    Aluguel aluguel = dao.buscarPorId(id);

                    if (aluguel == null) {
                        response.status(404);
                        return "{\"mensagem\":\"Aluguel não encontrado\"}";
                    }

                    return gson.toJson(aluguel);
                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\":\"ID inválido\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao buscar aluguel\"}";
                }
            }
        });

        // ------------------------------------
        // GET /alugueis/cliente/:clienteId - Buscar por cliente
        // ------------------------------------
        get("/alugueis/cliente/:clienteId", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long clienteId = Long.parseLong(request.params(":clienteId").replaceAll("\\D", ""));
                    String status = request.queryParams("status");

                    if (status != null && !status.isEmpty()) {
                        try {
                            StatusAluguel statusEnum = StatusAluguel.valueOf(status.toUpperCase());
                            return gson.toJson(dao.buscarPorClienteIdEStatus(clienteId, statusEnum));
                        } catch (IllegalArgumentException e) {
                            response.status(400);
                            return "{\"mensagem\":\"Status inválido. Use: AGUARDANDO_APROVACAO, APROVADO, REJEITADO, DEVOLVIDO\"}";
                        }
                    }

                    return gson.toJson(dao.buscarPorClienteId(clienteId));
                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\":\"ID do cliente inválido\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao buscar alugueis do cliente\"}";
                }
            }
        });

        // ------------------------------------
        // POST /aluguel - Solicitar aluguel
        // ------------------------------------
        post("/aluguel", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Aluguel aluguel = gson.fromJson(request.body(), Aluguel.class);

                    // Validações
                    if (aluguel.getClienteId() == null) {
                        response.status(400);
                        return "{\"mensagem\":\"Cliente ID é obrigatório\"}";
                    }

                    if (aluguel.getCarroId() == null) {
                        response.status(400);
                        return "{\"mensagem\":\"Carro ID é obrigatório\"}";
                    }

                    // Verificar se cliente existe
                    Cliente cliente = daoCliente.buscarPorId(aluguel.getClienteId());
                    if (cliente == null) {
                        response.status(404);
                        return "{\"mensagem\":\"Cliente não encontrado\"}";
                    }

                    // Verificar se carro existe
                    Carro carro = daoCarro.buscarPorId(aluguel.getCarroId());
                    if (carro == null) {
                        response.status(404);
                        return "{\"mensagem\":\"Carro não encontrado\"}";
                    }

                    // Verificar se cliente tem suspensão ativa
                    if (daoSuspensao.clienteEstaSuspenso(aluguel.getClienteId())) {
                        response.status(403);
                        return "{\"mensagem\":\"Cliente está suspenso e não pode alugar carros\"}";
                    }

                    // Verificar se cliente já tem aluguel ativo
                    if (dao.clienteTemAluguelAtivo(aluguel.getClienteId())) {
                        response.status(409);
                        return "{\"mensagem\":\"Cliente já possui um aluguel ativo\"}";
                    }

                    // Verificar se carro já está alugado
                    if (dao.carroEstaAlugado(aluguel.getCarroId())) {
                        response.status(409);
                        return "{\"mensagem\":\"Carro já está alugado\"}";
                    }

                    // Inserir aluguel
                    dao.inserir(aluguel);

                    response.status(201);
                    return gson.toJson(aluguel);

                } catch (IllegalArgumentException e) {
                    response.status(400);
                    return "{\"mensagem\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao solicitar aluguel\"}";
                }
            }
        });

        // ------------------------------------
        // PUT /aluguel/:id/aprovar - Aprovar aluguel
        // ------------------------------------
        put("/aluguel/:id/aprovar", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id").replaceAll("\\D", ""));
                    Aluguel aluguel = dao.buscarPorId(id);

                    if (aluguel == null) {
                        response.status(404);
                        return "{\"mensagem\":\"Aluguel não encontrado\"}";
                    }

                    if (aluguel.getStatus() != StatusAluguel.PENDENTE) {
                        response.status(400);
                        return "{\"mensagem\":\"Apenas alugueis pendentes podem ser aprovados\"}";
                    }

                    // Validar se a aprovação está dentro de 24h da criação
                    LocalDate dataSolicitacao = LocalDate.parse(aluguel.getDataSolicitacao());
                    LocalDate hoje = LocalDate.now();
                    long horasDecorridas = ChronoUnit.HOURS.between(
                        dataSolicitacao.atStartOfDay(),
                        hoje.atStartOfDay()
                    );

                    if (horasDecorridas > 24) {
                        response.status(400);
                        return "{\"mensagem\":\"A aprovação deve ser feita em até 24 horas após a criação do aluguel. Prazo expirado.\"}";
                    }

                    // Obter data de início do corpo da requisição
                    String dataInicio = null;
                    try {
                        java.util.Map<String, Object> body = gson.fromJson(request.body(), java.util.Map.class);
                        if (body != null && body.get("dataInicio") != null) {
                            dataInicio = body.get("dataInicio").toString();
                        }
                    } catch (Exception e) {
                        // Se não conseguir fazer parse, usar data de hoje
                    }
                    
                    if (dataInicio == null || dataInicio.isEmpty()) {
                        dataInicio = LocalDate.now().toString();
                    }

                    dao.aprovar(id, dataInicio);

                    response.status(200);
                    return "{\"mensagem\":\"Aluguel aprovado com sucesso\"}";

                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\":\"ID inválido\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao aprovar aluguel\"}";
                }
            }
        });

        // ------------------------------------
        // PUT /aluguel/:id/rejeitar - Rejeitar aluguel
        // ------------------------------------
        put("/aluguel/:id/rejeitar", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id").replaceAll("\\D", ""));
                    Aluguel aluguel = dao.buscarPorId(id);

                    if (aluguel == null) {
                        response.status(404);
                        return "{\"mensagem\":\"Aluguel não encontrado\"}";
                    }

                    if (aluguel.getStatus() != StatusAluguel.PENDENTE) {
                        response.status(400);
                        return "{\"mensagem\":\"Apenas alugueis aguardando aprovação podem ser rejeitados\"}";
                    }

                    String motivoRejeicao = gson.fromJson(request.body(), java.util.Map.class).get("motivoRejeicao").toString();
                    
                    if (motivoRejeicao == null || motivoRejeicao.trim().isEmpty()) {
                        response.status(400);
                        return "{\"mensagem\":\"Motivo da rejeição é obrigatório\"}";
                    }

                    Rod.string(motivoRejeicao, "Motivo da rejeição", false, 5, 500);

                    dao.rejeitar(id, motivoRejeicao);

                    response.status(200);
                    return "{\"mensagem\":\"Aluguel rejeitado com sucesso\"}";

                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\":\"ID inválido\"}";
                } catch (IllegalArgumentException e) {
                    response.status(400);
                    return "{\"mensagem\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao rejeitar aluguel\"}";
                }
            }
        });

        // ------------------------------------
        // PUT /aluguel/:id/devolver - Devolver carro
        // ------------------------------------
        put("/aluguel/:id/devolver", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id").replaceAll("\\D", ""));
                    Aluguel aluguel = dao.buscarPorId(id);

                    if (aluguel == null) {
                        response.status(404);
                        return "{\"mensagem\":\"Aluguel não encontrado\"}";
                    }

                    if (aluguel.getStatus() != StatusAluguel.APROVADO) {
                        response.status(400);
                        return "{\"mensagem\":\"Apenas alugueis aprovados podem ser devolvidos\"}";
                    }

                    // Calcular dias de atraso
                    LocalDate hoje = LocalDate.now();
                    LocalDate dataFim = LocalDate.parse(aluguel.getDataFimPrevista());
                    
                    int diasAtraso = 0;
                    if (hoje.isAfter(dataFim)) {
                        diasAtraso = (int) ChronoUnit.DAYS.between(dataFim, hoje);
                    }

                    // Devolver o carro
                    dao.devolver(id);

                    // Se houver atraso, criar suspensão
                    if (diasAtraso > 0) {
                        Suspensao suspensao = new Suspensao();
                        suspensao.setClienteId(aluguel.getClienteId());
                        suspensao.setAluguelId(id);
                        suspensao.setDiasSuspensao(String.valueOf(diasAtraso));
                        suspensao.setDataInicio(hoje.toString());
                        suspensao.setDataFim(hoje.plusDays(diasAtraso).toString());
                        suspensao.setMotivo("Atraso de " + diasAtraso + " dia(s) na devolução do veículo");
                        
                        daoSuspensao.inserir(suspensao);
                        
                        response.status(200);
                        return "{\"mensagem\":\"Carro devolvido. Cliente suspenso por " + diasAtraso + " dia(s) devido ao atraso\"}";
                    }

                    response.status(200);
                    return "{\"mensagem\":\"Carro devolvido com sucesso\"}";

                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\":\"ID inválido\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao devolver carro\"}";
                }
            }
        });

        // ------------------------------------
        // DELETE /aluguel/:id - Deletar aluguel
        // ------------------------------------
        delete("/aluguel/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id").replaceAll("\\D", ""));
                    Aluguel aluguel = dao.buscarPorId(id);

                    if (aluguel == null) {
                        response.status(404);
                        return "{\"mensagem\":\"Aluguel não encontrado\"}";
                    }

                    dao.deletar(id);

                    response.status(200);
                    return "{\"mensagem\":\"Aluguel deletado com sucesso\"}";

                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\":\"ID inválido\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao deletar aluguel\"}";
                }
            }
        });
    }
}