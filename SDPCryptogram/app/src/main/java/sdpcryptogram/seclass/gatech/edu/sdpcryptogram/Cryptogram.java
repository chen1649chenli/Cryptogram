package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import java.util.ArrayList;
import java.util.List;

import sdpcryptogram.seclass.gatech.edu.sdpcryptogram.cipher.Cipher;
import sdpcryptogram.seclass.gatech.edu.sdpcryptogram.wrappers.DateWrapper;

public class Cryptogram {
    // region Member Variables
    Cipher _cipher;
    String _encodedPhrase;
    String _solution;

    private DateWrapper _dateCreated;
    private int _maxAttempts;
    private String _name;
    private int _numberOfSolves;
    private List<Player> _playersSolved = new ArrayList<>();
    // endregion Member Variables

    // region Properties
    public void addSolver(Player player) {
        _playersSolved.add(player);
        _numberOfSolves++;
    }
    public DateWrapper getDateCreated() { return _dateCreated; }
    public String getEncodedPhrase() { return _encodedPhrase; }
    public int getMaxAttempts() { return _maxAttempts; }
    public int getNumberOfSolves() { return _numberOfSolves; }
    public String getName() { return _name; }
    // endregion Properties

    // region Constructors
    protected Cryptogram(Cryptogram cryptogram) {
        _cipher = cryptogram._cipher;
        _dateCreated = cryptogram._dateCreated;
        _name = cryptogram._name;
        _maxAttempts = cryptogram._maxAttempts;
        _numberOfSolves = cryptogram._numberOfSolves;
        _solution = cryptogram._solution;
        _encodedPhrase = cryptogram._encodedPhrase;
    }
    public Cryptogram(String name, int maxAttempts, Cipher cipher, String solution) {
        _cipher = cipher;
        _dateCreated = new DateWrapper();
        _name = name;
        _maxAttempts = maxAttempts;
        _numberOfSolves = 0;
        _solution = solution;

        _encodedPhrase = _cipher.encode(_solution);
    }
    // endregion Constructors

    // region Public Methods
    /**
     * Retrieves the top number of solvers for the cryptogram
     * @param numTopSolvers Number of solvers to list
     * @return List of players that solved the cryptogram, up to the provided number
     */
    public List<Player> getFirstSolvers(int numTopSolvers) {
        List<Player> topSolvers = new ArrayList<Player>();
        if (numTopSolvers > _numberOfSolves){
            numTopSolvers = _numberOfSolves;
        }
        for (int i = 0; i < numTopSolvers; i++) {
            topSolvers.add(_playersSolved.get(i));
        }

        return topSolvers;
    }

    /**
     * Begin the play attempt on a cryptogram
     * @return Returns a PlayedCryptogram object
     */
    public PlayedCryptogram play() {
        Player activePlayer = AccountManager.getActivePlayer();

        int numberOfAttempt =
                activePlayer.getCombinedPlayedCryptogram(this) == null ?
                0
                : activePlayer.getCombinedPlayedCryptogram(this).getNumberOfAttempt() + 1;

        return new PlayedCryptogram(this, numberOfAttempt);
    }
    // endregion Public Methods
}