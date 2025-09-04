package projetomps;

import projetomps.controller.FacadeSingletonController;
import projetomps.controller.RotationController;
import projetomps.controller.UserController;
import projetomps.model.Admin;
import projetomps.model.Taxist;
import projetomps.repository.MemoryUserRepository;
import projetomps.repository.MemoryRotationRepository;
import projetomps.repository.RotationRepository;
import projetomps.repository.UserRepository;
import projetomps.service.AuthenticationService;
import projetomps.service.RotationService;
import projetomps.service.UserService;
import projetomps.view.MenuPrincipalView;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");

        try {
            // Injeção de dependências manual
            UserRepository userRepository = new MemoryUserRepository();
            RotationRepository rotationRepository = new MemoryRotationRepository();

            UserService userService = new UserService(userRepository);
            RotationService rotationService = new RotationService(rotationRepository);
            AuthenticationService authenticationService = new AuthenticationService(userRepository);

            UserController userController = new UserController(userService);
            RotationController rotationController = new RotationController(rotationService);

            FacadeSingletonController facadeController = FacadeSingletonController.getInstance(
                    userController, rotationController, authenticationService);

            Scanner scanner = new Scanner(System.in);

            // Inicializar dados de teste
            inicializarDadosIniciais(userService);

            // Iniciar o menu principal
            MenuPrincipalView menuPrincipal = new MenuPrincipalView(facadeController, scanner);
            menuPrincipal.exibirMenuInicial();

            scanner.close();

            // Mensagem final do sistema
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║                                                              ║");
            System.out.println("║               Sistema AgendaTáxi Encerrado                   ║");
            System.out.println("║                                                              ║");
            System.out.println("║                    Até a próxima!                            ║");
            System.out.println("║                                                              ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("╔══════════════════════════════════════════════════════════════╗");
            System.err.println("║                    ERRO CRÍTICO                             ║");
            System.err.println("╚══════════════════════════════════════════════════════════════╝");
            System.err.println();
            System.err.println("Erro fatal no sistema: " + e.getMessage());
            System.err.println();
            System.err.println("Por favor, reinicie a aplicação.");
            e.printStackTrace();
        }
    }

    private static void inicializarDadosIniciais(UserService userService) {
        try {

            // Admin padrão
            Admin admin = new Admin("admin", "admin123");
            userService.salvarAdmin(admin);

            // Taxistas de exemplo
            Taxist taxist1 = new Taxist("joao", "senha123");
            taxist1.setName("João Silva");
            taxist1.setEmail("joao@email.com");
            userService.salvarTaxist(taxist1);

            Taxist taxist2 = new Taxist("maria", "senha456");
            taxist2.setName("Maria Santos");
            taxist2.setEmail("maria@email.com");
            userService.salvarTaxist(taxist2);

            System.out.println("Dados iniciais criados com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro ao inicializar dados: " + e.getMessage());
            throw new RuntimeException("Falha na inicialização", e);
        }
    }
}