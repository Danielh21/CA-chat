/*
 * 
 */
package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cherry
 */
public class Client extends Observable {

    Socket socket;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;
    private Boolean stop;
    private String message;
    private boolean connected = true;

    Client() {
        
    }
    
    public boolean connectToServer(String ip, int port, Runnable listener){
        try {
            serverAddress = InetAddress.getByName(ip);
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddress, port));
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
            new Thread(listener).start();
            stop = false;
            sendConnectedToGUI("Connected to " + socket.getInetAddress().toString() + " Please Login:");
            return true;
        } catch (Exception ex) {
            sendConnectedToGUI("Connection to " + ip + ":" + port + " Failed");
            System.out.println("ERROR:" + ex);
            return false;
        }
        
    }
    
    

    String hej;
    
    private void send(String msg) {
        if(socket == null){
            return;
        }
        if (socket.isConnected()) {
            output.println(msg);
        }
    }

    public void sendLogin(String username) {
        send("LOGIN:" + username);
    }

    public void sendLogout() {
        send("LOGOUT:");
        connected = false;
    }

    public void sendMessage(List<String> users, String msg) { // takes a string array of users and builds a message to the users in the array.
        StringWriter sw = new StringWriter();
        sw.write("MSG:");
        boolean first = true;
        for (String user : users) {
            if (first) {
                first = false;
            } else {
                sw.write(",");
            }
            sw.write(user);
        }
        sw.write(":");
        sw.write(msg);
        send(sw.toString());
        System.out.println(sw.toString());
    }

    public void sendToAll(String msg) { //sendMessage() sends to all when the array is empty.
        sendMessage(new ArrayList<String>(), msg);
    }

    public String receive() {
        message = input.nextLine(); // Blocking call
        if (message.equals("LOGOUT:")) {
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

    public boolean isStopped() {
        return stop;
    }

    public void listen() {
        String incommingMessage;
        do {
            incommingMessage = input.nextLine();
            setChanged();
            notifyObservers(incommingMessage);

        } while (connected && socket.isConnected());
    }
}
