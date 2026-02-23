import service.OrderService;  // если OrderService в package service
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        OrderService service = new OrderService();

        while (true) {
            System.out.println("1. Создать заказ");
            System.out.println("2. Выход");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.print("ID запчасти: ");
                    int partId = scanner.nextInt();
                    System.out.print("Количество: ");
                    int quantity = scanner.nextInt();
                    service.createOrder(partId, quantity);
                }
                case 2 -> System.exit(0);
            }
        }
    }
}
