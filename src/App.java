import static spark.Spark.*;
import api.*;

public class App {
    public static void main(String[] args) {

        port(8080);

        // Carrega TODAS as APIs estáticas
        apiCliente.Execute();
        apiCarro.Execute();
        apiAluguel.Execute();
        apiSuspensao.Execute();

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
        System.out.println("📋 ALUGUÉIS:");
        System.out.println("   → GET    /alugueis");
        System.out.println("   → GET    /aluguel/:id");
        System.out.println("   → GET    /alugueis/cliente/:clienteId");
        System.out.println("   → GET    /alugueis/cliente/:clienteId?status=APROVADO");
        System.out.println("   → POST   /aluguel");
        System.out.println("   → PUT    /aluguel/:id/aprovar");
        System.out.println("   → PUT    /aluguel/:id/rejeitar");
        System.out.println("   → PUT    /aluguel/:id/devolver");
        System.out.println("   → DELETE /aluguel/:id");
        System.out.println();
        System.out.println("🚫 SUSPENSÕES:");
        System.out.println("   → GET    /suspensoes");
        System.out.println("   → GET    /suspensao/:id");
        System.out.println("   → GET    /suspensoes/cliente/:clienteId");
        System.out.println("   → GET    /suspensoes/cliente/:clienteId?ativas=true");
        System.out.println("   → GET    /suspensoes/cliente/:clienteId/status");
        System.out.println("   → POST   /suspensao");
        System.out.println("   → PUT    /suspensao/:id");
        System.out.println("   → DELETE /suspensao/:id");
        System.out.println();
        System.out.println("✅ Servidor pronto para receber requisições!");

    }
}
