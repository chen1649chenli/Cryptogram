package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import java.util.List;

import sdpcryptogram.seclass.gatech.edu.sdpcryptogram.wrappers.DateWrapper;

public class PlayedCryptogram extends Cryptogram {
    // region Member Variables
    private List<CryptogramStatistic> _cryptogramStatistics;
    private DateWrapper _dateCompleted;
    private int _numberOfAttempt;
    private boolean _solved;
    // endregion Member Variables

    // region Properties
    public DateWrapper getDateCompleted() { return _dateCompleted; }
    public void setDateCompleted(DateWrapper date) { _dateCompleted = date; }
    public int getNumberOfAttempt() { return _numberOfAttempt; }
    public void setNumberOfAttempt(int number) { _numberOfAttempt = number; }
    public boolean getSolved() { return _solved; }
    public void setSolved(boolean solved) { _solved = solved; }
    // endregion Properties

    // region Constructors
    public PlayedCryptogram(Cryptogram cryptogram, int attemptNumber) {
        super(cryptogram);
        _solved = false;
        _dateCompleted = new DateWrapper();
        _numberOfAttempt = attemptNumber;
    }
    // endregion Constructors

    // region Public Methods
    /**
     * Attempts a guess with a potential solution
     * @param guess Potential solution
     * @return True if the guess was correct, otherwise false
     */
    public boolean solve(String guess) {
        _dateCompleted = new DateWrapper();

        String encodedGuess = _cipher.encode(guess);

        _solved = encodedGuess.equals(_encodedPhrase);

        if (_solved) {
            CryptogramLibrary.getCryptogram(this.getName()).addSolver(AccountManager.getActivePlayer());
        }
        return _solved;
    }
    // endregion Public Methods
}
