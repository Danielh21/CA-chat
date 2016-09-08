

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import server.Decoder;

/**
 *
 * @author cherr
 */
public class ProtocolTest {
    
    Decoder d;
    
    public ProtocolTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void getRecipientsListTest(){
        d = new Decoder();
        String inputMessage = "MSG:User1,User2:This is a test for some recipients.";
        d.splitLine(inputMessage);
        ArrayList<String> expectedRecipients = new ArrayList<>();
//        expectedRecipients.add();
        expectedRecipients.add("User1");
        expectedRecipients.add("User2");
        assertEquals(expectedRecipients,d.getList());

    }
}
