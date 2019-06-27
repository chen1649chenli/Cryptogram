package sdpcryptogram.seclass.gatech.edu.sdpcryptogram.cipher;

import java.util.HashMap;
import java.util.Map;

public class Cipher {
    // region Member Variables
    protected Map<Character, Character> _cipherMapping;
    // endregion Member Variables

    // region Constructors
    public Cipher() { _cipherMapping = new HashMap<>(); }
    // endregion Constructors

    // region Public Methods
    /**
     * Adds a char<->char mapping for the cipher.  Will not overwrite an existing pair
     * @param charOne Character one
     * @param charTwo Character two
     * @return True if the pair to be added is valid, otherwise false
     */
    public boolean addCipherCharPair(Character charOne, Character charTwo) {
        if (_cipherMapping.containsKey(charOne) || _cipherMapping.containsValue(charTwo)) { return false; }
        _cipherMapping.put(charOne, charTwo);
        return true;
    }

    /**
     * Encodes the solution using the stored cipher
     * @param solution Cryptogram solution
     * @return Encoded cryptogram
     */
    public String encode(String solution) {
        char[] characters = new char[solution.length()];
        solution.getChars(0, solution.length(), characters, 0);
        for (int i = 0; i < characters.length; i++) {
            //if (_cipherMapping.get(characters[i]) == null){ continue; }
            char ch;
            char ch2;
            int temp;
            temp = characters[i];
            int temp2 = 0;
            //if whitespace
            if (characters[i] == ' ')
                continue; //characters[i] = characters[i];
            //if digit
            if (temp >= 48 && temp <= 57)
                continue; //characters[i] = characters[i];
            //if char from solution i is uppercase
            if (temp >= 65 && temp <= 90) {
                if (!_cipherMapping.containsKey(characters[i])) { continue; }

                characters[i] = _cipherMapping.get(characters[i]);
                temp2 = characters[i];
                //if key value from map pair is lowercase then uppercase the result to match solution case
                if (temp2 >= 97 && temp2 <= 122) {
                    temp2 = temp2 - 32;
                    ch2 = (char) temp2;
                    characters[i] = ch2;
                }
            }
            //if char from solution i is lowercase
            else if (temp >= 97 && temp <= 122) {
                temp = temp - 32;
                ch = (char) temp;
                if (!_cipherMapping.containsKey(ch)) { continue; }
                characters[i] = _cipherMapping.get(ch);
                temp2 = characters[i];
                //if key value from map pair is uppercase then lowercase the result to match solution case
                if (temp2 >= 65 && temp2 <= 90) {
                    temp2 = temp2 + 32;
                    ch2 = (char) temp2;
                    characters[i] = ch2;
                }
            }
        }
        return new String(characters);
    }
    // endregion Public Methods
}