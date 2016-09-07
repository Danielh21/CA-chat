package server;

import Log.Log;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer {

    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private String ip;
    private int port;
    public static CopyOnWriteArrayList<ClientHandler> clients;

    public ChatServer() {
        clients = new CopyOnWriteArrayList<>();
    }
    
    

    private static void handleClient(Socket socket) throws IOException {
        Scanner input = new Scanner(socket.getInputStream());
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        String message = input.nextLine(); //IMPORTANT blocking call
        Logger.getLogger(Log.logName).log(Level.INFO, String.format("Received the message: %1$S ", message));
        //protocol goes here

    }

    private void runServer(String ip, int port) {
        this.port = port;
        this.ip = ip;

        Logger.getLogger(Log.logName).log(Level.INFO, "Server started. Listening on: " + port + ", bound to: " + ip);
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do {
                Socket socket = serverSocket.accept(); //Important Blocking call
                Logger.getLogger(Log.logName).log(Level.INFO, "Connected to a client");
                ClientHandler ch = new ClientHandler(socket);
                clients.add(ch);
                ch.start();
            } while (keepRunning);
        } catch (IOException ex) {
            Logger.getLogger(Log.logName).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized void sendToAllClients(String message) {
        clients.stream().forEach((client) -> {
            if(client.username != null)
            client.sendToClient(message);
        });
    }

    public static void stopServer() {
        Log.closeLogger();
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
            Log.startLogFile();
            Logger.getLogger(Log.logName).log(Level.INFO, "Starting the Server");
            new ChatServer().runServer(ip, port);
        } catch (Exception e) {
            Logger.getLogger(Log.logName).log(Level.SEVERE, null, e);
        } finally {
            Log.closeLogger();
        }
    }

}

