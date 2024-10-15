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



    private Article b;
    private Article c;
    private Article d;
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

        /* =================== Test Theo Delaroche =================*/
        // creation of an object to test
        a = new Article(9789059605831L, 1.3, "Cahier 72 pages");
        // creation d'un objet qui comporte des valeurs fausses to test
        b = new Article(1789059605831L, 2.8, "Livre annexe");
        c = new Article(9789059605831L, 1.3, "Cahier 72 pages");
        d = new Article(1300000000000L, 7.5, "Presse-papier");
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

    /* =================== Test Theo Delaroche =================*/

    @Test   // indicates that this method is a test case
    public void testNameTrue() {
        // an observation that will obviously succeed
        Assert.assertNotNull(a);
        // an observation that will cause the test to fail:
        Assert.assertEquals("Cahier 72 pages", a.getNom());
    }

    @Test
    public void testNameFalse() {
        Assert.assertNotNull(b);
        Assert.assertNotEquals("Livres annexe", b.getNom());
    }

    @Test
    public void testPriceTrue() {
        Assert.assertNotNull(a);
        Assert.assertEquals(1.3, a.getPrixUnitaire(), 0);
    }

    @Test
    public void testPriceFalse() {
        Assert.assertNotNull(b);
        Assert.assertNotEquals(1, b.getPrixUnitaire());
    }

    @Test
    public void testEan13True() {
        Assert.assertNotNull(a);
        Assert.assertEquals(9789059605831L, a.getEAN13(), 0);
        Assert.assertTrue(a.isValidEAN13());
        // Test du cas ou la clé de contrôle est égale a 0
        Assert.assertNotNull(d);
        Assert.assertTrue(d.isValidEAN13());
    }

    @Test
    public void testEan13False() {
        Assert.assertNotNull(b);
        Assert.assertFalse(b.isValidEAN13());
    }

    @Test
    public void testEqualsTDelaro() {
        Assert.assertNotNull(a);
        Assert.assertNotNull(b);
        Assert.assertFalse(a.equals(b));
        Assert.assertTrue(a.equals(c));
        Assert.assertFalse(a.equals(13));
    }

    @Test
    public void testHashcode() {
        Assert.assertNotNull(a);
        Assert.assertNotNull(c);
        Assert.assertEquals(a.hashCode(), c.hashCode());

    }

}

