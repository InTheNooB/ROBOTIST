package app.wrk.server;

import app.consts.PacketTypes;
import static app.consts.Constants.TCP_PORT;
import static app.consts.Constants.TCP_TIMEOUT;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class TCPServer extends Thread {

    private volatile ServerSocket serverSocket;
    private volatile Client client;
    private volatile boolean running;

    private final ItfServerWrkTCPServer refServerWrk;

    public TCPServer(ServerWrk refServerWrk) {
        super("TCP Server");
        this.refServerWrk = refServerWrk;
    }

    /**
     * Creates a new <b>ServerSocket</b> and starts a new Thread in this class.
     *
     * @throws IOException Throws a IOException if something went wrong.
     */
    public void startServer() throws IOException {
        serverSocket = new ServerSocket(TCP_PORT);
        serverSocket.setSoTimeout(TCP_TIMEOUT);
        start();
    }

    /**
     * Stops the <b>ServerSocket</b> and stops the running thread in this class.
     *
     * @throws IOException Throws a IOException if something went wrong.
     */
    public void stopServer() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
        running = false;
    }

    /**
     * This method is executed by a new Thread. This thread will listen to new
     * client. When a new client tries to connect, if he's the first, then
     * accept him, otherwise send him a refusal message and keeps on listening.
     * When the thread has to stop, it disconnects the current client if there
     * is one.
     */
    @Override
    public void run() {
        running = true;

        if (serverSocket == null) {
            running = false;
        }
        while (running) {
            try {
                synchronized (serverSocket) {
                    Socket socketClient = serverSocket.accept();
                    Object objToWaitToIndicateClientIsRunning = new Object();
                    Client c = new Client(objToWaitToIndicateClientIsRunning, socketClient.getInetAddress().getHostAddress(), socketClient, (ServerWrk) refServerWrk);
                    if ((client == null) || (client.getSocket().isClosed())) {
                        // First client
                        client = c;
                        client.setName("Client Thread");
                        client.start();
                        appendTextConsole("#w[SERVER] "
                                + "#gClient connected ("
                                + socketClient.getInetAddress().getHostAddress()
                                + ")");
                    } else {
                        refuseClient(c);
                    }
                    sleeep(10);
                }
            } catch (SocketTimeoutException ex) {
                // rien car timeout
            } catch (IOException exc) {
                running = false;
            }
        }
        if (client != null) {
            try {
                client.setRunning(false);
                client.getSocket().close();
                client.join();
                client = null;
            } catch (IOException | InterruptedException ex) {
            }
        }
        appendTextConsole("#w[SERVER] #oServer Closed");
    }

    /**
     * Asks the <b>ServerWrk</b> to append to text to the console.
     *
     * @param s The text to append.
     */
    private void appendTextConsole(String s) {
        refServerWrk.appendTextConsole(s);
    }

    /**
     * Refuses a client by creating a new one, wait for it to be ready to
     * communicate with, send him a refusal message and disconnect him.
     *
     * @param c The client to refuse.
     * @throws IOException Throws an IOException if something went wrong.
     */
    private void refuseClient(Client c) throws IOException {
        appendTextConsole("#w[SERVER] #mAn other client tried to connect");
        // Client already connected, send him infos and disconnect him
        c.start();

        // Wait until the client is ready to communicate
        try {
            synchronized (this) {
                this.wait();
            }
        } catch (InterruptedException ex) {
        }

        c.write(PacketTypes.SERVER_FULL.name());

        // Kill the thread safely
        c.setRunning(false);
        c.getSocket().close();
        try {
            c.join();
        } catch (InterruptedException ex) {
        }
    }

    /**
     * Writes a message to the connected client if there is one.
     *
     * @param msg The message to write.
     */
    public void writeClient(String msg) {
        if ((client != null) && (client.getSocket().isConnected())) {
            client.write(msg);
        }
    }

    /**
     * Sleeps the current thread for a amount of time.
     *
     * @param ms The amount of time to sleep
     */
    public void sleeep(long ms) {
        try {
            sleep(ms);
        } catch (InterruptedException ex) {
        }
    }

    public Client getClient() {
        return client;
    }

}
