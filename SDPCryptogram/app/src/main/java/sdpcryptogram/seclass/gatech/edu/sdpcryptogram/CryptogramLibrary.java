package sdpcryptogram.seclass.gatech.edu.sdpcryptogram;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import sdpcryptogram.seclass.gatech.edu.sdpcryptogram.cipher.Cipher;
import sdpcryptogram.seclass.gatech.edu.sdpcryptogram.wrappers.ToastWrapper;

public class CryptogramLibrary {
    // region Member Variables
    private static final String CRYPTOGRAM_FILE = "cryptograms.sav";

    private static List<Cryptogram> _cryptograms = new ArrayList<Cryptogram>();
    // endregion Member Variables

    // region Properties
    public static List<Cryptogram> listCryptograms(Context context) {
        _cryptograms = null;

        try { loadCryptograms(context); }
        catch (Exception e) {
            e.printStackTrace();
        }

        return _cryptograms;
    }
    // endregion Properties

    // region Public Methods
    /**
     * Adds a pre-generated cryptogram to the library
     * @param cryptogram Cryptogram to add
     */
    public static void createCryptogram(Cryptogram cryptogram) {
        _cryptograms.add(cryptogram);
    }

    /**
     * Retrieves the specified cryptogram by name (case-insensitive)
     * @param cryptogramName Cryptogram name
     * @return Cryptogram object if it exists, otherwise null
     */
    public static Cryptogram getCryptogram(String cryptogramName) {
        for (Cryptogram cryptogram : _cryptograms) {
            if (cryptogram.getName().equalsIgnoreCase(cryptogramName)) { return cryptogram; }
        }

        return null;
    }

    /**
     * Instantiates a fresh cryptogram library file
     * @param context Context
     */
    public static void instantiateNewCryptogramLibrary(Context context) {
        ToastWrapper.longToast(context, context.getText(R.string.cryptogram_library_instantiate_new_library).toString());

        Cipher cipher = new Cipher();
        cipher.addCipherCharPair('E','r');
        cipher.addCipherCharPair('L','f');
        cipher.addCipherCharPair('H','N');
        cipher.addCipherCharPair('O','t');
        cipher.addCipherCharPair('W','X');
        cipher.addCipherCharPair('R','m');
        cipher.addCipherCharPair('D','a');
        createCryptogram(new Cryptogram("My First Cryptogram!", 75, cipher, "Hello World"));

        Cipher anotherCipher = new Cipher();
        anotherCipher.addCipherCharPair('C','v');
        anotherCipher.addCipherCharPair('G','x');
        anotherCipher.addCipherCharPair('A','s');
        anotherCipher.addCipherCharPair('M','w');
        anotherCipher.addCipherCharPair('R','t');
        anotherCipher.addCipherCharPair('Y','Q');
        anotherCipher.addCipherCharPair('P','F');
        anotherCipher.addCipherCharPair('O','l');
        anotherCipher.addCipherCharPair('T','p');
        createCryptogram(new Cryptogram("Another easy one", 10, anotherCipher, "cryptogram"));

        Cipher yetAnotherCipher = new Cipher();
        yetAnotherCipher.addCipherCharPair('S','j');
        yetAnotherCipher.addCipherCharPair('E','z');
        yetAnotherCipher.addCipherCharPair('R','w');
        yetAnotherCipher.addCipherCharPair('A','b');
        yetAnotherCipher.addCipherCharPair('N','C');
        yetAnotherCipher.addCipherCharPair('W','q');
        createCryptogram(new Cryptogram("Only one chance!", 1, yetAnotherCipher, "answer"));

        saveCryptograms(context);
    }

    /**
     * Loads cryptograms from the stored file
     * @param context Context
     * @throws IOException Exception if there is an issue loading or reading the file
     */
    public static void loadCryptograms(Context context) throws IOException{

        FileInputStream cryptogramFile = context.openFileInput(CRYPTOGRAM_FILE);
        InputStreamReader inputStreamReader = new InputStreamReader(cryptogramFile);
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Cryptogram>>() {}.getType();
        _cryptograms = gson.fromJson(inputStreamReader, listType);
        cryptogramFile.close();
    }

    /**
     * Determines if the specified cryptogram name is unique (case-insensitive)
     * @param cryptogramName Name of the potential cryptogram
     * @return True if no cryptogram matches the name, otherwise false
     */
    public static boolean isUnique(String cryptogramName) {
        return getCryptogram(cryptogramName) == null;
    }

    /**
     * Saves the CryptogramLibrary contents to the CRYPTOGRAM_FILE
     * @param context Application context
     */
    public static void saveCryptograms(Context context) {
        try {
            context.deleteFile(CRYPTOGRAM_FILE);
            FileOutputStream cryptogramFile = context.openFileOutput(CRYPTOGRAM_FILE, Context.MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(cryptogramFile);
            Gson gson = new Gson();
            gson.toJson(_cryptograms, outputStreamWriter);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // endregion Public Methods
}