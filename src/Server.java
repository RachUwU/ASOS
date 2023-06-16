import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private final ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        while (true) {
            // Подключение нового агента
            Socket socket = serverSocket.accept();
            System.out.println("New agent connected");
            //получение данных от клиента
            Agent agent = new Agent(socket);
            agent.start();

        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("Server started...");
            Server server = new Server(12345);
            server.start();
            System.out.println("...Server ended");
        } catch (IOException e) {
            System.out.println("Error creating server");
        }
    }
}
