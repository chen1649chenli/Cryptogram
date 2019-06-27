package sdpcryptogram.seclass.gatech.edu.sdpcryptogram.wrappers;

import java.util.Date;

public class DateWrapper {
    // region Member Variables
    protected Date _date;
    // endregion Member Variables

    // region Properties
    public int getDay() { return _date.getDate(); }
    public int getMonth() { return _date.getMonth() + 1; }
    public int getYear() { return _date.getYear() + 1900; }

    public String toString() { return getMonth() + "/" + getDay() + "/" + getYear(); }
    // endregion Properties

    // region Constructors
    public DateWrapper() { _date = new Date(); }
    // endregion Constructors

    // region Public Methods

    /**
     * Determines if the current date is before the specified date
     * @param dateToCheck Date to check against
     * @return True if the current date is before the specified date, otherwise false
     */
    public boolean before(DateWrapper dateToCheck) {
        return _date.before(dateToCheck._date);
    }
    // endregion Public Methods
}
