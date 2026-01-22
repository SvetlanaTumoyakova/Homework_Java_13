import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class Main {

    public static void main(String[] args) {

        try {
            Dotenv dotenv = Dotenv.configure().load();
            String url = dotenv.get("DB_URL");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASSWORD");
            Connection conn = DriverManager.getConnection(url, user, password);

            Statement stmt = conn.createStatement();
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
            System.out.println("Таблица cars создана успешно!");

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
