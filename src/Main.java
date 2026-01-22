import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {
            Dotenv dotenv = Dotenv.configure().load();
            String url = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");
            Connection conn = DriverManager.getConnection(url, user, password);

            Statement stmt = conn.createStatement();

            if(createTable(stmt)){
                System.out.println("Таблица cars создана успешно!");
            }else {
                System.err.println("Не удалось создать таблицу cars");
            }

            run(conn);


            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static Boolean createTable(Statement stmt) {
        try {
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS cars " +
                        "(id INT PRIMARY KEY AUTO_INCREMENT," +
                        "manufacturer VARCHAR(50) NOT NULL," +
                        "model VARCHAR(50) NOT NULL," +
                        "engine_volume DECIMAL(3,1)," +
                        "year INT," +
                        "color VARCHAR(30)," +
                        "car_type VARCHAR(20))"
        );
        return true;
        } catch (SQLException e) {
           e.printStackTrace();
           return false;
        }
    }

    public static void run(Connection connection){
        while (true) {
            System.out.println();
            System.out.println("Каталог автомобилей");
            Scanner scanner = new Scanner(System.in);
            System.out.println("1. Показать все данные автомобилей");
            System.out.println("2. Показать всех производителей автомобилей");
            System.out.println("3. Показать все автомобили конкретного производителя");
            System.out.println("4. Показать все автомобили конкретного года выпуска");
            System.out.println("5. Фильтровать автомобили по цвету");
            System.out.println("6. Фильтровать автомобили по объему двигателя");
            System.out.println("7. Фильтровать автомобили по типу");
            System.out.println("0. выход");
            System.out.println("Выберите пункт меню:");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    showAllCars(connection,scanner);
                    break;
                case 2:
                    showAllManufacturers(connection,scanner);
                    break;
                case 3:
                    showCarsByManufacturer(connection,scanner);
                    break;
                case 4:
                    showCarsByYear(connection,scanner);
                    break;
                case 5:
                    filterCarsByColor(connection,scanner);
                    break;
                case 6:
                    filterCarsByEngineVolume(connection,scanner);
                    break;
                case 7:
                    filterCarsByType(connection,scanner);
                    break;
                case 0:
                    exit();
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте ещё раз");
            }

        }


    }
    public static void showAllCars(Connection connection,Scanner scanner){}
    public static void showAllManufacturers(Connection connection,Scanner scanner){}
    public static void showCarsByManufacturer(Connection connection,Scanner scanner){}
    public static void showCarsByYear(Connection connection,Scanner scanner){}
    public static void filterCarsByColor(Connection connection,Scanner scanner){}
    public static void filterCarsByEngineVolume(Connection connection,Scanner scanner){}
    public static void filterCarsByType(Connection connection,Scanner scanner){}

    public static void exit(){
        System.out.println("Спасибо за использование нашего приложения!");
    }
}
