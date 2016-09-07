/*
 * 
 */
package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cherry
 */
public class Client extends Observable{
    Socket socket;
    private int port;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;
    private Boolean stop;
    private String ip;
    private String message;
    
    Client(ClientGUI gui){
        //get port and ip from the gui
        port = ClientGUI.getPort();
        ip = ClientGUI.getAddress();
        connect(ip,port);
    }

    private void connect(String ip, int port) {
        try{
        this.port = port;
        serverAddress = InetAddress.getByName(ip);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
        stop = false;
        }catch(Exception ex){
            System.out.println("ERROR:" + ex);
        }
    }
    
     
     public void send(String msg){
         output.println(msg);
     }
     
     public String receive() {
        message = input.nextLine(); // Blocking call
        if (message.equals("LOGOUT")) {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);// We should have a logger
            }
        }

        setChanged();
        notifyObservers(message);
        return message;
    }
     
     public boolean isStopped(){
         return stop;
     }
}
