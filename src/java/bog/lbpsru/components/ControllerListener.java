package bog.lbpsru.components;

import bog.lbpsru.components.utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author Bog
 */
public class ControllerListener {

    private InetAddress ipaddress;
    private String ip;
    private int port;
    public long lastMillis = 0;

    private DatagramSocket socket;
    private DatagramPacket packet;

    public int playerCount = 0;

    public PlayerInputs Player1 = new PlayerInputs();

    public PlayerInputs Player2 = new PlayerInputs();

    public PlayerInputs Player3 = new PlayerInputs();

    public PlayerInputs Player4 = new PlayerInputs();

    private Thread thread;
    private boolean running = false;
    public ControllerListener(String ip, int port)
    {
        try
        {
            this.ipaddress = InetAddress.getByName(this.ip);
            this.port = port;
            this.ip = ip;
        }
        catch (Exception e) {e.printStackTrace();}
    }

    public void setIp(String ip) {
        try
        {
            this.ipaddress = InetAddress.getByName(this.ip);
            this.ip = ip;
        }
        catch (Exception e) {e.printStackTrace();}

        if(!isStopped())
        {
            stopListener();
            startListener();
        }
    }

    public String getIp()
    {
        return ip;
    }

    public void setPort(int port)
    {
        this.port = port;

        if(!isStopped())
        {
            stopListener();
            startListener();
        }
    }

    public int getPort()
    {
        return port;
    }

    public void update() throws IOException {
        socket.receive(packet);

        playerCount = Utils.getIntBitsFromBuffer(packet.getData(), 0);

        if(playerCount >= 1)
            Player1.update(Utils.getIntBitsFromBuffer(packet.getData(), 1),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 2)),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 3)),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 4)),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 5)));

        if(playerCount >= 2)
            Player2.update(Utils.getIntBitsFromBuffer(packet.getData(), 6),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 7)),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 8)),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 9)),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 10)));

        if(playerCount >= 3)
            Player3.update(Utils.getIntBitsFromBuffer(packet.getData(), 11),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 12)),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 13)),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 14)),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 15)));

        if(playerCount >= 4)
            Player4.update(Utils.getIntBitsFromBuffer(packet.getData(), 16),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 17)),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 18)),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 19)),
                    Float.intBitsToFloat(Utils.getIntBitsFromBuffer(packet.getData(), 20)));

        packet.setLength(packet.getData().length);
    }

    public void startListener()
    {
        thread = new Thread() {
            public void run() {
                try
                {
                    while (running)
                    {
                        if(socket.isClosed())
                            stopListener();

                        update();
                        lastMillis = System.currentTimeMillis();
                    }
                } catch(Exception v) {
                    v.printStackTrace();
                }
            }
        };

        try
        {
            if(socket != null && socket.isBound())
                socket.close();

            socket = new DatagramSocket(this.port, this.ipaddress);
            byte[] buffer = new byte[1024];
            packet = new DatagramPacket(buffer, buffer.length);
            running = true;
            thread.start();
        }catch (Exception e)
        {
            stopListener();
            e.printStackTrace();
        }
    }

    public void stopListener()
    {
        running = false;
        thread.stop();
        thread = null;
    }

    public boolean isStopped()
    {
        return thread == null;
    }

    public boolean isReceivingData()
    {
        return System.currentTimeMillis() - lastMillis < 100;
    }

    public void statKeeper()
    {
        if(!isReceivingData())
        {
            playerCount = 0;
            Player1.update(0, 0, 0, 0, 0);
            Player2.update(0, 0, 0, 0, 0);
            Player3.update(0, 0, 0, 0, 0);
            Player4.update(0, 0, 0, 0, 0);
        }
    }
}