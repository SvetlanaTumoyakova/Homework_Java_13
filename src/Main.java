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

            TableCreationStatus resultCreateTable = createTable(stmt);
            if(resultCreateTable == TableCreationStatus.CREATED){
                System.out.println("Таблица cars создана успешно!");
            }else if(resultCreateTable == TableCreationStatus.ERROR){
                System.err.println("Не удалось создать таблицу cars");
            }

            TableCreationStatus resultInsertAuto = insertSampleCarData(conn);
            if(resultInsertAuto == TableCreationStatus.CREATED){
                System.out.println("Данные успешно добавлены!");
            }else if(resultInsertAuto == TableCreationStatus.ERROR){
                System.err.println("Не удалось добавить данные в таблицу cars");
            }

            run(conn);


            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public enum TableCreationStatus {
        CREATED,        // Операция успешна (таблица создана / данные вставлены)
        ALREADY_EXISTS, // Таблица уже есть (для createTable)
        ERROR           // Произошла ошибка
    }

    public static TableCreationStatus createTable(Statement stmt) {
        try (ResultSet rs = stmt.executeQuery(
                "SELECT COUNT(*) FROM information_schema.tables " +
                        "WHERE table_schema = DATABASE() AND table_name = 'cars'")) {

            boolean tableExists = rs.next() && rs.getInt(1) > 0;

            if (tableExists) {
                return TableCreationStatus.ALREADY_EXISTS;
            }
            stmt.executeUpdate(
                    "CREATE TABLE cars (" +
                            "id INT PRIMARY KEY AUTO_INCREMENT, " +
                            "manufacturer VARCHAR(50) NOT NULL, " +
                            "model VARCHAR(50) NOT NULL, " +
                            "engine_volume DECIMAL(3,1), " +
                            "year INT, " +
                            "color VARCHAR(30), " +
                            "car_type VARCHAR(20)" +
                            ")");
            return TableCreationStatus.CREATED;

        } catch (SQLException e) {
            e.printStackTrace();
            return TableCreationStatus.ERROR;
        }
    }

    public static TableCreationStatus insertSampleCarData(Connection conn) {
        Object[][] sampleData = {
                {"Toyota", "Camry", 2.5, 2023, "Silver", "Sedan"},
                {"BMW", "X5", 3.0, 2022, "Black", "SUV"},
                {"Ford", "Mustang", 5.0, 1969, "Blue", "Coupe"},
                {"Honda", "Civic", 1.8, 2021, "White", "Hatchback"},
                {"Volkswagen", "Passat", 2.0, 2022, "Gray", "Sedan"}
        };

        String sql = "INSERT INTO cars " +
                "(manufacturer, model, engine_volume, year, color, car_type) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int successCount = 0;

            for (Object[] row : sampleData) {
                String manufacturer = (row[0] instanceof String) ? (String) row[0] : null;
                String model = (row[1] instanceof String) ? (String) row[1] : null;
                Double engineVolume = (row[2] instanceof Double) ? (Double) row[2] : null;
                Integer year = (row[3] instanceof Integer) ? (Integer) row[3] : null;
                String color = (row[4] instanceof String) ? (String) row[4] : null;
                String carType = (row[5] instanceof String) ? (String) row[5] : null;

                pstmt.setString(1, manufacturer);
                pstmt.setString(2, model);
                pstmt.setObject(3, engineVolume);
                pstmt.setObject(4, year);
                pstmt.setString(5, color);
                pstmt.setString(6, carType);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    successCount++;
                }
            }

            return (successCount == sampleData.length)
                    ? TableCreationStatus.CREATED
                    : TableCreationStatus.ERROR;
        } catch (SQLException e) {
            e.printStackTrace();
            return TableCreationStatus.ERROR;
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
