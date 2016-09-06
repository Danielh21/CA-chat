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
//    Protocol protocol;
    String username;
    
    public ClientHandler(Socket s) throws IOException{
        input = new Scanner (s.getInputStream());
        writer = new PrintWriter(s.getOutputStream(), true);
        this.s = s;
    }
    
    public void send(String message){
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
    String msg = input.nextLine(); //IMPORTANT blocking call
    Logger.getLogger(Log.logName).log(Level.INFO,String.format("Received the message from "+username+": %1$S ", msg));
    while (!msg.equals("LOGOUT:")) { //has to be changed to actually use the protocol and NOT be hardcoded strings like this
//        msg = protocol(msg);
//        
//        send(msg);//to who ? we should do a check if "to all" or "to specific"
//        //right now send method sends to all.
//        else
//        
//        Server.sendToAll(msg);
        
      Logger.getLogger(Log.logName).log(Level.INFO,String.format("Received the message from "+username+": %1$S ", msg ));
      msg = input.nextLine(); //IMPORTANT blocking call
    }
    writer.println("connection ended");
        try {
            s.close();
            ChatServer.removeHandler(this);
        } catch (IOException ex) {
             Logger.getLogger(Log.logName).log(Level.SEVERE,"Failed at closing for user " + username);
        }
    Logger.getLogger(Log.logName).log(Level.INFO,"Closed a Connection for user: " + username);
        
    }
    
}