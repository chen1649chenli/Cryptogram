package sdpcryptogram.seclass.gatech.edu.sdpcryptogram.cipher;

import org.junit.Assert;
import org.junit.Test;

import sdpcryptogram.seclass.gatech.edu.sdpcryptogram.cipher.Cipher;

public class CipherTests {
    @Test
    public void addCipherCharPair_AddValidMapping_NoExceptionThrown() {
        Cipher cipher = new Cipher();
        cipher.addCipherCharPair('a', 'b');

        Assert.assertEquals('b', cipher._cipherMapping.get('a').charValue());
    }

    @Test
    public void addCipherCharPair_AddDuplicateMapping_NewMappingIgnored() {
        Cipher cipher = new Cipher();
        cipher.addCipherCharPair('a', 'b');
        cipher.addCipherCharPair('a','c');

        Assert.assertEquals('b', cipher._cipherMapping.get('a').charValue());
        Assert.assertEquals(1, cipher._cipherMapping.size());
    }

    @Test
    public void addCipherCharPair_AddNewMappingToExistingMappedCharacter_NewMappingIgnored() {
        Cipher cipher = new Cipher();
        cipher.addCipherCharPair('a', 'b');
        cipher.addCipherCharPair('r', 'b');

        Assert.assertEquals('b', cipher._cipherMapping.get('a').charValue());
        Assert.assertNull(cipher._cipherMapping.get('r'));
    }
}