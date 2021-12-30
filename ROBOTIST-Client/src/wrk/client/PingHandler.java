package wrk.client;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PingHandler extends Thread{
    private volatile boolean running;
    private volatile boolean doNotSend;
    public static final int PING_RATE = 1000;
    private TCPClient client;

    public PingHandler(TCPClient client) {
        setName("Ping Handler Thread");
        this.client = client;
        running = true;
        doNotSend = true;
        
    }
    
    
    

    /**
     * Sends a PING packet every PING_RATE milliseconds.
     */
    @Override
    public void run() {
        while(running){
            while(doNotSend){
                sleeep();
            }
            client.writeMessage(PacketTypes.PING+""+System.currentTimeMillis());
            doNotSend = true;
        }
    }
    
    private void sleeep(){
        try {
            sleep(PING_RATE);
        } catch (InterruptedException ex) {
            Logger.getLogger(PingHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isDoNotSend() {
        return doNotSend;
    }

    public void setDoNotSend(boolean doNotSend) {
        this.doNotSend = doNotSend;
    }
    
    
    
    
    
}
