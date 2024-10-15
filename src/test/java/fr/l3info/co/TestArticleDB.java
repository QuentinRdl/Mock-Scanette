package fr.l3info.co;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Test file for the ArticleDB class.
 */
public class TestArticleDB {

    ArticleDB produits = new ArticleDB();
    ArticleDB voidProduits = new ArticleDB();

    // Path to the csv files

    String pathProduits = "target/classes/csv/produits.csv";
    String pathVoidProduits ="target/classes/csv/void.csv";
    @Before
    public void setup() throws FileFormatException {
        produits.init(new File(pathProduits));
        voidProduits.init(new File(pathVoidProduits));
    }

    public boolean fileExists(String path) {
        File f = new File(path);
        if(f.exists() && !f.isDirectory()) {
            return true; // File exists
        }
        return false; // File does not exist
    }

    @Test
    public void testLectureFichierExistant() throws FileFormatException {
        Assert.assertTrue(fileExists(pathProduits)); // Makes sure the file exists
        Assert.assertTrue(produits != null);
        ArticleDB db = new ArticleDB();
        db.init(new File("target/classes/csv/produits.csv"));
    }

    @Test(expected=FileFormatException.class)
    public void testLectureFichierInexistant() throws FileFormatException {
        ArticleDB db = new ArticleDB();
        db.init(new File("fichierInexistant.csv"));
        Assert.assertFalse(fileExists("fichierInexistant.csv"));
    }

    @Test
    public void testGetTailleBD() {
        Assert.assertTrue(fileExists(pathProduits)); // Makes sure the file exists
        Assert.assertTrue(produits != null);
        Assert.assertTrue(produits.getTailleDB() >= 0);
        Assert.assertTrue(produits.getTailleDB() == 17);
    }

    @Test
    public void testGetTailleBDempty() {
        Assert.assertTrue(fileExists(pathVoidProduits)); // Makes sure the file exists
        Assert.assertTrue(voidProduits != null);
        Assert.assertTrue(voidProduits.getTailleDB() == 0);
    }

    @Test
    public void testGetArticleValid() {
        Assert.assertTrue(fileExists(pathProduits)); // Makes sure the file exists
        Assert.assertTrue(produits != null);
        Article cookies = produits.getArticle(3560070048786l);
        Assert.assertTrue(cookies != null);
        Assert.assertTrue(cookies.getNom().equals("Cookies choco"));
        Assert.assertTrue(cookies.equals(cookies));
        Assert.assertTrue(cookies.equals(produits.getArticle(3560070048786l)));
        Assert.assertTrue(cookies.getPrixUnitaire() == 0.87);
        Assert.assertTrue(cookies.getEAN13() == 3560070048786l);
    }

    @Test
    public void testGetArticleInvalid() {
        Assert.assertTrue(fileExists(pathProduits)); // Makes sure the file exists
        Assert.assertTrue(produits != null);
        Article notFound = produits.getArticle(-2);
        Assert.assertTrue(notFound == null);
    }
}
