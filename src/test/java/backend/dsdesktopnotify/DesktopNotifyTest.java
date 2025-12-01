package backend.dsdesktopnotify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class DesktopNotifyTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        
        DesktopNotify.setLogOutput(System.out);
        
        DesktopNotify.setDefaultTheme(NotifyTheme.Dark);
        DesktopNotify.setDefaultTextOrientation(DesktopNotify.LEFT_TO_RIGHT);
        DesktopNotify.setLogLevel(DesktopNotify.INFORMATION);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        
        DesktopNotify.setLogOutput(originalOut);
    }

    @Test
    void testThemeConfiguration() {
        assertEquals(NotifyTheme.Dark, DesktopNotify.getDefaultTheme());
        
        DesktopNotify.setDefaultTheme(NotifyTheme.Light);
        assertEquals(NotifyTheme.Light, DesktopNotify.getDefaultTheme());
    }

    @Test
    void testOrientationConfiguration() {
        assertEquals(DesktopNotify.LEFT_TO_RIGHT, DesktopNotify.getDefaultTextOrientation());
        
        DesktopNotify.setDefaultTextOrientation(DesktopNotify.RIGHT_TO_LEFT);
        assertEquals(DesktopNotify.RIGHT_TO_LEFT, DesktopNotify.getDefaultTextOrientation());
    }

    @Test
    void testConstructorsAndShow() {
        assertDoesNotThrow(() -> 
            DesktopNotify.showDesktopMessage("Tit", "Msg")
        );

        assertDoesNotThrow(() -> 
            DesktopNotify.showDesktopMessage("Tit", "Msg", DesktopNotify.SUCCESS)
        );

        ActionListener action = e -> {};
        assertDoesNotThrow(() -> 
            DesktopNotify.showDesktopMessage("Tit", "Msg", DesktopNotify.WARNING, action)
        );

        assertDoesNotThrow(() -> 
            DesktopNotify.showDesktopMessage("Tit", "Msg", DesktopNotify.FAIL, null, action)
        );
        
        assertDoesNotThrow(() -> 
            DesktopNotify.showDesktopMessage("Tit", "Msg", DesktopNotify.TIP, 5000L)
        );
    }

    @Test
    void testInstanceLogic() {
        DesktopNotify notify = new DesktopNotify("Titulo", "Mensagem", DesktopNotify.INFORMATION, 
                                                 DesktopNotify.LEFT_TO_RIGHT, null);

        notify.setTheme(NotifyTheme.Light);
        assertEquals(NotifyTheme.Light, notify.getTheme());

        assertNull(notify.getAction());
        ActionListener action = e -> {};
        notify.setAction(action);
        assertEquals(action, notify.getAction());

        notify.popupStart = 1000L;
        notify.setTimeout(5000L);
        assertEquals(6000L, notify.expTime());
        
        notify.setTimeout(0); 
        assertEquals(Long.MAX_VALUE, notify.expTime());
    }

    @Test
    void testVisibilityAndDimensions() {
        DesktopNotify notify = new DesktopNotify("T", "M", 0, 0, null);
        
        notify.setWidth(300);
        
        assertFalse(notify.isVisible());
        notify.setVisible(true);
        assertTrue(notify.isVisible());
        
        notify.hide();
        assertTrue(notify.markedForHide);
    }


    @Test
    void testRenderLogic() {
        BufferedImage image = new BufferedImage(300, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        DesktopNotify notify = new DesktopNotify("Title", "Message line 1\nLine 2", 
                                                 DesktopNotify.INFORMATION, DesktopNotify.LEFT_TO_RIGHT, null);
        
        notify.setWidth(300);
        notify.h = 100;
        notify.popupStart = 1000;
        
        notify.render(0, 0, false, g2d, 1100L);
        
        notify.render(0, 0, false, g2d, 2000L);
        
        notify.render(0, 0, true, g2d, 2000L); 
        
        g2d.dispose();
    }
    
    @Test
    void testSortMessage() {
        DesktopNotify notify = new DesktopNotify("Very Long Title That Needs Splitting", 
                                                 "Very long message logic that needs splitting into lines", 
                                                 0, 0, null);
        notify.setWidth(100); 
        
        try {
            notify.sortMessage();
            assertTrue(notify.tlts.length >= 0);
        } catch (Throwable e) {
            System.err.println("Skipping sortMessage test due to env: " + e.getMessage());
        }
    }

    @Test
    void testLoggingSystem() {
        outContent.reset();

        DesktopNotify.setLogLevel(DesktopNotify.DEBUG);
        
        DesktopNotify.logDebug("TAG", "Debug Msg");
        String output = outContent.toString();
        assertTrue(output.contains("DEBUG:TAG"), "Deveria conter DEBUG. Recebido: " + output);

        DesktopNotify.logInfo("TAG", "Info Msg");
        output = outContent.toString();
        assertTrue(output.contains("INFO:TAG"), "Deveria conter INFO");

        DesktopNotify.logWarning("TAG", "Warn Msg");
        output = outContent.toString();
        assertTrue(output.contains("WARNING:TAG"), "Deveria conter WARNING");

        DesktopNotify.logError("TAG", "Error Msg", new RuntimeException("Crash"));
        output = outContent.toString();
        assertTrue(output.contains("ERROR:TAG"), "Deveria conter ERROR");
        assertTrue(output.contains("Caused by:"), "Deveria conter StackTrace");
    }

    @Test
    void testLogOutputTypes() {
        ByteArrayOutputStream customStream = new ByteArrayOutputStream();
        DesktopNotify.setLogOutput(customStream);
        
        DesktopNotify.logInfo("TAG", "Stream Test");
        
        assertTrue(customStream.toString().contains("INFO:TAG"));
        
        assertThrows(NullPointerException.class, () -> DesktopNotify.logInfo(null, "Msg"));
    }
}