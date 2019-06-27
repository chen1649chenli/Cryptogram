package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by li chen on 7/3/18.
 */
public class AccountManager {
    // region Member Variables
    private static final String PLAYER_ACCOUNT_FILE = "players.sav";

    private static Player _activePlayer;
    private static ArrayList<Player> _players;
    // endregion Member Variables

    // region Properties
    public static Player getActivePlayer() { return _activePlayer; }
    public static void setActivePlayer(Player player) { _activePlayer = player; }

    public static int getNumberOfAccounts() {
        return getPlayerList().size();
    }

    private static List<Player> getPlayerList() {
        if (_players == null) { _players = new ArrayList<>(); }
        return _players;
    }
    // endregion Properties

    // region Public Static Methods
    /**
     * Adds player to the internal player storage object
     * @param player Player to add
     */
    public static void addPlayer(Player player){
        getPlayerList().add(player);
    }

    /**
     * Gets player by username
     * @param username Username
     * @return Player instance from the players list
     */
    public static Player getPlayerByUsername(String username){
        for (Player player : getPlayerList()) {
            if (player.getUsername().equalsIgnoreCase(username)){
                return player;
            }
        }
        return null;
    }

    /**
     * Checks to see if the specified username is available (case-insensitive)
     * @param username Username to check
     * @return True if no player has the username, otherwise false
     */
    public static boolean isUserNameAvailable(String username){
        for (Player player : getPlayerList()){
            if (player.getUsername().equalsIgnoreCase(username)){
                return false;
            }
        }
        return true;
    }

    /**
     * Loads the players from the static file
     * @param context Application context
     */
    public static void loadPlayers(Context context) {
        try {
            FileInputStream fis = context.openFileInput(PLAYER_ACCOUNT_FILE);
            InputStreamReader isr = new InputStreamReader(fis);
            Type listType = new TypeToken<ArrayList<Player>>() {}.getType();
            _players = new Gson().fromJson(isr, listType);
            fis.close();
        } catch (Exception e) {
            _players = new ArrayList<>();
        }
    }

    /**
     * Writes the players to the static file
     * @param context Application context
     */
    public static void savePlayers(Context context){
        try {
            FileOutputStream fos = context.openFileOutput(PLAYER_ACCOUNT_FILE, Context.MODE_PRIVATE);//, Context.MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(getPlayerList(), osw);
            osw.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // endregion Public Static Methods
}