

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import server.Decoder;

/**
 *
 * @author cherr
 */
public class ProtocolTest {
    
    Decoder d;
    String inputMessage = "MSG:User1,User2:This is just a test.";
    
    public ProtocolTest() {
    }
    

    @Test
    public void getRecipientsListTest(){
        d = new Decoder();
        d.splitLine(inputMessage);
        ArrayList<String> expectedRecipients = new ArrayList<>();
        expectedRecipients.add("USER1");
        expectedRecipients.add("USER2");
        assertEquals(expectedRecipients,d.getList());
    }
    
    @Test
    public void generateResponseTest(){
        String expectedResponse = "MSGRES:User1:This is just a test.";
        d = new Decoder();
        d.setCurrentUserName("User1");
        d.splitLine(inputMessage);
        String result=d.getMesRes();
        assertEquals(expectedResponse,result);
    }
}
