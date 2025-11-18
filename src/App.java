import static spark.Spark.*;
import api.*;

public class App {
    public static void main(String[] args) {
        // Configura a porta
        port(8080);
        
        // Habilita CORS
        enableCORS();
        
        // Banner de inicialização
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   🚗 LOCADORA AUTOMOTIVA API 🚗       ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("🚀 Servidor iniciando na porta 8080...");
        System.out.println();
        
        // Inicializa todas as APIs
        try {
            new apiCliente();
            System.out.println("✅ API Clientes carregada");
            
            new apiCarro();
            System.out.println("✅ API Carros carregada");
            
            new apiEmprestimo();
            System.out.println("✅ API Empréstimos carregada");
            
            new apiSuspensao();
            System.out.println("✅ API Suspensões carregada");
            
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
            System.out.println("   → POST   /carros");
            System.out.println("   → GET    /carros/:id");
            System.out.println("   → PUT    /carros/:id");
            System.out.println("   → DELETE /carros/:id");
            System.out.println();
            System.out.println("   → GET    /emprestimos");
            System.out.println("   → POST   /emprestimos");
            System.out.println("   → GET    /emprestimos/:id");
            System.out.println("   → PATCH  /emprestimos/:id/aprovar");
            System.out.println("   → PATCH  /emprestimos/:id/rejeitar");
            System.out.println();
            System.out.println("   → GET    /suspensoes");
            System.out.println("   → GET    /suspensoes/cliente/:clienteId");
            System.out.println();
            System.out.println("⚡ Pressione Ctrl+C para parar o servidor");
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar as APIs:");
            e.printStackTrace();
        }
    }
    
    private static void enableCORS() {
        options("/*", (req, res) -> {
            String accessControlRequestHeaders = req.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                res.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            
            String accessControlRequestMethod = req.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                res.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });
        
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            res.type("application/json");
        });
    }
}