package app.consts;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author dingl01
 */
public interface Constants {

    static final String ASSETS_FOLDER = "assets";
    static final String ROBOT_SAVE_FILE = "robot.save";

    // TCP/UDP
    static final int TCP_PORT = 2121;
    static final int TCP_TIMEOUT = 1000;
    static final int UDP_PORT = 2122;
    static final long ROBOT_IMAGE_SEND_DELAY = 100;
    static final int NBR_ACTION_LIST_THREADS = 5;
    static final int CLIENT_SLEEP_DURATION_AFTER_READLINE = 1;

    // Database
    static final String DATABASE_PU = "PU_MYSQL_LOCAL";

    // Ihm
    static final Color IHM_BACKGROUND_COLOR = new Color(60, 63, 65);
    static final int NODE_PANEL_SLEEP_TIME = 10;
    static final int NODE_PANEL_ANIMATION_SPEED = 5;
    static final Font UNREADABLE_SEQUENCE_FONT = new Font("Arial", Font.BOLD, 12);
    static final Font READABLE_SEQUENCE_FONT = new Font("Arial", Font.BOLD, 20);

    // Robot
//     static final String ROBOT_WIFI_SSID = "APPBOT-0A600D";
//     static final String ROBOT_WIFI_PASSWORD = "link2014";
    static final String HOME_WIFI_SSID = "Lionel Ding";
    static final String HOME_WIFI_PASSWORD = "22222222";
    static final String ROBOT_IP_ADDRESS = "172.20.10.4";
    static final int ROBOT_VIEW_SIZE = 200;
    static final short ROBOT_MAX_TRACK_SPEED = 999;
    static final int ROBOT_RECONNECT_THREAD_SLEEPING_TIME = 20;

}
