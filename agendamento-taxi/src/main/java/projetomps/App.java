package projetomps;

import java.util.Scanner;

import lombok.extern.slf4j.Slf4j;
import projetomps.view.UserView;

@Slf4j
public class App 
{
    public static void main( String[] args )
    {
        UserView userView = new UserView();
        Scanner scanner = new Scanner(System.in);
        var response = 0;

        while (response != 5) {
            log.info("Menu:\n1: Criar usuario\n2: Remover usuario\n3: Ver usuario\n4: Atualizar usuario\n5: Cancelar");
            if (scanner.hasNextInt()) {
                response = scanner.nextInt();
                scanner.nextLine();
            } else {
                log.warn("Entrada invalida.");
                scanner.nextLine();
                continue;
            }
            switch (response) {
                case 1:
                    userView.createUser();
                    break;
                case 2:
                    userView.deleteUser();
                    break;
                case 3:
                    userView.getAllUsers();
                    break;
                case 4:
                    userView.updateUser();
                    break;
                default:
                    break;
            }
        }

        scanner.close();
    }
}
