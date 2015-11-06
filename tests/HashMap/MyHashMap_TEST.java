package HashMap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyHashMap_TEST{

    private MyHashMap hashMap;

    @Before
    public void setUp(){
        hashMap = new MyHashMap<>();
    }

    @Test
    public void put__addSingleEntry__ExpectPackers(){
        String expected = "Packers";
        String result = (String) hashMap.put(1, "Packers");
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void put__addEntryKeyIsNULL__ExpectException(){
        hashMap.put(null, "NFL");
    }

    @Test
    public void put__add3IdenticalEntries__ExpectSize1(){
        int expected = 1;
        hashMap.put(1, "Packers");
        hashMap.put(1, "Packers");
        hashMap.put(1, "Packers");
        int result = hashMap.size();
        assertEquals(expected, result);
    }

    @Test
    public void isEmpty__emptyMap__ExpectTRUE(){
        boolean result = hashMap.isEmpty();
        assertTrue(result);
    }

    @Test
    public void isEmpty__addSingleEntry__ExpectFALSE(){
        hashMap.put(1, "Packers");
        boolean result = hashMap.isEmpty();
        assertFalse(result);
    }

    @Test
    public void size__add2Entries__Expect2(){
        int expected = 2;
        hashMap.put(1, "Packers");
        hashMap.put(2, "Bears");
        int result = hashMap.size();
        assertEquals(expected, result);
    }

    @Test
    public void containsKey__addSingleEntry__ExpectTRUE(){
        hashMap.put(1, "Packers");
        boolean result = hashMap.containsKey(1);
        assertTrue(result);
    }

    @Test
    public void get__2EntriesDifferentKey__ExpectStringPackers(){
        String expected = "Packers";
        hashMap.put(1, "Packers");
        hashMap.put(2, "Bears");
        String result = hashMap.get(1).toString();
        assertEquals(expected, result);
    }

    @Test
    public void get__2EntriesSameKey__ExpectStringPackers(){
        String expected = "Packers";
        hashMap.put("Team", "Packers");
        hashMap.put("Team", "Bears");
        String result = hashMap.get("Team").toString();
        assertEquals(expected, result);
    }

    @Test
    public void getAll__3Entries2WithSameKey__ExpectSetSize2(){
        int expected = 2;
        hashMap.put("Team", "Packers");
        hashMap.put("Team", "Bears");
        hashMap.put("NotATeam", "Thunder");
        java.util.Set<String> resultSet = hashMap.getAll("Team");
        int result = resultSet.size();
        assertEquals(expected, result);
    }

    @Test
    public void getAll__3EntriesSameKey__ExpectPackersChargersBearsBills(){
        String expected = "[Packers, Chargers, Bears, Bills]";
        hashMap.put("Team", "Packers");
        hashMap.put("Team", "Chargers");
        hashMap.put("Team", "Bears");
        hashMap.put("Team", "Bills");
        String result = hashMap.getAll("Team").toString();
        assertEquals(expected, result);
    }

    @Test
    public void remove__3EntriesRemove1__ExpectSize2(){
        int expected = 2;
        hashMap.put(1, "Packers");
        hashMap.put(2, "Bears");
        hashMap.put(3, "Chargers");
        hashMap.remove(2);
        int result = hashMap.size();
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void remove__2elementsRemoveNonExisting__ExpectExpection(){
        hashMap.put("Team", "Packers");
        hashMap.put("Team", "Bears");
        hashMap.remove("NFL");
    }

    @Test
    public void getExistingKeys__3EntriesDifferentKeys__ExpectAFCEastAFCNorthNFCNorth(){
        String expected = "[AFCEast, AFCNorth, NFCNorth]";
        hashMap.put("AFCEast", "Bills");
        hashMap.put("AFCNorth", "Ravens");
        hashMap.put("NFCNorth", "Packers");
        String result = hashMap.getExistingKeys().toString();
        assertEquals(expected, result);
    }

    @Test
    public void getExistingValues__3EntriesDifferentKeys__ExpectBillsRavensPackers(){
        String expected = "[Packers, Ravens, Bills]";
        hashMap.put("NFCNorth", "Packers");
        hashMap.put("AFCNorth", "Ravens");
        hashMap.put("AFCEast", "Bills");
        String result = hashMap.getExistingValues().toString();
        assertEquals(expected, result);
    }

    @Test
    public void getExistingEntries__3EntriesDifferentKeys__ExpectString(){
        hashMap.put("NFCNorth", "Packers");
        hashMap.put("AFCNorth", "Ravens");
        hashMap.put("AFCEast", "Bills");
        String resultString = hashMap.getExistingEntries().toString();
        boolean firstContains = resultString.contains("[AFCEast, [Bills]]");
        boolean secondContains = resultString.contains("[AFCNorth, [Ravens]]");
        boolean thirdContains = resultString.contains("[NFCNorth, [Packers]]");
        boolean result = (firstContains && secondContains && thirdContains);
        assertTrue(result);
    }

    @Test
    public void clear__3EntriesDifferentKeys__ExpectSize0(){
        int expected = 0;
        hashMap.put("NFCNorth", "Packers");
        hashMap.put("AFCNorth", "Ravens");
        hashMap.put("AFCEast", "Bills");
        hashMap.clear();
        int result = hashMap.size();
        assertEquals(expected, result);
    }
}