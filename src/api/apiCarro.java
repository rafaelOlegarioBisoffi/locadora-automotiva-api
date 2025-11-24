package api;

import static spark.Spark.*;

import com.google.gson.Gson;

import dao.daoCarro;
import entities.Carro;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Filter;

public class ApiCarro {

  private static final daoCarro dao = new daoCarro();
  private static final Gson gson = new Gson();

  private static final String APPLICATION_JSON = "application/json";

  public static void Execute() {
    // Filtro para garantir retorno JSON
    after(new Filter() {
      @Override
      public void handle(Request request, Response response) {
        response.type(APPLICATION_JSON);
      }
    });

    // ------------------------------------
    // GET /carros - Buscar todos
    // ------------------------------------
    get("/carros", new Route() {
      @Override
      public Object handle(Request request, Response response) {
        try {
          return gson.toJson(dao.buscarTodos());
        } catch (Exception e) {
          response.status(500);
          return "{\"mensagem\":\"Erro ao buscar carros\"}";
        }
      }
    });

    // ------------------------------------
    // GET /carro/:id - Buscar por ID
    // ------------------------------------
    get("/carro/:id", new Route() {
      @Override
      public Object handle(Request request, Response response) {
        try {
          Long id = Long.parseLong(request.params(":id").replaceAll("\\D", ""));

          Carro carro = dao.buscarPorId(id);

          if (carro != null) {
            return gson.toJson(carro);
          } else {
            response.status(404);
            return "{\"mensagem\":\"Carro não encontrado\"}";
          }

        } catch (NumberFormatException e) {
          response.status(400);
          return "{\"mensagem\":\"ID inválido\"}";
        } catch (Exception e) {
          response.status(500);
          return "{\"mensagem\":\"Erro ao buscar carro\"}";
        }
      }
    });
  }
}
