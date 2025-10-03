package projetomps;

import projetomps.app_logic.dao.DAOFactory;
import projetomps.app_logic.dao.RotationDAO;
import projetomps.app_logic.dao.UserDAO;
import projetomps.business_logic.command.CommandInvoker;
import projetomps.business_logic.controller.FacadeSingletonController;
import projetomps.business_logic.controller.RelatorioController;
import projetomps.business_logic.controller.RotationController;
import projetomps.business_logic.controller.UserController;
import projetomps.business_logic.service.AuthenticationService;
import projetomps.business_logic.service.RelatorioService;
import projetomps.business_logic.service.RotationService;
import projetomps.business_logic.service.UserService;
import projetomps.view.MenuPrincipalView;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");

        try {
            // Injeção de dependências manual
            DAOFactory memoryFactory = DAOFactory.getDAOFactory(DAOFactory.MEMORY);

            UserDAO userDAO = memoryFactory.getUserDAO();
            RotationDAO rotationDAO = memoryFactory.getRotationDAO();

            // Services
            UserService userService = new UserService(userDAO);
            RotationService rotationService = new RotationService(rotationDAO);
            AuthenticationService authenticationService = new AuthenticationService(userDAO);
            RelatorioService relatorioService = new RelatorioService(rotationDAO);

            // Command Invoker (gerencia Command Pattern)
            CommandInvoker commandInvoker = new CommandInvoker();

            // Controllers com UserService injetado para Memento Pattern
            UserController userController = new UserController(userService, commandInvoker);
            RotationController rotationController = new RotationController(
                    rotationService,
                    userService,  // Necessário para restaurar Taxist no Memento
                    commandInvoker
            );
            RelatorioController relatorioController = new RelatorioController(relatorioService);

            // Facade Singleton
            FacadeSingletonController facadeController = FacadeSingletonController.getInstance(
                    userController,
                    rotationController,
                    authenticationService,
                    relatorioController,
                    commandInvoker
            );

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
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║          Inicializando dados do sistema...                   ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();

            // Admin padrão
            userService.criarAdmin("admin", "admin123");
            System.out.println("✓ Admin padrão criado (login: admin, senha: admin123)");

            // Taxistas de exemplo
            userService.criarTaxista("joao", "senha123", "João Silva", "joao@email.com");
            System.out.println("✓ Taxista 'João Silva' criado");

            userService.criarTaxista("maria", "senha456", "Maria Santos", "maria@email.com");
            System.out.println("✓ Taxista 'Maria Santos' criado");

            System.out.println();
            System.out.println("Dados iniciais criados com sucesso!");
            System.out.println("Padrões implementados: Command + Memento");
            System.out.println();

        } catch (Exception e) {
            System.err.println("Erro ao inicializar dados: " + e.getMessage());
            throw new RuntimeException("Falha na inicialização", e);
        }
    }
}