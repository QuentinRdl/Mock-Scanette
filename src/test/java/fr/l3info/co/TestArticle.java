package fr.l3info.co;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test file for the Article class.
 */
public class TestArticle {

    Article a;
    Article isNotValid;
    Article isNotValid2;
    Article isNotValid3;
    Article isNotValid4;
    Article eanKey0;
    Article eanWithLeadingZeros;
    @Before     // indicates that the method should be executed before each test
    public void setup() {
        // creation of an object to test
        a = createArticle(9789059605831l, 1.3, "Cahier 72 pages");
        isNotValid = createArticle(0, 0.0, "");
        isNotValid2 = createArticle(9789059605839l, 1.3, "Cahier 72 pages");
        isNotValid3 = createArticle(9789059605839l, 1.2, "Cahier 72 pages");
        isNotValid4 = createArticle(9789059605839l, 1.3, "Pas pareil");
        eanKey0 = createArticle(9799059605839l, 12, "Fruits");
        eanWithLeadingZeros = createArticle(0000001234567l, 10.0, "Article avec zéros en début");
    }

    public Article createArticle(long ean, double price, String name) {
        return new Article(ean, price, name);
    }

    @Test   // indicates that this method is a test case
    public void shouldReturnCorrectName() {
        // Checks if the object is not null
        Assert.assertNotNull(a);
        // Checks if the name is returned correctly
        Assert.assertEquals("Cahier 72 pages", a.getNom());
    }

    @Test
    public void testGetPriceValid() {
        Assert.assertNotNull(a);
        Assert.assertFalse(a.getPrixUnitaire() <= 0);
        Assert.assertEquals(1.3, a.getPrixUnitaire(), 0);
    }

    @Test
    public void testGetEan13(){
        Assert.assertNotNull(a);
        Assert.assertEquals(9789059605831l, a.getEAN13());
        Assert.assertNotEquals(12345, isNotValid.getEAN13());
    }

    @Test // Tests the price
    public void testPrice() {
        Assert.assertNotNull(a);
        Assert.assertTrue(a.getPrixUnitaire() != 0.0);
        Assert.assertEquals(1.3, a.getPrixUnitaire(), 0);
    }

    @Test // Tests the value of the EAN13 value
    public void testEAN13Valid0() {
        Assert.assertNotNull(a);
        Assert.assertTrue(a.isValidEAN13());
    }

    @Test
    public void testEAN13Valid1() {
        // Case where ean == 0
        Assert.assertNotNull(isNotValid);
        Assert.assertTrue(isNotValid.isValidEAN13());
    }

    @Test
    public void testEAN13Valid2() {
        // Case where the ean number starts with a 0 but is valid
        Article ar = createArticle(123456784l, 10.0, "Article avec zéros en début");
        Assert.assertNotNull(ar);
        Assert.assertTrue(ar.isValidEAN13());
    }

    @Test
    public void testEAN13NotValid0() {
        // We test the case where we give a ean13 > 13
        Article ar = createArticle(154548787556546898l, 5.0, "foufou");
        Assert.assertFalse(ar.isValidEAN13());
    }

    @Test
    public void testEAN13NotValid1() {
        Assert.assertNotNull(isNotValid2);
        Assert.assertFalse(isNotValid2.isValidEAN13());
        Assert.assertFalse(eanKey0.isValidEAN13());
    }
    @Test
    public void testEquals() {
        Assert.assertNotNull(a);
        Assert.assertNotNull(isNotValid);
        Assert.assertNotNull(isNotValid2);
        Article articleToTest = new Article(9789059605831l, 1.3, "Cahier 72 pages");
        // Create a null object
        Object nullObject = null;
        Object notArticle = new Object();
        Assert.assertFalse(a.equals(notArticle));
        Assert.assertEquals(articleToTest, a);
        Assert.assertNotEquals(a, isNotValid);
        Assert.assertFalse(isNotValid2.equals(isNotValid3));
        Assert.assertFalse(isNotValid2.equals(isNotValid4));
        Assert.assertTrue(a.equals(a));
        Assert.assertFalse(a.equals(nullObject));
    }


    @Test
    public void testHashCode() {
        Assert.assertNotNull(a);
        Article articleToTest = new Article(9789059605831l, 1.3, "Cahier 72 pages");
        Assert.assertEquals(articleToTest.hashCode(), a.hashCode());
        Assert.assertNotEquals(a.hashCode(), isNotValid.hashCode());
    }
}
