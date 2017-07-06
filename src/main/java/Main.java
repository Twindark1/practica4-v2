import org.h2.tools.Server;
import routes.Routes;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        openPort();
        Routes.startProyect();
    }

    private static void openPort() {

        try {
            Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
