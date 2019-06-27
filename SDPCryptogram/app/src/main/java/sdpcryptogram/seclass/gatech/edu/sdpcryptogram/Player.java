package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import java.util.ArrayList;
import java.util.List;

public class Player {
    // region Member Variables
    private String _emailAddress;
    private String _firstName;
    private String _lastName;
    private String _password;
    private List<PlayedCryptogram> _playedCryptograms;
    private String _username;
    // endregion Member Variables

    // region Properties
    public String getPassword() { return _password; }
    public String getUsername() { return _username; }
    // endregion Properties

    // region Constructors
    public Player(String firstName, String lastName, String email, String password, String username){
        _emailAddress = email;
        _firstName = firstName;
        _lastName = lastName;
        _password = password;
        _playedCryptograms = new ArrayList<>();
        _username = username;
    }
    // endregion Constructors

    // region Public Methods
    /**
     * Adds a cryptogram to the player's played cryptogram list
     * @param playedCryptogram Played cryptogram instance
     */
    public void addPlayedCryptogram(PlayedCryptogram playedCryptogram) {
        _playedCryptograms.add(playedCryptogram);
    }

    /**
     * Flattens the played cryptograms list into unique PlayedCryptogram entities - rather than have
     * 5 instances of one cryptogram's PlayedCryptogram, use this method to combine them and carry the
     * important data over.
     * @return List of flattened PlayedCryptogram objects
     */
    public List<PlayedCryptogram> getCombinedPlayedCryptograms() {
        List<PlayedCryptogram> _combinedPlayedCryptograms = new ArrayList<>();

        for (PlayedCryptogram cryptogram : _playedCryptograms) {
            boolean found = false;
            for (PlayedCryptogram combinedCryptogram : _combinedPlayedCryptograms) {
                if (combinedCryptogram.getName().equals(cryptogram.getName())) {
                    if (combinedCryptogram.getDateCompleted().before(cryptogram.getDateCompleted())) {
                        combinedCryptogram.setDateCompleted(cryptogram.getDateCompleted());
                    }

                    if (!combinedCryptogram.getSolved() && cryptogram.getSolved()) {
                        combinedCryptogram.setSolved(true);
                    }
                    found = true;
                }
            }

            if (!found) { _combinedPlayedCryptograms.add(cryptogram); }
        }


        for (PlayedCryptogram flattenedCryptogram : _combinedPlayedCryptograms) {
            flattenedCryptogram.setNumberOfAttempt(calculatePlayAttempts(flattenedCryptogram));
        }

        return _combinedPlayedCryptograms;
    }

    /**
     * Returns the flattened cryptogram
     * @param cryptogram Cryptogram
     * @return Flattened PlayedCryptogram
     */
    public PlayedCryptogram getCombinedPlayedCryptogram(Cryptogram cryptogram) {
        List<PlayedCryptogram> playedCryptograms = getCombinedPlayedCryptograms();

        for (PlayedCryptogram playedCryptogram : playedCryptograms) {
            if (playedCryptogram.getName().equals(cryptogram.getName())) {
                return playedCryptogram;
            }
        }

        return null;
    }
    /**
     * Determines if the player has any plays remaining on the specified cryptogram
     * @param cryptogram Cryptogram
     * @return True if the cryptogram is not solved and total number of plays < max attempts, otherwise false
     */
    public boolean hasPlaysRemaining(Cryptogram cryptogram) {
        List<PlayedCryptogram> playedCryptograms = getCombinedPlayedCryptograms();

        for (PlayedCryptogram playedCryptogram : playedCryptograms) {
            if (playedCryptogram.getName().equals(cryptogram.getName())) {

                return !playedCryptogram.getSolved() && playedCryptogram.getNumberOfAttempt() + 1 < playedCryptogram.getMaxAttempts();
            }
        }

        return true;
    }

    /**
     * Retrieves a specific cryptogram
     * @param cryptogramName Cryptogram name
     * @deprecated Not sure if this is being used?
     */
    @Deprecated
    public void getCryptogram(String cryptogramName) {
        CryptogramLibrary.getCryptogram(cryptogramName);
    }

    /**
     * Search the player's played cryptograms for a specific instance
     * @param cryptogram Cryptogram to check
     * @return PlayedCryptogram if the player has played the specified cryptogram, otherwise null
     */
    public PlayedCryptogram getPlayedCryptogram(Cryptogram cryptogram) {
        if (_playedCryptograms == null) { return null; }
        for (PlayedCryptogram cryptogramPlayed : _playedCryptograms) {
            if (cryptogramPlayed.getName().equalsIgnoreCase(cryptogram.getName())) {
                return cryptogramPlayed;
            }
        }

        return null;
    }

    /**
     * Checks the player's played cryptograms for a specific cryptogram
     * @param cryptogram Cryptogram to check
     * @return True if the player has played it, otherwise false
     */
    public boolean hasPlayedCryptogram(Cryptogram cryptogram) {
        return getPlayedCryptogram(cryptogram) != null;
    }

    public void viewStatistics(){ }
    // endregion Public Methods

    // region Private Methods
    /**
     * Calculates the number of plays the player has attempted on a single Cryptogram
     * @param cryptogramToCountPlays
     * @return Number of plays (starting from zero) that the player has attempted
     */
    private int calculatePlayAttempts(PlayedCryptogram cryptogramToCountPlays) {
        int count = 0;
        for (PlayedCryptogram playedCryptogram : _playedCryptograms) {
            if (playedCryptogram.getName().equals(cryptogramToCountPlays.getName())) {
                count++;
            }
        }

        return count - 1;
    }
    // endregion Private Methods
}