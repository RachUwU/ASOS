import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
                // Получаем задание от сервера
                double a = in.readDouble();
                double b = in.readDouble();
                String op = in.readUTF();
                int sec = in.readInt();

                // Выполняем задание
                double result = calculate(a, b, op, sec);

                // Отправляем результат на сервер
                out.writeDouble(result);
                out.flush();
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
//    public void run() {
//        try {
////            while (true) {
//                // Получаем задание от сервера
//            String str = in.readUTF();
//            str = "esfeses";
//            System.out.println("a222aa" + str);
//            double d = 2;
//            out.writeDouble(d);
//            out.flush();
////            if(isValidString(str)){
////                    double result = splitAndCalculate(str);
////                    str += " " + result;
////                    // Отправляем результат на сервер
////                    System.out.println("aaa" + str);
////                    out.writeUTF(str);
////                    out.flush();
////                }
////                else{
////                    out.writeUTF("Incorrect input");
////                    out.flush();
////                }
//////            }
//        } catch (IOException e) {
//            // В случае ошибки закрываем соединение
//            try {
//                socket.close();
//            } catch (IOException ex) {
//                System.out.println("Error closing socket");
//            }
//        }catch (ArithmeticException | IllegalArgumentException e){
//            System.out.println("Arithmetic error");
//        }
//    }
    public static double splitAndCalculate(String str) {
        String[] parts = str.split("\\s+"); // Разбиваем строку на части по пробелу
        int first = Integer.parseInt(parts[0]); // Пытаемся распознать первое число
        int second = Integer.parseInt(parts[2]); // Пытаемся распознать второе число
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