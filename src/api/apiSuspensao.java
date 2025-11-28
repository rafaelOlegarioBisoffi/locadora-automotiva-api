package api;

import static spark.Spark.*;
import com.google.gson.Gson;
import validation.Rod;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Route;
import java.time.LocalDate;
import entities.Suspensao;
import dao.daoSuspensao;
import dao.daoCliente;
import entities.Cliente;

public class apiSuspensao {

    private static final daoSuspensao dao = new daoSuspensao();
    
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
        // GET /suspensoes - Buscar todos
        // ------------------------------------
        get("/suspensoes", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return gson.toJson(dao.buscarTodos());
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao buscar suspensões\"}";
                }
            }
        });

        // ------------------------------------
        // GET /suspensao/:id - Buscar por ID
        // ------------------------------------
        get("/suspensao/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id").replaceAll("\\D", ""));
                    Suspensao suspensao = dao.buscarPorId(id);

                    if (suspensao == null) {
                        response.status(404);
                        return "{\"mensagem\":\"Suspensão não encontrada\"}";
                    }

                    return gson.toJson(suspensao);
                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\":\"ID inválido\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao buscar suspensão\"}";
                }
            }
        });

        // ------------------------------------
        // GET /suspensoes/cliente/:clienteId - Buscar por cliente
        // ------------------------------------
        get("/suspensoes/cliente/:clienteId", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long clienteId = Long.parseLong(request.params(":clienteId").replaceAll("\\D", ""));
                    String ativas = request.queryParams("ativas");

                    if (ativas != null && ativas.equalsIgnoreCase("true")) {
                        return gson.toJson(dao.buscarSuspensoesAtivas(clienteId));
                    }

                    return gson.toJson(dao.buscarPorClienteId(clienteId));
                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\":\"ID do cliente inválido\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao buscar suspensões do cliente\"}";
                }
            }
        });

        // ------------------------------------
        // GET /suspensoes/cliente/:clienteId/status - Verificar se está suspenso
        // ------------------------------------
        get("/suspensoes/cliente/:clienteId/status", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long clienteId = Long.parseLong(request.params(":clienteId").replaceAll("\\D", ""));
                    
                    boolean estaSuspenso = dao.clienteEstaSuspenso(clienteId);
                    
                    if (estaSuspenso) {
                        java.util.List<Suspensao> suspensoesAtivas = dao.buscarSuspensoesAtivas(clienteId);
                        return "{\"suspenso\":true, \"suspensoes\":" + gson.toJson(suspensoesAtivas) + "}";
                    } else {
                        return "{\"suspenso\":false}";
                    }

                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\":\"ID do cliente inválido\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao verificar status de suspensão\"}";
                }
            }
        });

        // ------------------------------------
        // POST /suspensao - Criar suspensão
        // ------------------------------------
        post("/suspensao", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Suspensao suspensao = gson.fromJson(request.body(), Suspensao.class);

                    // Validações
                    if (suspensao.getClienteId() == null) {
                        response.status(400);
                        return "{\"mensagem\":\"Cliente ID é obrigatório\"}";
                    }

                    if (suspensao.getDiasSuspensao() == null || suspensao.getDiasSuspensao().trim().isEmpty()) {
                        response.status(400);
                        return "{\"mensagem\":\"Dias de suspensão é obrigatório\"}";
                    }

                    Rod.string(suspensao.getMotivo(), "Motivo", false, 5, 500);

                    // Verificar se cliente existe
                    Cliente cliente = new daoCliente().buscarPorId(suspensao.getClienteId());
                    if (cliente == null) {
                        response.status(404);
                        return "{\"mensagem\":\"Cliente não encontrado\"}";
                    }

                    // Se não foi informada data de início, usar hoje
                    if (suspensao.getDataInicio() == null || suspensao.getDataInicio().isEmpty()) {
                        suspensao.setDataInicio(LocalDate.now().toString());
                    }

                    // Calcular data fim
                    LocalDate dataInicio = LocalDate.parse(suspensao.getDataInicio());
                    int dias = Integer.parseInt(suspensao.getDiasSuspensao());
                    LocalDate dataFim = dataInicio.plusDays(dias);
                    suspensao.setDataFim(dataFim.toString());

                    dao.inserir(suspensao);

                    response.status(201);
                    return gson.toJson(suspensao);

                } catch (IllegalArgumentException e) {
                    response.status(400);
                    return "{\"mensagem\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao criar suspensão\"}";
                }
            }
        });

        // ------------------------------------
        // PUT /suspensao/:id - Atualizar suspensão
        // ------------------------------------
        put("/suspensao/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id").replaceAll("\\D", ""));
                    Suspensao suspensaoExistente = dao.buscarPorId(id);

                    if (suspensaoExistente == null) {
                        response.status(404);
                        return "{\"mensagem\":\"Suspensão não encontrada\"}";
                    }

                    Suspensao suspensao = gson.fromJson(request.body(), Suspensao.class);
                    suspensao.setId(id);

                    // Validações
                    if (suspensao.getDiasSuspensao() != null && !suspensao.getDiasSuspensao().trim().isEmpty()) {
                        suspensaoExistente.setDiasSuspensao(suspensao.getDiasSuspensao());
                        
                        // Recalcular data fim
                        LocalDate dataInicio = LocalDate.parse(suspensaoExistente.getDataInicio());
                        int dias = Integer.parseInt(suspensao.getDiasSuspensao());
                        LocalDate dataFim = dataInicio.plusDays(dias);
                        suspensaoExistente.setDataFim(dataFim.toString());
                    }

                    if (suspensao.getMotivo() != null && !suspensao.getMotivo().trim().isEmpty()) {
                        Rod.string(suspensao.getMotivo(), "Motivo", false, 5, 500);
                        suspensaoExistente.setMotivo(suspensao.getMotivo());
                    }

                    dao.atualizar(suspensaoExistente);

                    response.status(200);
                    return gson.toJson(suspensaoExistente);

                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\":\"ID inválido\"}";
                } catch (IllegalArgumentException e) {
                    response.status(400);
                    return "{\"mensagem\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao atualizar suspensão\"}";
                }
            }
        });

        // ------------------------------------
        // DELETE /suspensao/:id - Deletar suspensão
        // ------------------------------------
        delete("/suspensao/:id", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    Long id = Long.parseLong(request.params(":id").replaceAll("\\D", ""));
                    Suspensao suspensao = dao.buscarPorId(id);

                    if (suspensao == null) {
                        response.status(404);
                        return "{\"mensagem\":\"Suspensão não encontrada\"}";
                    }

                    dao.deletar(id);

                    response.status(200);
                    return "{\"mensagem\":\"Suspensão deletada com sucesso\"}";

                } catch (NumberFormatException e) {
                    response.status(400);
                    return "{\"mensagem\":\"ID inválido\"}";
                } catch (Exception e) {
                    response.status(500);
                    return "{\"mensagem\":\"Erro ao deletar suspensão\"}";
                }
            }
        });
    }
}