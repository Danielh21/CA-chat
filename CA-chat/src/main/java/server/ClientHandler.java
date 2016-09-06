package server;

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
    
    public ClientHandler(Socket s) throws IOException{
        input = new Scanner (s.getInputStream());
        writer = new PrintWriter(s.getOutputStream(), true);
        this.s = s;
    }
    
    public void send(String message){
        writer.println(message);
    }
    
    @Override
    public void run(){ 
    String msg = input.nextLine(); //IMPORTANT blocking call
    Logger.getLogger(Log.LOG_NAME).log(Level.INFO,String.format("Received the message: %1$S ", msg));
    while (!msg.equals("LOGOUT:")) {
        Server.send(msg.toUpperCase());
      Logger.getLogger(Log.LOG_NAME).log(Level.INFO,String.format("Received the message: %1$S ", msg.toUpperCase()));
      msg = input.nextLine(); //IMPORTANT blocking call
    }
    writer.println("connection ended");
        try {
            s.close();
            Server.removeHandler(this);
        } catch (IOException ex) {
             Logger.getLogger(Log.LOG_NAME).log(Level.SEVERE,"Failed at closing");
        }
    Logger.getLogger(Log.LOG_NAME).log(Level.INFO,"Closed a Connection");
        
    }
    
}