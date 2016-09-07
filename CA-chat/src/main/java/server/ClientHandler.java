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
    boolean notLoggedin;
//    Protocol protocol;
    String username;
    Decoder decoder;
    
    public ClientHandler(Socket s) throws IOException{
        input = new Scanner (s.getInputStream());
        writer = new PrintWriter(s.getOutputStream(), true);
        this.s = s;
        decoder = new Decoder();
    }
    
    public void sendToClient(String message){
        writer.println(message);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public void run(){
    userLogin();
    decoder.setCurrentUserName(username);
    String msg = input.nextLine(); //IMPORTANT blocking call
    Logger.getLogger(Log.logName).log(Level.INFO,String.format("Received the message from "+username+": %1$S ", msg));
    while (!msg.equals("LOGOUT:")) { //has to be changed to actually use the protocol and NOT be hardcoded strings like this
//        msg = protocol(msg);
//        
//        sendToClient(msg);//to who ? we should do a check if "to all" or "to specific"
//        //right now sendToClient method sends to all.
//        else
//        
//        Server.sendToAll(msg);
//        ChatServer.sendToAllClients(msg);
        decoder.splitLine(msg);
      Logger.getLogger(Log.logName).log(Level.INFO,String.format("Received the message from "+username+": %1$S ", msg ));
      msg = input.nextLine(); //IMPORTANT blocking call
    }
    writer.println("connection ended");
        try {
            ChatServer.removeHandler(this);
            newUserLogedOn();
            s.close();
        } catch (IOException ex) {
             Logger.getLogger(Log.logName).log(Level.SEVERE,"Failed at closing for user " + username);
        }
    Logger.getLogger(Log.logName).log(Level.INFO,"Closed a Connection for user: " + username);
        
    }

    private void userLogin() {
        writer.println("Hello and Welcome to the Chat server Please login");
        notLoggedin = true;
        String message;
        try {
            do {
                message = input.nextLine(); // Blocking Call
                message = message.toUpperCase();
                String[] loginMessage = message.split(":");
                if (loginMessage[0].equals("LOGIN") && loginMessage.length == 2) {
                    notLoggedin = false;
                    username = loginMessage[1];
                    newUserLogedOn();
                } else {
                    writer.println("Syntax Error");
                }
                
            } while (notLoggedin);
        } catch (Exception e) {
            writer.println("Syntax Error");
        }
    }

    private void newUserLogedOn() {
        String output="CLIENTLIST:";
        for (ClientHandler handler : ChatServer.clients) {
            if(handler.username != null)
            output = output + handler.username + ", ";
        }
        ChatServer.sendToAllClients(output);
    }
    
}