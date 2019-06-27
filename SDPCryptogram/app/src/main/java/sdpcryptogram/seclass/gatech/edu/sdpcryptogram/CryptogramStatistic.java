package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import sdpcryptogram.seclass.gatech.edu.sdpcryptogram.wrappers.DateWrapper;

public class CryptogramStatistic {
    // region Member Variables
    private String _attemptedSolution;
    private int _attemptNumber;
    private DateWrapper _dateOfAttempt;
    private boolean _solved;

    // endregion Member Variables

    // region Properties
    public boolean isSolved() { return _solved; }
    // endregion Properties
}
