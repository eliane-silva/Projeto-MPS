package projetomps;

import projetomps.controller.UserController;
import projetomps.repository.MemoryRepository;
import projetomps.repository.UserRepository;
import projetomps.service.UserService;
import projetomps.view.UserView;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class Application {
    public static void main(String[] args) {
        // Injeção de dependências manual
        UserRepository userRepository = new MemoryRepository();
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);
        Scanner scanner = new Scanner(System.in);
        UserView userView = new UserView(userController, scanner);
        
        log.info("Iniciando Sistema de Gerenciamento de Usuários...");
        
        boolean continuar = true;
        while (continuar) {
            try {
                userView.exibirMenuPrincipal();
                int opcao = Integer.parseInt(scanner.nextLine());
                
                switch (opcao) {
                    case 1:
                        userView.exibirFormularioLogin();
                        break;
                    case 2:
                        userView.lerDadosUsuario();
                        break;
                    case 3:
                        userView.buscarUsuarioPorLogin();
                        break;
                    case 4:
                        userView.atualizarUsuario();
                        break;
                    case 5:
                        userView.deletarUsuario();
                        break;
                    case 0:
                        continuar = false;
                        log.info("Sistema encerrado.");
                        break;
                    default:
                        userView.exibirErro("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                userView.exibirErro("Por favor, digite um número válido.");
            } catch (Exception e) {
                log.error("Erro inesperado: ", e);
                userView.exibirErro("Erro inesperado no sistema.");
            }
        }
        
        scanner.close();
    }
}