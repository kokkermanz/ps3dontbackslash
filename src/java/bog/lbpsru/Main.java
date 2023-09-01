package bog.lbpsru;

import bog.lbpsru.components.ControllerListener;
import bog.lbpsru.components.Skin;
import bog.lbpsru.components.TasServer;
import bog.lbpsru.gui.LBPSRUtil;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.ArrayList;

/**
 * @author Bog
 */
public class Main {

    public static ControllerListener listener;
    public static TasServer server;
    public static LBPSRUtil lbpsrUtil = new LBPSRUtil();
    public static JFrame mainForm;
    private static long lastMillis = 0;
    public static Thread statKeeper;
    public static ArrayList<Skin> skins;

    public static void main(String args[]) {
        mainForm = new JFrame("LBP Speedrun Utility");
        mainForm.setContentPane(lbpsrUtil.mainForm);
        try {
            mainForm.setIconImage(ImageIO.read(Main.class.getResourceAsStream("/icon.png")));
        }catch (Exception e){e.printStackTrace();}
        LafManager.setTheme(new DarculaTheme());
        LafManager.install();
        mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainForm.setResizable(false);
        mainForm.setVisible(true);
        mainForm.pack();

        listener = new ControllerListener("0.0.0.0", 1337);
        lbpsrUtil.init(listener, server);

        statKeeper = new Thread() {
            public void run() {
                try
                {
                    while (true)
                    {
                        if(System.currentTimeMillis() - lastMillis > 250)
                        {
                            lbpsrUtil.statKeeper(listener, server);
                            listener.statKeeper();
                            lastMillis = System.currentTimeMillis();
                        }
                    }
                } catch(Exception v) {
                    v.printStackTrace();
                }
            }
        };
        statKeeper.start();
    }

}