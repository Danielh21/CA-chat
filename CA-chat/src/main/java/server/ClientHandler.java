package server;

import Log.Log;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread {

    Scanner input;
    PrintWriter writer;
    Socket s;
    boolean loggedIn;
    String username;
    Decoder decoder;

    public ClientHandler(Socket s) throws IOException {
        input = new Scanner(s.getInputStream());
        writer = new PrintWriter(s.getOutputStream(), true);
        this.s = s;
        decoder = new Decoder();
    }

    public void sendToClient(String message) {
        writer.println(message);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        userLogin();
        decoder.setCurrentUserName(username);
        String msg = input.nextLine(); //IMPORTANT blocking call
        Logger.getLogger(Log.logName).log(Level.INFO, String.format("Received the message from " + username + ": %1$S ", msg));
        while (!msg.equals("LOGOUT:")) {
            decoder.splitLine(msg);
            Logger.getLogger(Log.logName).log(Level.INFO, String.format("Received the message from " + username + ": %1$S ", msg));
            msg = input.nextLine(); //IMPORTANT blocking call
        }
        writer.println("connection ended");
        try {
            ChatServer.removeHandler(this);
            activeClients();
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(Log.logName).log(Level.SEVERE, "Failed at closing for user " + username);
        }
        Logger.getLogger(Log.logName).log(Level.INFO, "Closed a Connection for user: " + username);

    }

    private void userLogin() {
        loggedIn = true;
        String message;
        try {
            do {
                message = input.nextLine(); // Blocking Call
                message = message.toUpperCase();
                String[] loginMessage = message.split(":");
                if (loginMessage[0].equals("LOGIN") && loginMessage.length == 2) {
                    loggedIn = false;
                    username = loginMessage[1];
                    activeClients();
                } else {
                    Logger.getLogger(Log.logName).log(Level.INFO, "Failed login attempt");
                }

            } while (loggedIn);
        } catch (Exception e) {
            Logger.getLogger(Log.logName).log(Level.INFO, "Failed login attempt");
        }
    }

    private void activeClients() {
        String output = "CLIENTLIST:";
        for (ClientHandler handler : ChatServer.clients) {
            if (handler.username != null) {
                output = output + handler.username + ",";
            }
        }
        output = output.substring(0, output.length() - 1);
        ChatServer.sendToAllClients(output);
    }

}
