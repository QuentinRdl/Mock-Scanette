package fr.l3info.co;

// Mockito
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


// For the tests
import org.junit.Before;
import org.junit.Test;

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

    // Path to the csv files

    String pathArticleProduits = "target/classes/csv/produits.csv";
    String pathArticleVoidProduits ="target/classes/csv/void.csv";

    ICaisse mockCaisse;

   @Before
   public void setup() throws FileFormatException {
       articleProduits.init(new File(pathArticleProduits));
       articleVoidProduits.init(new File(pathArticleVoidProduits));
       mockCaisse = Mockito.mock(ICaisse.class);
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
}
