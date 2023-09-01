package bog.lbpsru.gui;

import bog.lbpsru.Main;
import bog.lbpsru.components.ControllerListener;
import bog.lbpsru.components.Skin;
import bog.lbpsru.components.SkinRenderer;
import bog.lbpsru.components.TasServer;
import bog.lbpsru.components.utils.Patch;
import bog.lbpsru.components.utils.Utils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Bog
 */
public class LBPSRUtil {
    public JPanel mainForm;
    private JTabbedPane tabbedPane1;
    private JTextField UDPListenerIP;
    private JTextField UDPListenerPort;
    private JButton setListenerIP;
    private JButton setListenerPort;
    private JButton startUDPListener;
    private JButton stopUDPListener;
    private JLabel listenerIPLabel;
    private JLabel listenerPortLabel;
    private JLabel listenerRunning;
    public JComboBox<String> player1Combo;
    public JComboBox<String> player2Combo;
    public JComboBox<String> player3Combo;
    public JComboBox<String> player4Combo;
    private JButton startPlayer1;
    private JButton startPlayer2;
    private JButton startPlayer3;
    private JButton startPlayer4;
    private JButton refreshSkinsButton;
    private JLabel playerCount;
    private JLabel listenerActivity;
    private JButton patchELFFileButton;
    private JCheckBox legacyFileDialogueCheckBox;

    public void init(ControllerListener listener, TasServer server)
    {
        setListenerIP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = UDPListenerIP.getText();
                if(Utils.validateIP(ip, mainForm))
                    listener.setIp(ip);
            }
        });
        setListenerPort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String port = UDPListenerPort.getText();
                int validated = Utils.validatePort(port, mainForm);
                if(validated != -1)
                    listener.setPort(validated);
            }
        });
        startUDPListener.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.startListener();
            }
        });
        stopUDPListener.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.stopListener();
            }
        });
        refreshSkinsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                    if(path.startsWith("/"))
                        path = path.substring(1);
                    path = path.substring(0, path.lastIndexOf("/") + 1);

                    File controllerskins = new File(path + "controllerskins/");
                    Main.skins.clear();
                    player1Combo.removeAllItems();
                    player2Combo.removeAllItems();
                    player3Combo.removeAllItems();
                    player4Combo.removeAllItems();

                    File[] files = controllerskins.listFiles();

                    if(files == null)
                    {
                        JOptionPane.showMessageDialog(mainForm, "Controllerskins folder is missing.", "ERROR!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    for(File file : Objects.requireNonNull(files))
                        Main.skins.add(SkinRenderer.readSkin(file.getPath(), file.getName()));

                    for(Skin skin : Main.skins)
                    {
                        player1Combo.addItem(skin.name);
                        player2Combo.addItem(skin.name);
                        player3Combo.addItem(skin.name);
                        player4Combo.addItem(skin.name);
                    }
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
        startPlayer1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new SkinRenderer(Main.skins.get(player1Combo.getSelectedIndex()), Main.listener.Player1, "Player 1");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        startPlayer2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new SkinRenderer(Main.skins.get(player2Combo.getSelectedIndex()), Main.listener.Player2, "Player 2");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        startPlayer3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new SkinRenderer(Main.skins.get(player3Combo.getSelectedIndex()), Main.listener.Player3, "Player 3");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        startPlayer4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new SkinRenderer(Main.skins.get(player4Combo.getSelectedIndex()), Main.listener.Player4, "Player 4");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        patchELFFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    File elf = legacyFileDialogueCheckBox.isSelected() ? Utils.openFileLegacy("elf") : Utils.openFile("elf");
                    byte[] outData = Files.readAllBytes(Path.of(elf.getPath()));
                    Patch.applyPatches(outData, Patch.ebootPatch());
                    Files.write(Path.of(elf.getPath() + ".patched"), outData);
                    JOptionPane.showMessageDialog(mainForm, "Patched file: " + elf.getPath() + ".patched", "Done!", JOptionPane.PLAIN_MESSAGE);
                }catch (Exception ex){ex.printStackTrace();}
            }
        });

        try {
            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if(path.startsWith("/"))
                path = path.substring(1);
            path = path.substring(0, path.lastIndexOf("/") + 1);

            File controllerskins = new File(path + "controllerskins/");

            Main.skins = new ArrayList<>();

            for(File file : Objects.requireNonNull(controllerskins.listFiles()))
                Main.skins.add(SkinRenderer.readSkin(file.getPath(), file.getName()));

            for(Skin skin : Main.skins)
            {
                player1Combo.addItem(skin.name);
                player2Combo.addItem(skin.name);
                player3Combo.addItem(skin.name);
                player4Combo.addItem(skin.name);
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    public void statKeeper(ControllerListener listener, TasServer server)
    {
        listenerIPLabel.setText(listener.getIp());
        listenerPortLabel.setText(Integer.toString(listener.getPort()));
        listenerRunning.setText(listener.isStopped() ? "Stopped" : "Running");
        playerCount.setText(Integer.toString(listener.playerCount));
        listenerActivity.setText(listener.isStopped() ? "Inactive" : listener.isReceivingData() ? "Active" : "Waiting");
    }

    private void createUIComponents() {

    }
}
