import static spark.Spark.*;
import api.*;

public class App {
    public static void main(String[] args) {

        port(8080);

        // Carrega TODAS as APIs estáticas
        ApiCliente.Execute();
        ApiCarro.Execute();
        // ApiEmprestimo.Execute();
        // ApiSuspensao.Execute();

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   🚗 LOCADORA AUTOMOTIVA API 🚗       ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("🚀 Servidor iniciando na porta 8080...");

        System.out.println();
        System.out.println("🌐 Servidor rodando em: http://localhost:8080");
        System.out.println();
        System.out.println("📚 Endpoints disponíveis:");
        System.out.println("   → GET    /clientes");
        System.out.println("   → POST   /clientes");
        System.out.println("   → GET    /clientes/:id");
        System.out.println("   → PUT    /clientes/:id");
        System.out.println("   → DELETE /clientes/:id");
        System.out.println();
        System.out.println("   → GET    /carros");
        System.out.println("   → GET    /carro/:id");
        System.out.println();

    }
}
