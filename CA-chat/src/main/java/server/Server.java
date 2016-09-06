package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private String ip;
    private int port;
    private static CopyOnWriteArrayList<ClientHandler> clients;

    private static void handleClient(Socket socket) throws IOException {
        Scanner input = new Scanner(socket.getInputStream());
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        String message = input.nextLine(); //IMPORTANT blocking call
        Logger.getLogger(Log.LOG_NAME).log(Level.INFO, String.format("Received the message: %1$S ", message));
        //protocol goes here

    }

    private void runServer(String ip, int port) {
        this.port = port;
        this.ip = ip;

        Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Server started. Listening on: " + port + ", bound to: " + ip);
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Connected to a client");
                ClientHandler ch = new ClientHandler(socket);
                clients.add(ch);
                ch.start();
            } while (keepRunning);
        } catch (IOException ex) {
            Logger.getLogger(Log.LOG_NAME).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized void send(String message) {
        for (ClientHandler client : clients) {
            client.send(message);
        }
    }

    public static void stopServer() {
        keepRunning = false;
    }

    public static synchronized void removeHandler(ClientHandler ch) {
        clients.remove(ch);
    }

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                throw new IllegalArgumentException("Error: Use like: java -jar EchoServer.jar <ip> <port>");
            }
            String ip = args[0];
            int port = Integer.parseInt(args[1]);
            Log.setLogFile("logFile.txt", "ServerLog");
            Logger.getLogger(Log.LOG_NAME).log(Level.INFO, "Starting the Server");
            new Server().runServer(ip, port);
        } catch (Exception e) {
            Logger.getLogger(Log.LOG_NAME).log(Level.SEVERE, null, e);
        } finally {
            Log.closeLogger();
        }
    }

}
