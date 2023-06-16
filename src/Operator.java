import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Operator {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public Operator(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void sendTask(double a, double b, String op, int sec) throws IOException {
        // Отправляем задание на сервер
        out.writeDouble(a);
        out.writeDouble(b);
        out.writeUTF(op);
        out.writeInt(sec);
        out.flush();

        // Получаем результат от сервера
        double result = in.readDouble();

        // Выводим результат
        System.out.println("Result: " + result);
    }

    public static void main(String[] args) {
        try {
            Operator operator = new Operator("localhost", 12345);

            // Отправляем несколько заданий
            operator.sendTask(10, 5, "+", 2);
            operator.sendTask(12, 4, "*", 5);
            operator.sendTask(8, 0, "/", 3); // Ошибка деления на 0
        } catch (IOException e) {
            System.out.println("Error creating operator");
        }
    }
}