package server;

import java.util.ArrayList;

public class Decoder {

    private String cmd;
    private ArrayList connectedUsers;

    public Decoder(String input) {

        String[] splitString = input.split(":");

        if (splitString.length > 1) {

            switch (splitString[0]) {
                case "LOGIN":
                    login();
                    break;
                case "MSG":
                    sendMsg();
                    break;
                case "LOGOUT":
                    logout();
                    break;
                default:
                    cmd = "SYNTAX ERROR";

            }

        }

    }

    public String getCmd() {
        return cmd;
    }

    

    private void login() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void sendMsg() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void logout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
