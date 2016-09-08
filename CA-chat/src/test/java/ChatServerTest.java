
import dummyclient.Client;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import server.ChatServer;

/**
 *
 * @author cherr
 */
public class ChatServerTest {

    private static String ip = "127.0.0.10";
    private static int port = 7777;
    private static String portString = "7777";
    private static Runnable run;
    public static Client c1, c2;
    private static ChatServer server;

    public ChatServerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] args = new String[2];
                args[0] = ip;
                args[1] = portString;
                server.main(args);
            }
        }).start();
//        c = new Client(ip,port,
//        new Runnable(){
//            @Override
//            public void run() {
//                String[] args = new String[2];
//                args[0] = ip;
//                args[1] = portString;
//                ChatServer.main(args);
//            }
//        });
    }

    @AfterClass
    public static void tearDownClass() {
        ChatServer.stopServer();
    }

    /*This test checks for expected server respons (if our client/server covers the protocol rules)
    based on only the loging and logout method. tested from both the client and server side.
    we put a sleep after sending logout, because we need to make sure the request gets to the server
    before we assert that the clints list is updated.
    */
    @Test
    public void testLoginLogout() throws InterruptedException {
        server = new ChatServer();
        assertTrue(server.clients.isEmpty());
        c1 = new Client(ip, port, run);
        c2 = new Client(ip, port, run);
        c1.send("LOGIN:fedtmule");
        assertEquals(1, server.clients.size());
        assertEquals("CLIENTLIST:FEDTMULE", c1.receive());
        c2.send("LOGIN:mickey");
        assertEquals(2, server.clients.size());
        assertEquals("CLIENTLIST:FEDTMULE,MICKEY", c1.receive());
        assertEquals("CLIENTLIST:FEDTMULE,MICKEY", c2.receive());
        c1.sendLogout();
        Thread.sleep(500); /*we cannot synchronize between client and server. 
        we DONT assume that list is updated "same time" as client sends logout.
        We wait 0,5 sec for the server to update before we do the final check*/
        assertEquals(1, server.clients.size());
        assertEquals(false, c1.isConnected());
        assertEquals("CLIENTLIST:MICKEY", c2.receive());
        c2.sendLogout();
        Thread.sleep(500);
        assertEquals(0, server.clients.size());
        assertEquals(false, c2.isConnected());
    }

//    
//    @Test
//    public void runServerThruMainTest(){
//        String[] args = new String[2];
//        args[0] = "localhost";
//        args[1] = "7777";
//        ChatServer.main(args);
//        assertTrue(ChatServer.keepRunning);
//    }
//    @Test
//    public void 
}
