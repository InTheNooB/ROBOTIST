package app.wrk.server;

import static app.consts.Constants.CLIENT_SLEEP_DURATION_AFTER_READLINE;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dingl01
 */
public class Client extends Thread {

    private volatile Socket socket;
    private volatile BufferedReader in;
    private volatile PrintWriter out;

    private volatile InetAddress ipAddress;

    private volatile boolean running;
    private volatile long startTime;

    private volatile ItfServerWrkClient refServerWrk;

    // This object is used to notify() 
    // a thread after the client has been initialised
    private final Object objToWaitToIndicateClientIsRunning;

    public Client(Object objToWaitToIndicateClientIsRunning, String name, Socket socket, ServerWrk serverWrk) {
        super(name);
        this.objToWaitToIndicateClientIsRunning = objToWaitToIndicateClientIsRunning;
        this.socket = socket;
        this.refServerWrk = serverWrk;
    }

    /**
     * This method is executed by a new Thread. This new Thread will create two
     * streams using a socket to communicate with a distant machine. Once
     * created, it will notify any thread waiting on
     * "objToWaitToIndicateClientIsRunning" (this is to tell others that the
     * client is now ready to communicate). Then, while the client is connected,
     * this thread will listen to received packet and asks the serverWrk to add
     * them to the Singleton ActionsList. Once the connection is over (received
     * a null message), the streams a deleted.
     */
    @Override
    public void run() {
        running = true;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            synchronized (objToWaitToIndicateClientIsRunning) {
                objToWaitToIndicateClientIsRunning.notifyAll();
            }
            ipAddress = socket.getInetAddress();
            while (running) {
                String msg = in.readLine();
                if (msg != null) {
                    refServerWrk.addTCPMsgToList(msg, this);
                } else {
                    running = false;
                }
                sleeep(CLIENT_SLEEP_DURATION_AFTER_READLINE);
            }
            out.close();
            out = null;
            in.close();
            in = null;
        } catch (IOException ex) {

        }
        try {
            socket.close();
        } catch (IOException ex) {

        } finally {
            refServerWrk.disconnectClient(this);
        }

    }

    /**
     * Writes a message to the distant machine using it's socket. If the message
     * is null or empty, nothing is sent.
     *
     * @param msg A String corresponding to the message to send.
     */
    public synchronized void write(String msg) {
        if ((msg != null) && (!msg.isEmpty()) && (out != null)) {
            out.println(msg);
            out.flush();
        }
    }

    /**
     * Calculate the current session time using the startTime attribut. This
     * attribut is set to the current time in ms when a client logs in.
     *
     * @return A String representing the current session time if a client is
     * logged in. Null if not.
     */
    public String getCurrentSessionTime() {
        if ((socket.isClosed()) || (startTime == 0)) {
            return "";
        }
        int diff = (int) ((System.currentTimeMillis() - startTime) / 1000);
        int min = 0;
        int sec = diff;
        if (diff >= 60) {
            min = (int) (diff / 60);
            sec = (int) (diff - 60 * min);
        }
        String minS = (min < 10) ? ("0" + min) : ("" + min);
        String secS = (sec < 10) ? ("0" + sec) : ("" + sec);
        return (minS + ":" + secS);
    }

    /**
     * Sleeps the current thread for a amount of time.
     *
     * @param ms An int corresponding to the amount of time to sleep
     */
    public void sleeep(long ms) {
        try {
            sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Socket getSocket() {
        return socket;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

}
