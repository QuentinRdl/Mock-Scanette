package fr.l3info.co;

// Mockito
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


// For the tests
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;


// JUnit
import org.junit.runner.RunWith;

// For the usage of files
import java.io.File;
import java.util.Set;

import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class ScanetteTest {
    ArticleDB articleProduits = new ArticleDB();
    ArticleDB articleVoidProduits = new ArticleDB();

    private Scanette sc;
    private ArticleDB db;
    // Path to the csv files

    String pathArticleProduits = "target/classes/csv/produits.csv";
    String pathArticleVoidProduits ="target/classes/csv/void.csv";

    ICaisse mockCaisse;

   @Before
   public void setup() throws FileFormatException {
       articleProduits.init(new File(pathArticleProduits));
       articleVoidProduits.init(new File(pathArticleVoidProduits));
       mockCaisse = Mockito.mock(ICaisse.class);
       db = new ArticleDB();
       db.init(new File("target/classes/csv/produits.csv"));
       sc = new Scanette(db);
   }

    @Test
    public void testDebloquer0() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
    }

    /*
    @Test
    public void testTransmission0() {
        // object we test
        Scanette scan = new Scanette(articleProduits);

        // instanciation of the mock
        ICaisse mockCaisse = Mockito.mock(ICaisse.class);

        // definition of the awaited behavior for the object
        Mockito.when(mockCaisse.connexion(scan)).thenReturn(1);

        assertEquals(1, scan.transmission(mockCaisse));
    }
     */

    /*
    @Test
    public void testTransmission1_EmptyArticleDB() {
       // We initiate scan with an empty ArticleDB
        Scanette scan = new Scanette(articleVoidProduits);

        ICaisse mockCaisse = Mockito.mock(ICaisse.class);

        // definition of the awaited behavior for the object
        Mockito.when(mockCaisse.connexion(scan)).thenReturn(1);

        assertEquals(-1, scan.transmission(mockCaisse));
    }
     */

    @Test
    public void testScanetteInit() {
       Scanette scan = new Scanette(articleProduits);
       assertNotNull(scan);
    }

    @Test
    public void testDeblocage() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
    }

    @Test
    public void testDeblocage1() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(-1, scan.debloquer());
        assertEquals(-1, scan.debloquer());
    }

    @Test
    public void testNbArticlesPasDedans() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.quantite(1234567890123L));
    }

    @Test
    public void testNbArticlesDedans() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        long ean = 3560070139675L;
        assertEquals(0, scan.scanner(ean));
        assertEquals(1, scan.quantite(ean));
    }

    @Test
    public void testNbArticlesDedansPlusieursFois(){
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        long ean = 3560070139675L;
        assertEquals(0, scan.scanner(ean));
        assertEquals(1, scan.quantite(ean));
        assertEquals(0, scan.scanner(ean));
        assertEquals(2, scan.quantite(ean));
        assertEquals(0, scan.scanner(ean));
        assertEquals(3, scan.quantite(ean));
        assertEquals(0, scan.scanner(ean));
        assertEquals(4, scan.quantite(ean));
        assertEquals(0, scan.scanner(ean));
        assertEquals(5, scan.quantite(ean));
    }

    @Test
    public void testScannerNotReconnu() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(-2, scan.scanner(1234567890123L));
    }

    @Test
    public void testScannerCorrect() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        long ean = 3560070139675L;
        assertEquals(0, scan.scanner(ean));
    }

    @Test
    public void testScannerMauvaisEtat() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(-1, scan.scanner(3560070139675L));
    }

    @Test
    public void testScannerEtatValide() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(0, scan.scanner(3560070139675L));
    }

    @Test
    public void testScannerRelectureDansPanier() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(0, scan.scanner(3560070139675L));

        // On utilise un mock pour simuler la caisse
        Mockito.when(mockCaisse.connexion(scan)).thenReturn(1);
        assertEquals(1, scan.transmission(mockCaisse));
        assertEquals(0, scan.scanner(3560070139675L));
    }

    @Test
    public void testScannerRelecturePasDansPanier() {
        // On attend un return de -3
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(0, scan.scanner(3560070139675L));

        // On utilise un mock pour simuler la caisse
        Mockito.when(mockCaisse.connexion(scan)).thenReturn(1);
        assertEquals(1, scan.transmission(mockCaisse));
        assertEquals(-3, scan.scanner(4560070139675L));
    }

    @Test
    public void testScannerRelectureRienARescanner() {
        // On attend un return de -3
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());

        // On utilise un mock pour simuler la caisse
        Mockito.when(mockCaisse.connexion(scan)).thenReturn(1);
        assertEquals(1, scan.transmission(mockCaisse));
        assertEquals(-1, scan.scanner(4560070139675L));
    }

    @Test
    public void testSupprimerPasEnCourses(){
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(-1, scan.supprimer(3560070139675L));
    }

    @Test
    public void testSupprimerExistePas(){
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(-2, scan.supprimer(3560070139675L));
    }

    @Test
    public void testSupprimerExiste1Article(){
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(0, scan.scanner(3560070139675L));
        assertEquals(0, scan.supprimer(3560070139675L));
    }


    @Test
    public void testSupprimerExiste2Article(){
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(0, scan.scanner(8718309259938L));
        assertEquals(0, scan.scanner(3560070139675L));
        assertEquals(0, scan.supprimer(3560070139675L));
        assertEquals(0, scan.supprimer(8718309259938L));
        assertEquals(-2, scan.supprimer(8718309259938L));
    }

    @Test
    public void testSupprimerExisteMemeArticle(){
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(0, scan.scanner(3560070139675L));
        assertEquals(0, scan.scanner(3560070139675L));
        assertEquals(0, scan.scanner(3560070139675L));
        assertEquals(0, scan.supprimer(3560070139675L));
        assertEquals(0, scan.supprimer(3560070139675L));
        assertEquals(0, scan.supprimer(3560070139675L));
        assertEquals(-2, scan.supprimer(3560070139675L));
    }

    @Test
    public void testTransmissionNull() {
        ICaisse c = null;
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(-1, scan.transmission(c));
    }

    @Test
    public void testTransmissionScanetteBloquee() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(-1, scan.transmission(mockCaisse));
    }

    @Test
    public void testTransmissionFiniTravail() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        Mockito.when(mockCaisse.connexion(scan)).thenReturn(0);
        assertEquals(0, scan.transmission(mockCaisse));
    }

    @Test
    public void testTransmissionRelectureDemandee0() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        Mockito.when(mockCaisse.connexion(scan)).thenReturn(1);
        assertEquals(1, scan.transmission(mockCaisse));
    }

    @Test
    public void testTransmissionRelectureDemandee1() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(0, scan.scanner(3560070139675L));
        assertEquals(0, scan.scanner(3560070139675L));
        assertEquals(0, scan.scanner(3560070139675L));
        Mockito.when(mockCaisse.connexion(scan)).thenReturn(1);
        assertEquals(1, scan.transmission(mockCaisse));
    }


    @Test
    public void testTransmissionRetourFauxConnexion() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(0, scan.scanner(3560070139675L));
        assertEquals(0, scan.scanner(3560070139675L));
        assertEquals(0, scan.scanner(3560070139675L));
        Mockito.when(mockCaisse.connexion(scan)).thenReturn(12);
        assertEquals(-1, scan.transmission(mockCaisse));
    }

    @Test
    public void testRelectureEffectueeFaux() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertFalse(scan.relectureEffectuee());
    }

    @Test
    public void testRelectureEffectueeVrai() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        Mockito.when(mockCaisse.connexion(scan)).thenReturn(1);
        assertEquals(1, scan.transmission(mockCaisse));
        assertTrue(scan.relectureEffectuee());
    }


    @Test
    public void testAbandon() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(0, scan.scanner(3560070139675L));
        assertEquals(0, scan.scanner(3560070139675L));
        assertEquals(0, scan.scanner(3560070139675L));
        Mockito.when(mockCaisse.connexion(scan)).thenReturn(1);
        assertEquals(1, scan.transmission(mockCaisse));
        scan.abandon();
        assertFalse(scan.relectureEffectuee());
    }

    @Test
    public void testGetReferencesInconnues0() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(0, scan.scanner(3560070139675L));
        Mockito.when(mockCaisse.connexion(scan)).thenReturn(1);
        assertEquals(1, scan.transmission(mockCaisse));
        assertEquals(0, scan.getReferencesInconnues().size());
    }

    @Test
    public void testGetReferencesInconnues1() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(-2, scan.scanner(3560070439655L));
        Mockito.when(mockCaisse.connexion(scan)).thenReturn(1);
        assertEquals(1, scan.transmission(mockCaisse));
        assertEquals(1, scan.getReferencesInconnues().size());
    }

    @Test
    public void testGetArticleNothing() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        Set<Long> theSet = scan.getReferencesInconnues();
        assertEquals(0, theSet.size());
    }

    @Test
    public void testGetArticleSomething1() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(0, scan.scanner(3560070139675L));
        Set<Article> theSet = scan.getArticles();
        assertEquals(1, theSet.size());
    }


    @Test
    public void testGetArticleSomething2() {
        Scanette scan = new Scanette(articleProduits);
        assertNotNull(scan);
        assertEquals(0, scan.debloquer());
        assertEquals(0, scan.scanner(3560070139675L));
        assertEquals(0, scan.scanner(3570590109324L));
        Set<Article> theSet = scan.getArticles();
        assertEquals(2, theSet.size());
    }


    /* =================== Test Theo Delaroche =================*/



    /* =================== Test Quentin Payet =================*/


    @Test
    public void testDebloquerScanette() {
        Assert.assertEquals(0, sc.debloquer());
        Assert.assertEquals(-1, sc.debloquer());
    }

    @Test
    public  void testScanner() {
        Assert.assertEquals(0, sc.debloquer());
        Assert.assertEquals(0, sc.scanner(5410188006711l));
    }

    @Test
    public  void testScannerMauvaisEtatQuentin() {
        Assert.assertEquals(-1, sc.scanner(5410188006711l));
    }

    @Test
    public  void testScannerEan13NonValide() {
        Assert.assertEquals(0, sc.debloquer());
        Assert.assertEquals(-2, sc.scanner(5410188006710l));

    }

    @Test
    public  void testScannerEtatRelecture2ArticleRescanner() {
        Assert.assertEquals(0, sc.debloquer());

        // instanciation du mock
        ICaisse mockCaisse = Mockito.mock(ICaisse.class);

        // définition du comportement attendu pour l'objet
        Mockito.when(mockCaisse.connexion(sc)).thenReturn(1);

        // Ajout d'article dans le panier
        Assert.assertEquals(0, sc.scanner(5410188006711l));
        Assert.assertEquals(0, sc.scanner(5410188006711l));


        Assert.assertEquals(1, sc.transmission(mockCaisse));
        Assert.assertEquals(0, sc.scanner(5410188006711l));
    }

    @Test
    public  void testScannerEtatRelecture1ArticleRescanner() {
        Assert.assertEquals(0, sc.debloquer());

        // instanciation du mock
        ICaisse mockCaisse = Mockito.mock(ICaisse.class);

        // définition du comportement attendu pour l'objet
        Mockito.when(mockCaisse.connexion(sc)).thenReturn(1);

        // Ajout d'article dans le panier
        Assert.assertEquals(0, sc.scanner(5410188006711l));

        Assert.assertEquals(1, sc.transmission(mockCaisse));
        Assert.assertEquals(0, sc.scanner(5410188006711l));
    }

    @Test
    public  void testScannerEtatRelectureVerifContainKeyTrue() {
        testScannerEtatRelecture2ArticleRescanner();
        Assert.assertEquals(0, sc.scanner(5410188006711l));
    }

    @Test
    public  void testScannerEtatRelectureArticleRescannerInexistant() {
        Assert.assertEquals(0, sc.debloquer());

        // instanciation du mock
        ICaisse mockCaisse = Mockito.mock(ICaisse.class);

        // définition du comportement attendu pour l'objet
        Mockito.when(mockCaisse.connexion(sc)).thenReturn(1);

        // Ajout d'article dans le panier
        Assert.assertEquals(0, sc.scanner(5410188006711l));
        Assert.assertEquals(0, sc.scanner(5410188006711l));


        Assert.assertEquals(1, sc.transmission(mockCaisse));
        Assert.assertEquals(-3, sc.scanner(3046920010856l));
        Assert.assertFalse(sc.relectureEffectuee());
    }

    @Test
    public  void testScannerEtatRelectureMemeArticlePlusieurFois() {
        testTransmission13Article();
        Assert.assertEquals(0, sc.scanner(3046920010856l));
        Assert.assertEquals(-3, sc.scanner(3046920010856l));

    }

    @Test
    public  void testSupprimer() {
        Assert.assertEquals(0, sc.debloquer());
        Assert.assertEquals(0, sc.scanner(5410188006711l));
        Assert.assertEquals(0, sc.supprimer(5410188006711l));
    }

    @Test
    public  void testSupprimerArticlePlusieurOccurences() {
        Assert.assertEquals(0, sc.debloquer());

        Assert.assertEquals(0, sc.scanner(5410188006711l));
        Assert.assertEquals(0, sc.scanner(5410188006711l));
        Assert.assertEquals(2, sc.quantite(5410188006711l));

        Assert.assertEquals(0, sc.supprimer(5410188006711l));
    }


    @Test
    public  void testSupprimerMauvaisEtat() {
        Assert.assertEquals(-1, sc.supprimer(5410188006711l));
    }

    @Test
    public  void testSupprimerEan13NonValide() {
        Assert.assertEquals(0, sc.debloquer());
        Assert.assertEquals(-2, sc.supprimer(5410188006710l));
    }

    @Test
    public  void testSupprimerEan13PasPresent() {
        Assert.assertEquals(0, sc.debloquer());
        Assert.assertEquals(-2, sc.supprimer(7640164630021l));
    }


    @Test
    public  void testQuantite() {
        Assert.assertEquals(0, sc.debloquer());
        Assert.assertEquals(0, sc.scanner(3560070048786l));
        Assert.assertEquals(0, sc.scanner(3560070048786l));
        Assert.assertEquals(0, sc.scanner(3560070048786l));

        Assert.assertEquals(3, sc.quantite(3560070048786l));
    }

    @Test
    public  void testNonValideQuantite() {
        Assert.assertEquals(0, sc.debloquer());
        Assert.assertEquals(0, sc.scanner(3017800238592l));
        Assert.assertEquals(0, sc.scanner(3017800238592l));

        Assert.assertNotEquals(3, sc.quantite(3017800238592l));
    }

    @Test
    public  void testAbandonQuentin() {
        Assert.assertEquals(0, sc.debloquer());
        Assert.assertEquals(0, sc.scanner(3560070048786l));
        sc.abandon();
        Assert.assertEquals(0, sc.debloquer());
        Assert.assertTrue(sc.getArticles().isEmpty());
    }

    @Test
    public  void getArticles() {
        Assert.assertEquals(0, sc.debloquer());
        Assert.assertEquals(0, sc.scanner(3560070048786l));
        Assert.assertEquals(0, sc.scanner(45496420598l));
        Assert.assertEquals(0, sc.scanner(3245412567216l));

        Set<Article> s = new HashSet<Article>();
        s.add(db.getArticle(3560070048786l));
        s.add(db.getArticle(45496420598l));
        s.add(db.getArticle(3245412567216l));

        Assert.assertEquals(s, sc.getArticles());
    }

    @Test
    public  void getArticlesEtatBloque() {
        Assert.assertTrue(sc.getArticles().isEmpty());
    }

    @Test
    public  void getReferencesInconnues() {
        Assert.assertEquals(0, sc.debloquer());

        // Code inconnues
        Assert.assertEquals(-2, sc.scanner(4463487097622l));
        Assert.assertEquals(-2, sc.scanner(2123201259812l));
        Assert.assertEquals(-2, sc.scanner(1190623242310l));

        Set<Long> s = new HashSet<Long>();
        s.add(4463487097622l);
        s.add(2123201259812l);
        s.add(1190623242310l);

        Assert.assertEquals(s, sc.getReferencesInconnues());
    }

    @Test
    public  void getReferencesInconnuesEtatBloque() {
        Assert.assertTrue(sc.getReferencesInconnues().isEmpty());
    }


    @Test
    public void testTransmission0() {
        Assert.assertEquals(0, sc.debloquer());

        // instanciation du mock
        ICaisse mockCaisse = Mockito.mock(ICaisse.class);

        // définition du comportement attendu pour l'objet
        Mockito.when(mockCaisse.connexion(sc)).thenReturn(1);
        Assert.assertEquals(1, sc.transmission(mockCaisse));
    }

    @Test
    public void testTransmissionErreurConnexion() {
        Assert.assertEquals(0, sc.debloquer());

        // instanciation du mock
        ICaisse mockCaisse = Mockito.mock(ICaisse.class);

        // définition du comportement attendu pour l'objet
        Mockito.when(mockCaisse.connexion(sc)).thenReturn(-1); // -> bloque
        Assert.assertEquals(-1, sc.transmission(mockCaisse));
    }

    @Test
    public void testTransmissionEtatBloque() {

        // instanciation du mock
        ICaisse mockCaisse = Mockito.mock(ICaisse.class);

        // définition du comportement attendu pour l'objet
        Assert.assertEquals(-1, sc.transmission(mockCaisse));
    }

    @Test
    public void testTransmissionConnexionValide() {
        Assert.assertEquals(0, sc.debloquer());

        // instanciation du mock
        ICaisse mockCaisse = Mockito.mock(ICaisse.class);

        // définition du comportement attendu pour l'objet
        Mockito.when(mockCaisse.connexion(sc)).thenReturn(0);
        Assert.assertEquals(0, sc.transmission(mockCaisse));
        Assert.assertTrue(sc.getArticles().isEmpty() && sc.getReferencesInconnues().isEmpty());
    }

    @Test
    public  void testTransmission13Article() {
        Assert.assertEquals(0, sc.debloquer());

        // instanciation du mock
        ICaisse mockCaisse = Mockito.mock(ICaisse.class);

        // définition du comportement attendu pour l'objet
        Mockito.when(mockCaisse.connexion(sc)).thenReturn(1);

        // Ajout d'article dans le panier
        Assert.assertEquals(0, sc.scanner(5410188006711l));
        Assert.assertEquals(0, sc.scanner(3560070048786l));
        Assert.assertEquals(0, sc.scanner(3017800238592l));
        Assert.assertEquals(0, sc.scanner(3560070976478l));
        Assert.assertEquals(0, sc.scanner(3046920010856l));
        Assert.assertEquals(0, sc.scanner(8715700110622l));
        Assert.assertEquals(0, sc.scanner(3570590109324l));
        Assert.assertEquals(0, sc.scanner(3520115810259l));
        Assert.assertEquals(0, sc.scanner(3270190022534l));
        Assert.assertEquals(0, sc.scanner(8718309259938l));
        Assert.assertEquals(0, sc.scanner(3560071097424l));
        Assert.assertEquals(0, sc.scanner(3017620402678l));
        Assert.assertEquals(0, sc.scanner(3245412567216l));

        Assert.assertEquals(1, sc.transmission(mockCaisse));
    }

    @Test
    public void testTransmissionEtatRelectureKO() {
        testScannerEtatRelectureArticleRescannerInexistant();

        ICaisse mockCaisse = Mockito.mock(ICaisse.class);
        Assert.assertEquals(-1, sc.transmission(mockCaisse));
    }

    @Test
    public void testTransmissionEtatRelecture() {

        Assert.assertEquals(0, sc.debloquer());
        // instanciation du mock
        ICaisse mockCaisse = Mockito.mock(ICaisse.class);

        // définition du comportement attendu pour l'objet
        Mockito.when(mockCaisse.connexion(sc)).thenReturn(1);
        Assert.assertEquals(1, sc.transmission(mockCaisse));
        Assert.assertEquals(-1, sc.transmission(mockCaisse));
    }

    @Test
    public void testTransmissionICaisseNull() {
        Assert.assertEquals(0, sc.debloquer());

        // instanciation du mock
        ICaisse mockCaisse = null;

        // définition du comportement attendu pour l'objet
        Assert.assertEquals(-1, sc.transmission(mockCaisse));
    }

    @Test
    public void testRelectureEffectuee() {
        Assert.assertEquals(0, sc.debloquer());

        // instanciation du mock
        ICaisse mockCaisse = Mockito.mock(ICaisse.class);

        // définition du comportement attendu pour l'objet
        Mockito.when(mockCaisse.connexion(sc)).thenReturn(1);
        Assert.assertEquals(1, sc.transmission(mockCaisse));
        Assert.assertTrue(sc.relectureEffectuee());
    }

    @Test
    public void testRelectureNonEffectuee() {
        Assert.assertEquals(0, sc.debloquer());

        // instanciation du mock
        ICaisse mockCaisse = Mockito.mock(ICaisse.class);

        // définition du comportement attendu pour l'objet
        Mockito.when(mockCaisse.connexion(sc)).thenReturn(0);
        Assert.assertEquals(0, sc.transmission(mockCaisse));
        Assert.assertFalse(sc.relectureEffectuee());
    }

     /* --- TESTS - GALLAND Romain --- */

    private ArticleDB articleDB_GLD;
    private File csv_file_GLD;
    private Scanette scan_GLD;
    private ICaisse mockCaisse_GLD;

    public void setup_GLD() throws IOException, FileFormatException {
        csv_file_GLD = TestUtils.generateCsvFile();  // Génère le fichier CSV temporaire
        articleDB_GLD = TestUtils.generateArticleDB(csv_file_GLD);  // Charge la base d'articles
        scan_GLD = new Scanette(articleDB_GLD);  // Initialise la scanette avec la base d'articles
        mockCaisse_GLD = Mockito.mock(ICaisse.class);  // Crée un mock de la caisse
    }

    // Teste le déblocage normal
    @Test
    public void testDebloquer_GLD() throws FileFormatException, IOException {
        setup_GLD();
        Assert.assertEquals(0, scan_GLD.debloquer());
        cleanup_GLD();
    }

    // Teste la tentative de déblocage lorsque la scanette n'est pas bloquée
    @Test
    public void testDebloquer_two_attempts_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        Assert.assertEquals(-1, scan_GLD.debloquer());
        cleanup_GLD();
    }

    // Teste la quantification correcte des articles scannés
    @Test
    public void testQuantite_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        long eanUsed = 8715700110622L;
        scan_GLD.scanner(eanUsed);
        Assert.assertEquals(1, scan_GLD.quantite(eanUsed));
        scan_GLD.scanner(eanUsed);
        Assert.assertEquals(2, scan_GLD.quantite(eanUsed));
        cleanup_GLD();
    }

    // Teste la quantification avec une quantité de zéro pour un article non scanné
    @Test
    public void testQuantite_ZeroQuantity_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        Assert.assertEquals(0, scan_GLD.quantite(8715700110622L));
        cleanup_GLD();
    }

    // Teste le scanner avec un produit reconnu
    @Test
    public void testScanner_ProduitReconnu_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        long eanUsed = 8715700110622L;
        Assert.assertEquals(0, scan_GLD.scanner(eanUsed));
        Assert.assertEquals(1, scan_GLD.getArticles().size());
        cleanup_GLD();
    }

    // Teste le scanner avec un produit non reconnu
    @Test
    public void testScanner_ProduitNonReconnu_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        long unknownEAN = 1234567890123L;
        Assert.assertEquals(-2, scan_GLD.scanner(unknownEAN));
        cleanup_GLD();
    }

    // Teste le scanner alors que la scanette est bloquée
    @Test
    public void testScanner_ScanBloque_GLD() throws FileFormatException, IOException {
        setup_GLD();
        long eanUsed = 8715700110622L;
        Assert.assertEquals(-1, scan_GLD.scanner(eanUsed));
        cleanup_GLD();
    }

    // Teste la suppression d'un produit du panier
    @Test
    public void testSupprimer_ProduitDansPanier_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        long eanUsed = 8715700110622L;
        scan_GLD.scanner(eanUsed);
        Assert.assertEquals(0, scan_GLD.supprimer(eanUsed));
        Assert.assertEquals(0, scan_GLD.quantite(eanUsed));
        cleanup_GLD();
    }

    // Teste la suppression d'un produit du panier lorsque la quantité de produit est supérieur à 1
    @Test
    public void testSupprimer_ProduitDansPanier_n_sup_1_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        long eanUsed = 8715700110622L;
        int quantity = 5;
        for (int i = 0; i < quantity; i++) {
            scan_GLD.scanner(eanUsed);
            quantity--;
        }
        Assert.assertEquals(0, scan_GLD.supprimer(eanUsed));
        Assert.assertEquals(quantity, scan_GLD.quantite(eanUsed));
        cleanup_GLD();
    }

    // Teste la suppression d'un produit qui n'est pas dans le panier
    @Test
    public void testSupprimer_ProduitAbsent_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        long eanUsed = 8715700110622L;
        Assert.assertEquals(-2, scan_GLD.supprimer(eanUsed));
        cleanup_GLD();
    }

    // Teste la suppression d'un produit alors que la scanette est bloquée
    @Test
    public void testSupprimer_ScanBloquee_GLD() throws FileFormatException, IOException {
        setup_GLD();
        long eanUsed = 8715700110622L;
        Assert.assertEquals(-1, scan_GLD.supprimer(eanUsed));
        cleanup_GLD();
    }

    // Teste l'abandon de la transaction et le blocage de la scanette
    @Test
    public void testAbandon_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();  // Débloque la scanette
        long eanUsed = 8715700110622L;
        scan_GLD.scanner(eanUsed);  // Ajoute un produit au panier
        scan_GLD.abandon();  // Abandonne la transaction

        // Vérifie que l'état est bien revenu à BLOQUEE et que le panier est vide
        Assert.assertEquals(0, scan_GLD.debloquer());  // Peut être débloqué car l'état est BLOQUEE
        Assert.assertEquals(0, scan_GLD.quantite(eanUsed));  // Vérifie que le panier est vide après abandon
        Assert.assertTrue(scan_GLD.getArticles().isEmpty());  // Vérifie que le panier est effectivement vide
        cleanup_GLD();
    }


    // Teste la récupération des articles non reconnus
    @Test
    public void testGetReferencesInconnues_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        long unknownEAN = 1234567890123L;
        scan_GLD.scanner(unknownEAN);
        Set<Long> inconnus = scan_GLD.getReferencesInconnues();
        Assert.assertTrue(inconnus.contains(unknownEAN));
        cleanup_GLD();
    }

    @Test
    public void testTransmission_CaisseNull_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        Assert.assertEquals(-1, scan_GLD.transmission(null));
        cleanup_GLD();
    }

    // Teste la méthode de transmission avec une relecture demandée
    @Test
    public void testTransmission_RelectureDemandee_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        Mockito.when(mockCaisse_GLD.connexion(scan_GLD)).thenReturn(1);  // Simule une demande de relecture

        Assert.assertEquals(1, scan_GLD.transmission(mockCaisse_GLD));
        cleanup_GLD();
    }

    // Teste la méthode de transmission sans relecture
    @Test
    public void testTransmission_AucuneRelecture_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        Mockito
                .when(mockCaisse_GLD.connexion(scan_GLD))
                .thenReturn(0);  // Simule une absence de relecture

        Assert.assertEquals(0, scan_GLD.transmission(mockCaisse_GLD));
        cleanup_GLD();
    }

    // Teste la méthode de transmission avec une erreur de la caisse
    @Test
    public void testTransmission_Erreur_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        Mockito.when(mockCaisse_GLD.connexion(scan_GLD)).thenReturn(-1);  // Simule une erreur

        Assert.assertEquals(-1, scan_GLD.transmission(mockCaisse_GLD));
        cleanup_GLD();
    }

    // Teste la relecture effectuée avec succès
    @Test
    public void testRelectureEffectuee_Succes_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        Mockito.when(mockCaisse_GLD.connexion(scan_GLD)).thenReturn(1);  // Simule une absence de relecture

        scan_GLD.transmission(mockCaisse_GLD);
        Assert.assertTrue(scan_GLD.relectureEffectuee());
        cleanup_GLD();
    }

    // Teste la relecture en échec
    @Test
    public void testRelectureEffectuee_Echec_GLD() throws FileFormatException, IOException {
        setup_GLD();
        scan_GLD.debloquer();
        long eanUsed = 8715700110622L;
        scan_GLD.scanner(eanUsed);
        Mockito.when(mockCaisse_GLD.connexion(scan_GLD)).thenReturn(1);  // Simule une demande de relecture
        scan_GLD.transmission(mockCaisse_GLD);

        scan_GLD.scanner(1234567890123L);  // Simule un produit incorrect lors de la relecture
        Assert.assertFalse(scan_GLD.relectureEffectuee());
        cleanup_GLD();
    }

    public void cleanup_GLD() throws IOException {
        Files.deleteIfExists(csv_file_GLD.toPath());  // Supprime le fichier CSV temporaire
    }

}
