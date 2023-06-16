import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inputLine;
            inputLine = in.readLine();
            if(inputLine.equals("quit")) break;
            System.out.println("Received message: " + inputLine);
            // Создание потока для агента
            Agent agent = new Agent(socket);
            agent.start();
            //отправка данных обратно клиенту
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(inputLine);
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
