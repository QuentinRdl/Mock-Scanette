package fr.l3info.co;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;

/**
 * Test file for the ArticleDB class.
 */
public class TestArticleDB {

    ArticleDB produits = new ArticleDB();
    ArticleDB voidProduits = new ArticleDB();
    ArticleDB db;


    // Path to the csv files

    String pathProduits = "target/classes/csv/produits.csv";
    String pathVoidProduits ="target/classes/csv/void.csv";
    @Before
    public void setup() throws FileFormatException {
        produits.init(new File(pathProduits));
        voidProduits.init(new File(pathVoidProduits));
        db = new ArticleDB();
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

    /* =================== Test Théo Delaroche =================*/

    @Test
    public void testAffichage_DLR() throws FileFormatException {
        ArticleDB db = new ArticleDB();
        db.init(new File("target/classes/csv/shortTest.csv"));
        Assert.assertEquals("5410188006711,2.15,Tropicana Tonic Breakfast\n" +
                "3560070048786,0.87,Cookies choco\n", db.toString());
    }

    @Test
    public void testPresenceArticleTrue_DLR() throws FileFormatException {
        ArticleDB db = new ArticleDB();
        db.init(new File("target/classes/csv/produits_no_errors.csv"));
        Article article = db.getArticle(3560070976478L);
        Article article1 = new Article(3560070976478L,1.94,"Poulet satay et son riz");
        Assert.assertEquals(article, article1);
    }

    @Test
    public void testPresenceArticleFalse_DLR() throws FileFormatException {
        ArticleDB db = new ArticleDB();
        db.init(new File("target/classes/csv/produits_no_errors.csv"));
        Article article = db.getArticle(3560070956478L);
        Assert.assertNull(article);
    }

    @Test
    public void testTailleDBShort_DLR() throws FileFormatException {
        ArticleDB db = new ArticleDB();
        db.init(new File("target/classes/csv/shortTest.csv"));
        Assert.assertEquals(2, db.getTailleDB());
    }

    @Test
    public void testTailleDBLong_DLR() throws FileFormatException {
        ArticleDB db = new ArticleDB();
        db.init(new File("target/classes/csv/produits_no_errors.csv"));
        Assert.assertEquals(16, db.getTailleDB());
    }

    @Test
    // Permet d'avoir un test sur une DB de taille variante
    public void testTailleDB_DLR() throws FileFormatException, IOException {
        String fileName = "produits_no_errors.csv";
        ArticleDB db = new ArticleDB();
        db.init(new File("target/classes/csv/" + fileName));

        FileReader reader = new FileReader("target/classes/csv/" + fileName);
        BufferedReader br = new BufferedReader(reader);
        String line;
        int count = 0;
        while ((line = br.readLine()) != null) {
            ++count;
        }

        Assert.assertEquals(count, db.getTailleDB());
    }

    @Test
    public void testTailleZero_DLR() throws FileFormatException {
        ArticleDB db = new ArticleDB();
        db.init(new File("target/classes/csv/empty.csv"));
        Assert.assertEquals(0, db.getTailleDB());
    }

    @Test(expected=FileFormatException.class)
    public void testDBErronne_DLR() throws FileFormatException {
        ArticleDB db = new ArticleDB();
        db.init(new File("target/classes/csv/produits.csv"));
        System.out.println(db);
    }

    /* =================== Test Quentin Payet =================*/


    @Test
    public void testLectureFichierExistantQuentin() throws FileFormatException {
        Assert.assertTrue(db != null);
        db.init(new File("target/classes/csv/produits.csv"));
    }

    @Test(expected=FileFormatException.class)
    public void testLectureFichierInexistantQuentin() throws FileFormatException {
        Assert.assertTrue(db != null);
        db.init(new File("target/classes/csv/fichierInexistant.csv"));
    }

    @Test(expected = FileFormatException.class)
    public void testLectureFichierAttributManquant() throws FileFormatException {
        Assert.assertTrue(db != null);
        db.init(new File("target/classes/csv/fichierErreur.csv"));
        Assert.assertEquals(0, db.getTailleDB());
    }

    @Test
    public void testLectureFichierVide() throws FileFormatException {
        Assert.assertTrue(db != null);
        db.init(new File("target/classes/csv/fichierVide.csv"));
        Assert.assertEquals(0, db.getTailleDB());
    }

    @Test(expected = FileFormatException.class)
    public void testLectureFichierAttributEnTrop() throws FileFormatException {
        Assert.assertTrue(db != null);
        db.init(new File("target/classes/csv/FichierErreurAttributEnTrop.csv"));
        Assert.assertEquals(0, db.getTailleDB());
    }

    @Test(expected = FileFormatException.class)
    public void testLectureFichierAttributEan13NonValide() throws FileFormatException {
        Assert.assertTrue(db != null);
        db.init(new File("target/classes/csv/fichierErreurAttributEan13.csv"));
        Assert.assertEquals(0, db.getTailleDB());
    }

    @Test(expected = FileFormatException.class)
    public void testLectureFichierAttributPrix() throws FileFormatException {
        Assert.assertTrue(db != null);
        db.init(new File("target/classes/csv/fichierErreurAttributPrixUnitaire.csv"));
        Assert.assertEquals(0, db.getTailleDB());
    }

    @Test(expected = FileFormatException.class)
    public void testLectureFichierAttributNom() throws FileFormatException {
        Assert.assertTrue(db != null);
        db.init(new File("target/classes/csv/fichierErreurAttributNom.csv"));
        Assert.assertEquals(0, db.getTailleDB());
    }

    @Test(expected = FileFormatException.class)
    public void testFormatFile() throws FileFormatException {
        Assert.assertTrue(db != null);
        db.init(new File("target/classes/csv/produitsTxt.txt"));
        Assert.assertEquals(0, db.getTailleDB());
    }


    @Test
    public void testGetTailleInitDB() throws FileFormatException {
        Assert.assertTrue(db != null);

        db.init(new File("target/classes/csv/produits.csv"));
        Assert.assertEquals(db.getTailleDB(), 17);
    }

    @Test
    public void testGetTailleNotInitDB() {
        Assert.assertTrue(db != null);
        Assert.assertEquals(0, db.getTailleDB());
    }

    @Test
    public void testGetExistingArticle() throws FileFormatException{
        Assert.assertTrue(db != null);
        db.init(new File("target/classes/csv/produits.csv"));

        Article a = new Article(5410188006711l, 2.15, "Tropicana Tonic Breakfast");
        Assert.assertTrue(a != null);

        Assert.assertTrue(db.getArticle(5410188006711l).equals(a));
    }

    @Test
    public void testGetNotExistingArticle() throws FileFormatException{
        Assert.assertTrue(db != null);
        db.init(new File("target/classes/csv/produits.csv"));

        Assert.assertEquals(null, db.getArticle(000l));
    }

     /* --- TESTS - GALLAND Romain --- */

    private File csv_articles_file_GLD;
    private ArticleDB db_GLD;

    /**
     * Créer un nouveau articleDB et un fichier CSV d'articles avant chaque test.
     */
    public void before_GLD() throws IOException, FileFormatException {
        csv_articles_file_GLD = TestUtils.generateCsvFile();
        Assert.assertTrue(csv_articles_file_GLD.exists());
        db_GLD = TestUtils.generateArticleDB(csv_articles_file_GLD);
    }

    public void after_GLD() throws IOException {
        Files.deleteIfExists(csv_articles_file_GLD.toPath());
    }

    /**
     * Teste l'initialisation d'une base de données d'articles à partir d'un fichier CSV.
     */
    @Test
    public void testInit_ExistingFile_GLD() throws FileFormatException, IOException {
        before_GLD();
        ArticleDB db = new ArticleDB();
        db.init(csv_articles_file_GLD);
        after_GLD();
    }

    /**
     * Teste l'initialisation d'une base de données d'articles à partir d'un fichier CSV inexistant.
     */
    @Test(expected=FileFormatException.class)
    public void testLectureFichierInexistant_GLD() throws FileFormatException, IOException {
        before_GLD();
        ArticleDB db = new ArticleDB();
        db.init(new File("fichierInexistant.csv"));
        after_GLD();
    }


    /**
     * Teste l'initialisation d'une base de données d'articles à partir d'un fichier CSV vide.
     */
    @Test
    public void testInit_EmptyFile_GLD() throws IOException, FileFormatException {
        before_GLD();
        File emptyFile = new File(TestUtils.TEMP_DIR + "empty.csv");
        emptyFile.createNewFile();
        ArticleDB db = new ArticleDB();
        db.init(emptyFile);
        Assert.assertEquals(0, db.getTailleDB());
        Files.deleteIfExists(emptyFile.toPath());
        after_GLD();
    }

    /**
     * Teste l'initialisation d'une base de données d'articles à partir d'un fichier CSV mal formatté.
     */
    @Test(expected = FileFormatException.class)
    public void testInit_MalformedFile_GLD() throws IOException, FileFormatException {
        before_GLD();
        File malformedFile = new File(TestUtils.TEMP_DIR + "malformed.csv");
        try (FileWriter fw = new FileWriter(malformedFile)) {
            fw.write("malformed,line\nanother,malformed,line");
        }
        ArticleDB db = new ArticleDB();
        db.init(malformedFile);
        Files.deleteIfExists(malformedFile.toPath());
        after_GLD();
    }

    /**
     * Teste la fonction getTailleDB de la classe ArticleDB dans un cas normal
     */
    @Test
    public void testGetTailleDB_GLD() throws FileFormatException, IOException {
        before_GLD();
        Assert.assertEquals(db_GLD.getTailleDB(), TestUtils.TEST_CSV_CONTENT.split("\n").length);
        after_GLD();
    }

    /**
     * Teste la fonction getTailleDB de la classe ArticleDB dans le cas d'une base de données vide
     */
    @Test
    public void testGetTailleDB_empty_GLD() throws FileFormatException, IOException {
        before_GLD();
        ArticleDB empty_db = new ArticleDB();
        Assert.assertEquals(0, empty_db.getTailleDB());
        after_GLD();
    }

    /**
     * Teste la fonction getArticle de la classe ArticleDB dans le cas d'un article existant
     */
    @Test
    public void testGetArticle_GLD() throws FileFormatException, IOException {
        before_GLD();
        Article getted = db_GLD.getArticle(8715700110622L);
        Assert.assertEquals("Ketchup", getted.getNom());
        Assert.assertEquals(0.96, getted.getPrixUnitaire(), 0.0);
        Assert.assertEquals(8715700110622L, getted.getEAN13());
        after_GLD();
    }

    /**
     * Teste la fonction getArticle de la classe ArticleDB dans le cas d'un article qui n'existe pas
     */
    @Test
    public void testGetArticle_null_GLD() throws FileFormatException, IOException {
        before_GLD();
        Assert.assertNull(db_GLD.getArticle(-5L));
        after_GLD();
    }

    /**
     * Teste la fonction getArticle de la classe ArticleDB dans le cas d'un ean 13 trop long
     */
    @Test
    public void testGetArticle_NonExistingEAN13_GLD() throws FileFormatException, IOException {
        before_GLD();
        Assert.assertNull(db_GLD.getArticle(9999999999999L));
        after_GLD();
    }


}


