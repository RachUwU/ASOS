import java.io.*;
import java.net.Socket;

public class Agent extends Thread {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public Agent(Socket socket) throws IOException {
        this.socket = socket;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String inputLine = bufferedReader.readLine();
                if(inputLine.equals("quit"))break;
                try {
                    if (isValidString(inputLine)) {
                        double result = splitAndCalculate(inputLine);
                        // Отправляем результат на сервер
                        String str = "Result: " + result + "\n\r";
                        out.writeUTF(str);
                        out.flush();
                    } else {
                        out.writeUTF("Input errors\n\r");
                        out.flush();
                    }
                } catch (ArithmeticException e){
                    out.writeUTF("Division by zero is forbidden\n\r");
                    out.flush();
                }
            }
        } catch (IOException e) {
            // В случае ошибки закрываем соединение
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error closing socket");
            }
        }
    }
    public static double splitAndCalculate(String str) {
        String[] parts = str.split("\\s+"); // Разбиваем строку на части по пробелу
        double first = Double.parseDouble(parts[0]); // Пытаемся распознать первое число
        double second = Double.parseDouble((parts[2])); // Пытаемся распознать второе число
        int timeout = Integer.parseInt(parts[3]); // Пытаемся распознать третье число
        String operator = parts[1];
        return calculate(first, second, operator, timeout);
    }
    private static double calculate(double a, double b, String op, int sec) {
        // Задержка на выполнение задания
        try {
            Thread.sleep(sec * 1000L);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping thread");
        }
        // Выполнение операции
        switch (op) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0) {
                    throw new ArithmeticException("Division by zero!");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("Invalid operation: " + op);
        }
    }
    public static boolean isValidString(String str) {
        if(str == null) return false;
        String[] parts = str.split("\\s+"); // Разбиваем строку на части по пробелу
        if (parts.length != 4) { // Если количество частей не равно 3, значит, строка не соответствует формату
            return false;
        }
        try {
            Integer.parseInt(parts[0]); // Пытаемся распознать первое число
            Integer.parseInt(parts[2]); // Пытаемся распознать второе число
            Integer.parseInt(parts[3]); // Пытаемся распознать третье число
            if (!("+".equals(parts[1]) || "-".equals(parts[1]) || "*".equals(parts[1]) || "/".equals(parts[1]))) {
                // Если вторая часть не является одним из допустимых символов, значит, строка не соответствует формату
                return false;
            }
        } catch (NumberFormatException e) {
            // Если в первой или второй части оказалась не целочисленная строка, значит, строка не соответствует формату
            return false;
        }
        return true;
    }
}