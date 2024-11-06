package fr.l3info.co;

// Mockito
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

// For the tests

import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;


// JUnit
import org.junit.runner.RunWith;

// For the usage of files
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TestCaisse {
    ArticleDB articleProduits = new ArticleDB();
    ArticleDB articleVoidProduits = new ArticleDB();

    // Path to the csv files

    String pathArticleProduits = "target/classes/csv/produits.csv";
    String pathArticleVoidProduits ="target/classes/csv/void.csv";

    Scanette mockScan;

    private MaCaisse c;
    private ArticleDB db;
    private Scanette sc;
    private MaCaisse caisse;
    private Scanette sc1;
    private ArticleDB db1;

    @Before
    public void setup() throws FileFormatException {
        articleProduits.init(new File(pathArticleProduits));
        articleVoidProduits.init(new File(pathArticleVoidProduits));

        mockScan = Mockito.spy(new Scanette(articleProduits));

        db = new ArticleDB();
        db.init(new File("target/classes/csv/produits.csv"));
        sc = new Scanette(db);
        c = new MaCaisse(db);
        Assert.assertNotEquals(null, c);

        db1 = new ArticleDB();
        db.init(new File("target/classes/csv/produits_no_errors.csv"));
        caisse = Mockito.spy(new MaCaisse(db1));
        sc1 = Mockito.mock(Scanette.class);
    }

    @Test
    public void testMaCaisse() {
        MaCaisse laCaisse = new MaCaisse(articleProduits);
        assertNotNull(laCaisse);
    }

    @Test
    public void testConnexionNull() {
        MaCaisse laCaisse = new MaCaisse(articleProduits);
        assertNotNull(laCaisse);
        assertEquals(-1, laCaisse.connexion(null));
    }

    @Test
    public void testConnexionScanVide() {
        MaCaisse laCaisse = new MaCaisse(articleProduits);
        assertNotNull(laCaisse);
        assertEquals(0, laCaisse.connexion(mockScan));
    }

    @Test
    public void testConnexionAttenteCaissier1() {
        MaCaisse laCaisse = new MaCaisse(articleProduits);
        assertNotNull(laCaisse);
        Set<Long> hash = new HashSet<Long>();

        hash.add(475475458L);
        hash.add(475470055458L);
        hash.add(4745375458L);

        when(mockScan.getReferencesInconnues()).thenReturn(hash);
        System.out.println(hash.size());

        assertEquals(0, laCaisse.connexion(mockScan));
    }

    @Test
    public void testConnexionAttenteCaissier2() {
        // Test pour la branche OU 0 ref inconnues et 0 articles . size
        MaCaisse laCaisse = new MaCaisse(articleProduits);
        assertNotNull(laCaisse);
        Set<Long> hash = new HashSet<Long>();

        when(mockScan.getReferencesInconnues()).thenReturn(hash);

        assertEquals(0, laCaisse.connexion(mockScan));
    }

    @Test
    public void testConnexionAttenteCaissier3() {
        // Test pour la branche plsieurs ref inconnues et des articles . size
        MaCaisse laCaisse = new MaCaisse(articleProduits);
        assertNotNull(laCaisse);
        Set<Long> hash = new HashSet<Long>();

        /* 3 ref suivantes sont invalides*/
        hash.add(475475458L);
        hash.add(475470055458L);
        hash.add(4745375458L);

        hash.add(3245412567216L); // Ref valide
        when(mockScan.getReferencesInconnues()).thenReturn(hash);

        assertEquals(0, laCaisse.connexion(mockScan));
    }

    @Test
    public void testConnexionEtatCaissePaiement() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan;
        testScan = Mockito.spy(new Scanette(articleProduits));
        assertNotNull(laCaisse);
        Set<Long> hash = new HashSet<Long>();
        testScan.debloquer();

        assertEquals(0, testScan.scanner(3245412567216L));
        when(testScan.getReferencesInconnues()).thenReturn(hash);
        when(laCaisse.demandeRelecture()).thenReturn(false); // Etre sur qu'il n'y a pas de relecture

        System.out.println("sgetReferernceInconnues :" +  testScan.getReferencesInconnues().size());
        System.out.println("sgetArticle :" +  testScan.getArticles().size());

        assertEquals(0, laCaisse.connexion(testScan));
    }

    @Test
    public void testConnexionEtatCaissierEnAttente() {
        // Test pour la branche OU 0 ref inconnues et 0 articles . size
        MaCaisse laCaisse = new MaCaisse(articleProduits);
        assertNotNull(laCaisse);
        Set<Long> hash = new HashSet<Long>();

        when(mockScan.getReferencesInconnues()).thenReturn(hash);

        assertEquals(0, laCaisse.connexion(mockScan));
        assertEquals(-1, laCaisse.connexion(mockScan));
    }

    @Test
    public void testConnexionFalseFalseSizeZero() {
        // relectureEffectuee = false
        // getArticles().size() == 0
        // demandeRelecture() = false
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        assertNotNull(laCaisse);
        when(testScan.relectureEffectuee()).thenReturn(false);
        when(laCaisse.demandeRelecture()).thenReturn(false);

        assertEquals(0, laCaisse.connexion(testScan));
    }

    @Test
    public void testConnexionFalseFalseSizeGreaterThanZero() {
        // relectureEffectuee = false
        // getArticles().size() > 0
        // demandeRelecture() = false
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        assertNotNull(laCaisse);
        when(testScan.relectureEffectuee()).thenReturn(false);
        testScan.debloquer();
        assertEquals(0, testScan.scanner(3245412567216L));
        when(laCaisse.demandeRelecture()).thenReturn(false);

        assertEquals(0, laCaisse.connexion(testScan));
    }

    @Test
    public void testConnexionFalseTrueSizeZero() {
        // relectureEffectuee = false
        // getArticles().size() == 0
        // demandeRelecture() = true
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        assertNotNull(laCaisse);
        when(testScan.relectureEffectuee()).thenReturn(false);
        when(laCaisse.demandeRelecture()).thenReturn(true);

        assertEquals(0, laCaisse.connexion(testScan));
    }

    @Test
    public void testConnexionFalseTrueSizeGreaterThanZero() {
        // relectureEffectuee = false
        // getArticles().size() > 0
        // demandeRelecture() = true
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        assertNotNull(laCaisse);
        when(testScan.relectureEffectuee()).thenReturn(false);
        testScan.debloquer();
        assertEquals(0, testScan.scanner(3245412567216L));
        when(laCaisse.demandeRelecture()).thenReturn(true);

        assertEquals(1, laCaisse.connexion(testScan));
    }

    @Test
    public void testConnexionTrueFalseSizeZero() {
        // relectureEffectuee = true
        // getArticles().size() == 0
        // demandeRelecture() = false
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        assertNotNull(laCaisse);
        when(testScan.relectureEffectuee()).thenReturn(true);
        when(laCaisse.demandeRelecture()).thenReturn(false);

        assertEquals(0, laCaisse.connexion(testScan));
    }

    @Test
    public void testConnexionTrueFalseSizeGreaterThanZero() {
        // relectureEffectuee = true
        // getArticles().size() > 0
        // demandeRelecture() = false
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        assertNotNull(laCaisse);
        when(testScan.relectureEffectuee()).thenReturn(true);
        testScan.debloquer();
        assertEquals(0, testScan.scanner(3245412567216L));
        when(laCaisse.demandeRelecture()).thenReturn(false);

        assertEquals(0, laCaisse.connexion(testScan));
    }

    @Test
    public void testConnexionTrueTrueSizeZero() {
        // relectureEffectuee = true
        // getArticles().size() == 0
        // demandeRelecture() = true
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        assertNotNull(laCaisse);
        when(testScan.relectureEffectuee()).thenReturn(true);
        when(laCaisse.demandeRelecture()).thenReturn(true);

        assertEquals(0, laCaisse.connexion(testScan));
    }

    @Test
    public void testConnexionTrueTrueSizeGreaterThanZero() {
        // relectureEffectuee = true
        // getArticles().size() > 0
        // demandeRelecture() = true
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        assertNotNull(laCaisse);
        when(testScan.relectureEffectuee()).thenReturn(true);
        testScan.debloquer();
        assertEquals(0, testScan.scanner(3245412567216L));
        when(laCaisse.demandeRelecture()).thenReturn(true);

        assertEquals(0, laCaisse.connexion(testScan));
    }

    @Test
    public void testAbandon() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        laCaisse.abandon();
    }

    @Test
    public void ouvrirSession0AttenteCaissierFauxPaiementVrai() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        assertNotNull(laCaisse);

        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        assertNotNull(laCaisse);
        when(testScan.relectureEffectuee()).thenReturn(true);
        testScan.debloquer();
        assertEquals(0, testScan.scanner(3245412567216L));

        when(laCaisse.demandeRelecture()).thenReturn(false);

        assertEquals(0, laCaisse.connexion(testScan));
        assertEquals(0, laCaisse.ouvrirSession());
    }

    @Test
    public void ouvrirSession0AttenteCaissierVraiPaiementFaux() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        assertNotNull(laCaisse);
        Set<Long> hash = new HashSet<Long>();

        hash.add(475475458L);
        hash.add(475470055458L);
        hash.add(4745375458L);

        when(mockScan.getReferencesInconnues()).thenReturn(hash);
        when(mockScan.relectureEffectuee()).thenReturn(true);
        mockScan.debloquer();
        assertEquals(0, mockScan.scanner(3245412567216L));

        when(laCaisse.demandeRelecture()).thenReturn(false);

        assertEquals(0, laCaisse.connexion(mockScan));
        assertEquals(0, laCaisse.ouvrirSession());
    }

    @Test
    public void ouvrirSessionAttenteCaissierFauxPaiementFaux() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        assertEquals(-1, laCaisse.ouvrirSession());
    }

    /* ============ Test fermer session =============== */

    @Test
    public void fermerSessionEtatAutenthifieVraiAchatIsEmptyVrai() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        when(testScan.relectureEffectuee()).thenReturn(true);
        testScan.debloquer();
        laCaisse.connexion(testScan);
        laCaisse.ouvrirSession();
        assertEquals(0, laCaisse.fermerSession());
    }

    @Test
    public void fermerSessionEtatAutenthifieVraiAchatIsEmptyFaux() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        when(testScan.relectureEffectuee()).thenReturn(true);
        testScan.debloquer();
        assertEquals(0, testScan.scanner(3245412567216L));
        laCaisse.connexion(testScan);
        laCaisse.ouvrirSession();
        assertEquals(0, laCaisse.fermerSession());
    }

    @Test
    public void fermerSessionEtatAutenthifieFaux() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        assertNotNull(laCaisse);
        assertEquals(-1, laCaisse.fermerSession());
    }

    /* ============ Test payer =============== */

    @Test
    public void payerEtatPasPayement(){
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        int somme = (int) laCaisse.payer(23);
        assertEquals(-42, somme);
    }

    @Test
    public void payerSommeEgal0(){
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        laCaisse.connexion(testScan);
        laCaisse.ouvrirSession();
        assertEquals(0, laCaisse.scanner(3245412567216L));
        laCaisse.fermerSession();
        int somme = (int) laCaisse.payer(23);
        assertEquals(21, somme);
    }

    @Test
    public void payerSommeNonEgal0(){
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        laCaisse.connexion(testScan);
        laCaisse.ouvrirSession();
        assertEquals(0, laCaisse.scanner(3245412567216L));
        laCaisse.fermerSession();
        int somme = (int) laCaisse.payer(0.1);
        assertEquals(-1, somme);
    }

    /* ============ Test scanner =============== */
    @Test
    public void scannerNonIdentifie() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        assertEquals(-1, laCaisse.scanner(2342L));
    }


    @Test
    public void scannerArticleNull() {
        // Pour passer en etat caisse authentifie il faut
        // ouvirr la session en etat caissant.paiement
        // Pour ca il faut faire connexion avec des articles valides

        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        when(laCaisse.demandeRelecture()).thenReturn(false);
        assertEquals(0,testScan.debloquer());
        assertEquals(0, testScan.scanner(3245412567216L));
        assertEquals(0, laCaisse.connexion(testScan));
        assertEquals(0, laCaisse.ouvrirSession());
        assertEquals(-2, laCaisse.scanner(1231242345312529035L));
        // assertEquals(0, laCaisse.scanner(3245412567216L));

    }

    @Test
    public void scannerContainsKey() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        when(laCaisse.demandeRelecture()).thenReturn(false);
        assertEquals(0,testScan.debloquer());
        assertEquals(0, testScan.scanner(3245412567216L));
        assertEquals(0, laCaisse.connexion(testScan));
        assertEquals(0, laCaisse.ouvrirSession());
        assertEquals(0, laCaisse.scanner(3245412567216L));
        assertEquals(0, laCaisse.scanner(3245412567216L));
    }
    @Test
    public void scannerNotContaisKeyOfA() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        when(laCaisse.demandeRelecture()).thenReturn(false);
        assertEquals(0,testScan.debloquer());
        assertEquals(0, laCaisse.connexion(testScan));
        assertEquals(0, laCaisse.ouvrirSession());
        assertEquals(0, laCaisse.scanner(3245412567216L));
    }

    /* ============ Test supprimer =============== */

    @Test
    public void supprimerNotIdentifie() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        assertEquals(-1, laCaisse.supprimer(2342L));
    }

    @Test
    public void supprimerNotIdentifieEanNotValid() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        // On a besoin de connexion pour passer en etat authentifie
        // Pour ca il faut des articles valides
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        when(laCaisse.demandeRelecture()).thenReturn(false);
        assertEquals(0,testScan.debloquer());
        assertEquals(0, testScan.scanner(3245412567216L));
        assertEquals(0, laCaisse.connexion(testScan));
        assertEquals(0, laCaisse.ouvrirSession());
        assertEquals(-2, laCaisse.supprimer(1231242345312529035L));
    }

    @Test
    public void supprimerNoArticles() {
        // On supprime un article sans en avoir scanner aucun
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        when(laCaisse.demandeRelecture()).thenReturn(false);
        assertEquals(0,testScan.debloquer());
        assertEquals(0, laCaisse.connexion(testScan));
        assertEquals(0, laCaisse.ouvrirSession());
        assertEquals(-2, laCaisse.supprimer(3245412567216L));

    }

    @Test
    public void supprimerNbEq1() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        when(laCaisse.demandeRelecture()).thenReturn(false);
        assertEquals(0,testScan.debloquer());
        assertEquals(0, testScan.scanner(3245412567216L));
        assertEquals(0, laCaisse.connexion(testScan));
        assertEquals(0, laCaisse.ouvrirSession());
        assertEquals(0, laCaisse.supprimer(3245412567216L));
    }

    @Test
    public void supprimerMoreThan1() {
        MaCaisse laCaisse = Mockito.spy(new MaCaisse(articleProduits));
        Scanette testScan = Mockito.spy(new Scanette(articleProduits));
        when(laCaisse.demandeRelecture()).thenReturn(false);
        assertEquals(0,testScan.debloquer());
        assertEquals(0, testScan.scanner(3245412567216L));
        assertEquals(0, testScan.scanner(3245412567216L));
        assertEquals(0, testScan.scanner(3245412567216L));
        assertEquals(0, laCaisse.connexion(testScan));
        assertEquals(0, laCaisse.ouvrirSession());
        assertEquals(0, laCaisse.supprimer(3245412567216L));
    }



    /**

     ████████╗███████╗███████╗████████╗    ████████╗██╗  ██╗███████╗ ██████╗     ██████╗ ███████╗██╗      █████╗ ██████╗  ██████╗  ██████╗██╗  ██╗███████╗
     ╚══██╔══╝██╔════╝██╔════╝╚══██╔══╝    ╚══██╔══╝██║  ██║██╔════╝██╔═══██╗    ██╔══██╗██╔════╝██║     ██╔══██╗██╔══██╗██╔═══██╗██╔════╝██║  ██║██╔════╝
        ██║   █████╗  ███████╗   ██║          ██║   ███████║█████╗  ██║   ██║    ██║  ██║█████╗  ██║     ███████║██████╔╝██║   ██║██║     ███████║█████╗
        ██║   ██╔══╝  ╚════██║   ██║          ██║   ██╔══██║██╔══╝  ██║   ██║    ██║  ██║██╔══╝  ██║     ██╔══██║██╔══██╗██║   ██║██║     ██╔══██║██╔══╝
        ██║   ███████╗███████║   ██║          ██║   ██║  ██║███████╗╚██████╔╝    ██████╔╝███████╗███████╗██║  ██║██║  ██║╚██████╔╝╚██████╗██║  ██║███████╗
        ╚═╝   ╚══════╝╚══════╝   ╚═╝          ╚═╝   ╚═╝  ╚═╝╚══════╝ ╚═════╝     ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝╚══════╝
     */

    @Test
    public void testConnexionScannerEnAttente_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
    }

    @Test
    public void testConnexionScannerNull_DLR() {
        Scanette scNull = null;
        Assert.assertEquals(-1, caisse.connexion(scNull));
    }

    @Test
    public void testConnexionCaisseEnAttenteCaissier_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(-1, caisse.connexion(sc1));
    }

    @Test
    public void testConnexionDemandeRelecture_DLR() {
        Mockito.when(sc1.relectureEffectuee()).thenReturn(false);
        HashSet<Article> ret = new HashSet<Article>();
        Article art = new Article(3020120029030L, 1.70, "Cahier Oxford 90 pages petits carreaux");
        ret.add(art);
        Mockito.when(sc1.getArticles()).thenReturn(ret);

        Mockito.when(caisse.demandeRelecture()).thenReturn(true);

        Assert.assertEquals(1, caisse.connexion(sc1));
    }

    @Test
    public void testConnexionReferencesInconnues_DLR() {
        HashSet<Long> nr = new HashSet<Long>();
        nr.add(3020120029030L);
        Mockito.when(sc1.getReferencesInconnues()).thenReturn(nr);

        Assert.assertEquals(0, caisse.connexion(sc1));
    }

    @Test
    public void testPayerCaissePasPrete_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(-42, caisse.payer(10), 0);
    }

    /* TODO
    @Test
    public void testPayerCaisseSommeExacte_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(0, caisse.scanner(3020120029030L));

        Assert.assertEquals(0, caisse.fermerSession());

        Assert.assertEquals(0, caisse.payer(1.70), 0);
    }

    @Test
    public void testPayerCaisseSommeNulle_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(0, caisse.scanner(3020120029030L));

        Assert.assertEquals(0, caisse.fermerSession());

        Assert.assertEquals(-1.70, caisse.payer(0), 0);
    }


    @Test
    public void testPayerCaisseSommeSuperieure_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(0, caisse.scanner(3020120029030L));

        Assert.assertEquals(0, caisse.fermerSession());

        Assert.assertEquals(10-1.70, caisse.payer(10), 0);
    }
     */

    @Test
    public void testOuvrirSessionInvalide_DLR() {
        Assert.assertEquals(-1, caisse.ouvrirSession());
    }

    @Test
    public void testOuvrirSessionAttenteCaissier_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());
    }

    /* TODO
    @Test
    public void testOuvrirSessionPaiement_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(0, caisse.scanner(3020120029030L));

        Assert.assertEquals(0, caisse.fermerSession());

        Assert.assertEquals(0, caisse.ouvrirSession());
    }
    */

    @Test
    public void testFermerSessionCaisseNonAuthentifiee_DLR() {
        Assert.assertEquals(-1, caisse.fermerSession());
    }

    @Test
    public void testFermerSessionCaisseAuthentifieeAchatsEmpty_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(0, caisse.fermerSession());
    }

    /* TODO
    @Test
    public void testFermerSessionCaisseAuthentifieeAchatsNotEmpty_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(0, caisse.scanner(3020120029030L));

        Assert.assertEquals(0, caisse.fermerSession());
    }
    */

    @Test
    public void testScanCaisseNonAuthentifiee_DLR() {
        Assert.assertEquals(-1, caisse.scanner(3020120029030L));
    }

    @Test
    public void tesScanArticleNonExistant_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(-2, caisse.scanner(3020120029031L));
    }
/* TODO
    @Test
    public void testScanArticlePresent_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(0, caisse.scanner(3020120029030L));
    }

    @Test
    public void testScanArticleDoublon_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(0, caisse.scanner(3020120029030L));
        Assert.assertEquals(0, caisse.scanner(3020120029030L));
    }
    */

    @Test
    public void testSupprimerCaisseNonAuthentifiee_DLR() {
        Assert.assertEquals(-1, caisse.supprimer(3020120029030L));
    }

    @Test
    public void testSupprimerArticleNonPresentAchatsVide_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(-2, caisse.supprimer(3020120029030L));
    }
/* TODO
    @Test
    public void testSupprimerArticleNonPresentAchatsRemplis_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(0, caisse.scanner(3020120029030L));
        Assert.assertEquals(0, caisse.scanner(3245412567216L));

        Assert.assertEquals(-2, caisse.supprimer(3046920010856L));
    }

    @Test
    public void testSupprimerArticlePresent_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(0, caisse.scanner(3020120029030L));

        Assert.assertEquals(0, caisse.supprimer(3020120029030L));
    }

    @Test
    public void testSupprimerPlusieursFoisArticle_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(0, caisse.scanner(3020120029030L));

        Assert.assertEquals(0, caisse.supprimer(3020120029030L));
        Assert.assertEquals(-2, caisse.supprimer(3020120029030L));
    }

    @Test
    public void testSupprimerArticlePresentPlusieursFois_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(0, caisse.scanner(3020120029030L));
        Assert.assertEquals(0, caisse.scanner(3020120029030L));

        Assert.assertEquals(0, caisse.supprimer(3020120029030L));
        Assert.assertEquals(0, caisse.supprimer(3020120029030L));
    }

    @Test
    public void testSupprimerArticlePlusieursFoisEnTrop_DLR() {
        Assert.assertEquals(0, caisse.connexion(sc1));
        Assert.assertEquals(0, caisse.ouvrirSession());

        Assert.assertEquals(0, caisse.scanner(3020120029030L));
        Assert.assertEquals(0, caisse.scanner(3020120029030L));

        Assert.assertEquals(0, caisse.supprimer(3020120029030L));
        Assert.assertEquals(0, caisse.supprimer(3020120029030L));
        Assert.assertEquals(-2, caisse.supprimer(3020120029030L));
    }
*/


    /* =================== Test Quentin Payet =================*/


 
 
     @Test
     public void testConnexionEtatAttenteScannetteVide() {
         Assert.assertEquals(0, c.connexion(sc));
     }
 
     @Test
     public void testConnexionEtatAttenteScannetteNonVide() {
 
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3560070048786l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
         Assert.assertEquals(0, c.connexion(mockScannette));
     }
 
     @Test
     public void testConnexionEtatAttenteScannetteGetReferenceInconnuNonVide() {
 
         // Code inconnues
         Set<Long> s = new HashSet<Long>();
         s.add(4463487097622l);
         s.add(2123201259812l);
         s.add(1190623242310l);
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getReferencesInconnues()).thenReturn(s);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
         Assert.assertEquals(0, c.connexion(mockScannette));
     }
 
     @Test
     public void testConnexionScannetteInexistante() {
         Assert.assertEquals(-1, c.connexion(null));
     }
 
 
     @Test
     public void testConnexionDemandeRelectureFaux() {
 
         // instanciation du mock
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(false);
 
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3560070048786l));
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
 
         // instanciaton du spy
         MaCaisse spyMaCaisse = Mockito.spy(new MaCaisse(db));
         Mockito.when(spyMaCaisse.demandeRelecture()).thenReturn(false);
 
         Assert.assertEquals(0, spyMaCaisse.connexion(mockScannette));
 
     }
 
     @Test
     public void testConnexionDemandeRelectureVrai() {
 
         // instanciation du mock
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(false);
 
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3560070048786l));
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
 
         // instanciaton du spy
         MaCaisse spyMaCaisse = Mockito.spy(new MaCaisse(db));
         Mockito.when(spyMaCaisse.demandeRelecture()).thenReturn(true);
 
         Assert.assertEquals(1, spyMaCaisse.connexion(mockScannette));
     }
 
     @Test
     public  void testConnexionEtatPaiement() {
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
         s.add(db.getArticle(3560071097424l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
         c.connexion(mockScannette);
         Assert.assertEquals(-1,c.connexion(mockScannette));
 
     }
 
     @Test
     public  void testConnexionEtatPaiementAttenteCaissier() {
 
         // Code inconnues
         Set<Long> s = new HashSet<Long>();
         s.add(4463487097622l);
         s.add(2123201259812l);
         s.add(1190623242310l);
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getReferencesInconnues()).thenReturn(s);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
         c.connexion(mockScannette);
         Assert.assertEquals(-1,c.connexion(mockScannette));
     }
 
     @Test
     public  void testConnexionEtatAuthentifier() {
 // Code inconnues
         Set<Long> s = new HashSet<Long>();
         s.add(4463487097622l);
         s.add(2123201259812l);
         s.add(1190623242310l);
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getReferencesInconnues()).thenReturn(s);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
         c.connexion(mockScannette);
         c.ouvrirSession();
         Assert.assertEquals(-1,c.connexion(mockScannette));
     }
 
 
     @Test
     public  void testAbandonQuentin() {
         c.abandon();
         Assert.assertEquals(0, c.connexion(sc));
     }

    @Test
    public  void testAbandonPanierNonVide() {

        Set<Article> s = new HashSet<Article>();
        s.add(db.getArticle(3017620402678l));
        s.add(db.getArticle(3560071097424l));

        Scanette mockScannette = Mockito.mock(Scanette.class);
        Mockito.when(mockScannette.getArticles()).thenReturn(s);

        c.connexion((mockScannette));

        c.abandon();
        Assert.assertEquals(0, c.connexion(sc));
    }

    @Test
    public  void testAbandonTransactionEnCours() {

        Set<Article> s = new HashSet<Article>();
        s.add(db.getArticle(3017620402678l));
        s.add(db.getArticle(3560071097424l));

        Scanette mockScannette = Mockito.mock(Scanette.class);
        Mockito.when(mockScannette.getArticles()).thenReturn(s);

        c.connexion((mockScannette));
        c.payer(10.0);
        c.abandon();
        Assert.assertEquals(0, c.connexion(sc));
    }
 
     @Test
     public  void testPayerEtatCaissePayementAvecRendu() {
 
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
         s.add(db.getArticle(3560071097424l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
 
         c.connexion(mockScannette);
         Assert.assertTrue(c.payer(10) > 0);
     }
 
 
 
     @Test
     public  void testPayerEtatCaissePayementSansRendu() {
 
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
         s.add(db.getArticle(3560071097424l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
 
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
 
         c.connexion(mockScannette);
         Assert.assertTrue( c.payer(1.86) == 0);
     }
 
     @Test
     public  void testPayerPanierVide() {
 
         Set<Article> s = new HashSet<Article>();
         Scanette mockScannette = Mockito.mock(Scanette.class);
 
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
 
 
 
         c.connexion(mockScannette);
         Assert.assertEquals(-42, c.payer(1.86), 0.0);
     }
 
     @Test
     public  void testPayerEtatCaissePayementErreur() {
 
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
         s.add(db.getArticle(3560071097424l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
 
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.quantite(3560071097424l)).thenReturn(1);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
         c.connexion(mockScannette);
         Assert.assertTrue( c.payer(1) < 0.0);
     }



    @Test
    public  void testPayerSommeNegative() {

        Set<Article> s = new HashSet<Article>();
        s.add(db.getArticle(3017620402678l));
        s.add(db.getArticle(3560071097424l));

        Scanette mockScannette = Mockito.mock(Scanette.class);

        Mockito.when(mockScannette.getArticles()).thenReturn(s);
        Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
        Mockito.when(mockScannette.quantite(3560071097424l)).thenReturn(1);
        Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);

        c.connexion(mockScannette);
        Assert.assertTrue( c.payer(-1) < 0.0);
    }
 
 
     @Test
     public  void testOuvrirSessionEtatCaissePayement() {
 
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
         s.add(db.getArticle(3560071097424l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
 
 
 
         c.connexion(mockScannette);
         Assert.assertEquals(0, c.ouvrirSession());
     }
 
     @Test
     public  void testOuvrirSessionEtatAttenteCaissier() {
 
 
         // Code inconnues
         Set<Long> s = new HashSet<Long>();
         s.add(4463487097622l);
         s.add(2123201259812l);
         s.add(1190623242310l);
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getReferencesInconnues()).thenReturn(s);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
         c.connexion(mockScannette);
 
         Assert.assertEquals(0, c.ouvrirSession());
     }
 
 
     @Test
     public  void testOuvrirSessionEtatEnAttente() {
         Assert.assertEquals(-1, c.ouvrirSession());
     }
 
     @Test
     public  void testOuvrirSessionDejaOuverteEtatAuthentifie() {
         // Code inconnues
         Set<Long> s = new HashSet<Long>();
         s.add(4463487097622l);
         s.add(2123201259812l);
         s.add(1190623242310l);
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getReferencesInconnues()).thenReturn(s);
 
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
         c.connexion(mockScannette);
         Assert.assertEquals(0, c.ouvrirSession());
         Assert.assertEquals(-1, c.ouvrirSession());
     }

    @Test
    public  void testOuvrirSessionFermerAuPrealable() {
        // Code inconnues
        Set<Long> s = new HashSet<Long>();
        s.add(4463487097622l);
        s.add(2123201259812l);
        s.add(1190623242310l);

        Scanette mockScannette = Mockito.mock(Scanette.class);
        Mockito.when(mockScannette.getReferencesInconnues()).thenReturn(s);
        Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);

        c.connexion(mockScannette);
        Assert.assertEquals(0, c.ouvrirSession());
        Assert.assertEquals(0, c.fermerSession());
    }
 
     @Test
     public  void testFermerSessionCaissierAuthentifiéPanierNonVide() {
 
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
         s.add(db.getArticle(3560071097424l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.quantite(3560071097424l)).thenReturn(1);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
 
 
         c.connexion(mockScannette);
         c.ouvrirSession();
         Assert.assertEquals(0, c.fermerSession());
     }
 
     @Test
     public  void testFermerSessionCaissierAuthentifiéPanierVide() {
         // Code inconnues
         Set<Long> s = new HashSet<Long>();
         s.add(4463487097622l);
         s.add(2123201259812l);
         s.add(1190623242310l);
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getReferencesInconnues()).thenReturn(s);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
 
         c.connexion(mockScannette);
         c.ouvrirSession();
         Assert.assertEquals(0, c.fermerSession());
     }
 
 
     @Test
     public  void testFermerSessionCaissierNonOuverteEtatEnAttente() {
         Assert.assertEquals(-1, c.fermerSession());
     }
 
     @Test
     public  void testFermerSessionCaissierNonOuverteEtatAttenteCaissier() {
         // Code inconnues
         Set<Long> s = new HashSet<Long>();
         s.add(4463487097622l);
         s.add(2123201259812l);
         s.add(1190623242310l);
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getReferencesInconnues()).thenReturn(s);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
 
 
         c.connexion(mockScannette);
         Assert.assertEquals(-1, c.fermerSession());
     }
 
     @Test
     public  void testFermerSessionCaissierNonOuverteEtatPayement() {
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
         s.add(db.getArticle(3560071097424l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
 
 
         c.connexion(mockScannette);
         Assert.assertEquals(-1, c.fermerSession());
     }
 
     @Test
     public  void testScannerNonAuthentifierEtatEnAttente() {
         Assert.assertEquals(-1, c.scanner(3017800238592l));
     }
 
     @Test
     public  void testScannerNonAuthentifierAttenteCaissier() {
         // Code inconnues
         Set<Long> s = new HashSet<Long>();
         s.add(4463487097622l);
         s.add(2123201259812l);
         s.add(1190623242310l);
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getReferencesInconnues()).thenReturn(s);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
 
 
         c.connexion(mockScannette);
         Assert.assertEquals(-1, c.scanner(3017800238592l));
     }
 
 
     @Test
     public  void testScannerNonAuthentifierEtatPayement() {
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3560070048786l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
 
         c.connexion(mockScannette);
         Assert.assertEquals(-1, c.scanner(3017800238592l));
     }
 
 
     @Test
     public  void testScannerArticleNUll() {
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
         s.add(db.getArticle(3560071097424l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.quantite(3560071097424l)).thenReturn(1);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
 
         c.connexion(mockScannette);
         c.ouvrirSession();
         Assert.assertEquals(-2,c.scanner(71097424l));
     }
 
     @Test
     public  void testScannerAuthentifierNouvelArticle() {
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
         c.connexion(mockScannette);
         c.ouvrirSession();
         Assert.assertEquals(0, c.scanner(5410188006711l));
     }
 
     @Test
     public  void testScannerAuthentifierArticleDejaPresent() {
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
         c.connexion(mockScannette);
         c.ouvrirSession();
         Assert.assertEquals(0, c.scanner(3017620402678l));
     }
 
     @Test
     public  void testSupprimerNonAuthentifierEtatEnAttente() {
         Assert.assertEquals(-1, c.supprimer(3017800238592l));
     }
 
     @Test
     public  void testSupprimerNonAuthentifierAttenteCaissier() {
         // Code inconnues
         Set<Long> s = new HashSet<Long>();
         s.add(4463487097622l);
         s.add(2123201259812l);
         s.add(1190623242310l);
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getReferencesInconnues()).thenReturn(s);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
 
 
         c.connexion(mockScannette);
         Assert.assertEquals(-1, c.supprimer(3017800238592l));
     }
 
 
     @Test
     public  void testSupprimerNonAuthentifierEtatPayement() {
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3560070048786l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
 
         c.connexion(mockScannette);
         Assert.assertEquals(-1, c.supprimer(3017800238592l));
     }
 
 
     @Test
     public  void testSupprimerArticleNUll() {
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
         s.add(db.getArticle(3560071097424l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.quantite(3560071097424l)).thenReturn(1);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
 
         c.connexion(mockScannette);
         c.ouvrirSession();
         Assert.assertEquals(-2,c.supprimer(71097424l));
     }
 
     @Test
     public  void testSupprimerAuthentifierArticleNonPresent() {
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
         c.connexion(mockScannette);
         c.ouvrirSession();
         Assert.assertEquals(-2, c.supprimer(5410188006711l));
     }
 
     @Test
     public  void testSupprimerrAuthentifierArticleDejaPresent() {
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(1);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
         c.connexion(mockScannette);
         c.ouvrirSession();
         Assert.assertEquals(0, c.supprimer(3017620402678l));
     }
 
     @Test
     public  void testSupprimerrAuthentifierArticleDejaPresentPlusieurExemplaire() {
         Set<Article> s = new HashSet<Article>();
         s.add(db.getArticle(3017620402678l));
 
         Scanette mockScannette = Mockito.mock(Scanette.class);
         Mockito.when(mockScannette.getArticles()).thenReturn(s);
         Mockito.when(mockScannette.quantite(3017620402678l)).thenReturn(4);
         Mockito.when(mockScannette.relectureEffectuee()).thenReturn(true);
 
         c.connexion(mockScannette);
         c.ouvrirSession();
         Assert.assertEquals(0, c.supprimer(3017620402678l));
     }

    @Test
    public  void testDemandeRelectureTHRESHOLDZero() {

        c.THRESHOLD = 0.0;
        for (int i = 0; i < 1000; i++) {
            Assert.assertFalse( c.demandeRelecture());
        }
    }

    @Test
    public  void testDemandeRelectureTHRESHOLDUn() {
        c.THRESHOLD = 1.0;
        for (int i = 0; i < 1000; i++) {
            Assert.assertTrue( c.demandeRelecture());
        }
    }

    @Test
    public  void testDemandeRelectureTHRESHOLDCinquanteCiquante() {
        c.THRESHOLD = 0.5;
        int trueCount = 0;
        int falseCount = 0;
        for (int i = 0; i < 1000; i++) {
            if (c.demandeRelecture()) {
                trueCount++;
            } else {
                falseCount++;
            }
        }
        Assert.assertTrue(Math.abs(trueCount - 500) < 100);
        Assert.assertTrue( Math.abs(falseCount - 500) < 100);
    }

     /* --- TESTS - GALLAND Romain --- */

private ArticleDB articleProduits_GLD;
    private Scanette spyScan_GLD;
    private File csvFileForArticleBD_GLD;

    public void setup_GLD() throws FileFormatException, IOException {
        articleProduits_GLD = new ArticleDB();
        articleProduits_GLD.init((csvFileForArticleBD_GLD = TestUtils.generateCsvFile()));
        spyScan_GLD = Mockito.spy(new Scanette(articleProduits_GLD));
    }

    public void after_GLD() throws IOException {
        Files.deleteIfExists(csvFileForArticleBD_GLD.toPath());
    }

    @Test
    public void testAbandon_Transaction_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse caisse = Mockito.spy(new MaCaisse(articleProduits_GLD));  // Utilisation d'un spy pour la caisse
        caisse.abandon();  // Appel de la méthode abandon pour annuler la transaction
        verify(caisse).abandon();  // Vérification que la méthode abandon a bien été appelée
        after_GLD();
    }

    @Test
    public void testConnexion_SuccessfulWithoutRelecture_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse caisse = Mockito.spy(new MaCaisse(articleProduits_GLD));  // Spy sur la caisse
        assertNotNull(caisse);  // Vérification que la caisse est bien initialisée

        Scanette scanette = Mockito.spy(new Scanette(articleProduits_GLD));  // Spy sur la scanette
        assertNotNull(scanette);  // Vérification que la scanette est bien initialisée
        when(scanette.relectureEffectuee()).thenReturn(true);  // Simulation d'une relecture effectuée
        scanette.debloquer();  // Déblocage de la scanette
        assertEquals(0, scanette.scanner(3245412567216L));  // Simulation du scan d'un article

        when(caisse.demandeRelecture()).thenReturn(false);  // Pas de relecture demandée

        assertEquals(0, caisse.connexion(scanette));  // Connexion réussie
        assertEquals(0, caisse.ouvrirSession());  // Ouverture de session réussie
        after_GLD();
    }

    @Test
    public void testOuvrirSession_WithoutPriorConnexionReturnsError_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse caisse = Mockito.spy(new MaCaisse(articleProduits_GLD));  // Spy sur MaCaisse
        assertEquals(-1, caisse.ouvrirSession());  // Tentative d'ouverture de session sans connexion retourne une erreur
        after_GLD();
    }

    @Test
    public void testConnexion_WithUnrecognizedArticlesAndOpenSession_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse caisse = Mockito.spy(new MaCaisse(articleProduits_GLD));  // Spy sur MaCaisse
        assertNotNull(caisse);  // Vérification que la caisse est bien initialisée

        Set<Long> articlesNonReconnus = new HashSet<>();  // Ensemble d'articles non reconnus
        articlesNonReconnus.add(475475458L);
        articlesNonReconnus.add(475470055458L);
        articlesNonReconnus.add(4745375458L);

        Scanette scanetteMock = Mockito.spy(new Scanette(articleProduits_GLD));  // Spy sur la scanette
        when(scanetteMock.getReferencesInconnues()).thenReturn(articlesNonReconnus);  // Simulation d'articles non reconnus
        scanetteMock.debloquer();  // Déblocage de la scanette
        assertEquals(0, scanetteMock.scanner(3245412567216L));  // Simulation du scan d'un article

        when(caisse.demandeRelecture()).thenReturn(false);  // Pas de relecture demandée

        assertEquals(0, caisse.connexion(scanetteMock));  // Connexion réussie avec articles non reconnus
        assertEquals(0, caisse.ouvrirSession());  // Ouverture de session réussie
        after_GLD();
    }

    @Test
    public void testConnexion_WithNullScanetteReturnsError_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse caisse = new MaCaisse(articleProduits_GLD);
        assertNotNull(caisse);  // Vérifie que l'objet caisse est bien initialisé
        assertEquals(-1, caisse.connexion(null));  // Connexion avec une scanette null retourne -1
        after_GLD();
    }

    @Test
    public void testConnexion_WithRelectureRequest_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse caisse = Mockito.spy(new MaCaisse(articleProduits_GLD));
        when(spyScan_GLD.relectureEffectuee()).thenReturn(false);
        spyScan_GLD.debloquer();
        spyScan_GLD.scanner(3046920010856L);
        when(caisse.demandeRelecture()).thenReturn(true);
        caisse.connexion(spyScan_GLD);
        after_GLD();
    }

    @Test
    public void testConnexion_WhenStateIsNotPending_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse caisse = new MaCaisse(articleProduits_GLD);
        caisse.connexion(spyScan_GLD);
        assertEquals(-1, caisse.connexion(spyScan_GLD));
        after_GLD();
    }

    @Test
    public void testFermerSession_StateAuthenticatedAndPurchaseIsEmpty_GLD() throws FileFormatException, IOException {
        setup_GLD();
        when(spyScan_GLD.getArticles()).thenReturn(Collections.emptySet());

        MaCaisse c = new MaCaisse(articleProduits_GLD);

        c.connexion(spyScan_GLD);

        c.ouvrirSession();

        assertEquals(0, c.fermerSession());
        after_GLD();
    }

    @Test
    public void testFermerSession_StateAuthenticatedAndPurchaseIsNotEmpty_GLD() throws FileFormatException, IOException {
        setup_GLD();
        spyScan_GLD.debloquer();

        spyScan_GLD.scanner(9999999999999L);
        spyScan_GLD.scanner(3570590109324L);

        MaCaisse c = Mockito.spy(new MaCaisse(articleProduits_GLD));
        when(c.demandeRelecture()).thenReturn(false);

        System.out.println("c.connexion(spyScan) = " + c.connexion(spyScan_GLD));

        c.ouvrirSession();

        assertEquals(0, c.fermerSession());
        after_GLD();
    }

    @Test
    public void testFermerSession_StateNotAuthenticated_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse c = new MaCaisse(articleProduits_GLD);
        assertEquals(-1, c.fermerSession());
        after_GLD();
    }

    @Test
    public void testPayer_NotInStatePaiement_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse c = new MaCaisse(articleProduits_GLD);
        c.payer(0.0);
        after_GLD();
    }

    @Test
    public void testPayer_GivenExcessAmount_GLD() throws FileFormatException, IOException {
        setup_GLD();
        spyScan_GLD.debloquer();
        spyScan_GLD.scanner(3570590109324L);
        MaCaisse c = new MaCaisse(articleProduits_GLD);
        c.connexion(spyScan_GLD);
        c.ouvrirSession();
        c.fermerSession();
        c.payer(50);
        after_GLD();
    }

    @Test
    public void testPayer_GivenInsufficientAmount_GLD() throws FileFormatException, IOException {
        setup_GLD();
        spyScan_GLD.debloquer();
        spyScan_GLD.scanner(3570590109324L);
        MaCaisse c = new MaCaisse(articleProduits_GLD);
        c.connexion(spyScan_GLD);
        c.ouvrirSession();
        c.fermerSession();
        System.out.println("c.payer(7) = " + c.payer(7));
        after_GLD();
    }

    @Test
    public void testPayer_GivenExactAmount_GLD() throws FileFormatException, IOException {
        setup_GLD();
        spyScan_GLD.debloquer();
        spyScan_GLD.scanner(3570590109324L);
        MaCaisse c = new MaCaisse(articleProduits_GLD);
        c.connexion(spyScan_GLD);
        c.ouvrirSession();
        c.fermerSession();
        System.out.println("c.payer(7) = " + c.payer(7.48));
        after_GLD();
    }

    @Test
    public void testScanner_NotConnected_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse caisse = new MaCaisse(articleProduits_GLD);
        caisse.connexion(spyScan_GLD);
        assertEquals(-1, caisse.scanner(-1L));
        after_GLD();
    }

    @Test
    public void testScanner_UnknownProduct_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse caisse = new MaCaisse(articleProduits_GLD);
        caisse.connexion(spyScan_GLD);
        caisse.ouvrirSession();
        assertEquals(-2, caisse.scanner(-1));
        after_GLD();
    }

    @Test
    public void testScanner_KnownProduct_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse caisse = new MaCaisse(articleProduits_GLD);
        caisse.connexion(spyScan_GLD);
        caisse.ouvrirSession();
        assertEquals(0, caisse.scanner(8715700110622L));
        after_GLD();
    }

    @Test
    public void testScanner_ProductAlreadyPresent_GLD() throws FileFormatException, IOException {
        setup_GLD();
        spyScan_GLD.debloquer();
        spyScan_GLD.scanner(8715700110622L);
        MaCaisse caisse = Mockito.spy(new MaCaisse(articleProduits_GLD));
        when(caisse.demandeRelecture()).thenReturn(false);
        caisse.connexion(spyScan_GLD);
        caisse.ouvrirSession();
        assertEquals(0, caisse.scanner(8715700110622L));
        after_GLD();
    }

    @Test
    public void testSupprimer_NotAuthenticated_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse caisse = Mockito.spy(new MaCaisse(articleProduits_GLD));
        when(caisse.demandeRelecture()).thenReturn(false);
        caisse.connexion(spyScan_GLD);
        assertEquals(-1, caisse.supprimer(-1L));
        after_GLD();
    }

    @Test
    public void testSupprimer_ArticleNotPresent_GLD() throws FileFormatException, IOException {
        setup_GLD();
        MaCaisse caisse = Mockito.spy(new MaCaisse(articleProduits_GLD));
        when(caisse.demandeRelecture()).thenReturn(false);
        caisse.connexion(spyScan_GLD);
        caisse.ouvrirSession();
        assertEquals(-2, caisse.supprimer(-1L));
        after_GLD();
    }

    @Test
    public void testSupprimer_OnlyArticlePresent_GLD() throws FileFormatException, IOException {
        setup_GLD();
        spyScan_GLD.debloquer();
        spyScan_GLD.scanner(8715700110622L);
        MaCaisse caisse = Mockito.spy(new MaCaisse(articleProduits_GLD));
        when(caisse.demandeRelecture()).thenReturn(false);
        caisse.connexion(spyScan_GLD);
        caisse.ouvrirSession();
        assertEquals(0, caisse.supprimer(8715700110622L));
        after_GLD();
    }

    @Test
    public void testSupprimer_NotOnlyArticlePresent_GLD() throws FileFormatException, IOException {
        setup_GLD();
        spyScan_GLD.debloquer();
        spyScan_GLD.scanner(3560070048786L);
        spyScan_GLD.scanner(7640164630021L);
        spyScan_GLD.scanner(3017620402678L);
        MaCaisse caisse = Mockito.spy(new MaCaisse(articleProduits_GLD));
        when(caisse.demandeRelecture()).thenReturn(false);
        caisse.connexion(spyScan_GLD);
        caisse.ouvrirSession();
        assertEquals(0, caisse.supprimer(7640164630021L));
        after_GLD();
    }

    @Test
    public void testSupprimer_DeleteOneOfTwoArticles_GLD() throws FileFormatException, IOException {
        setup_GLD();
        spyScan_GLD.debloquer();
        spyScan_GLD.scanner(3560070048786L);
        spyScan_GLD.scanner(3560070048786L);
        MaCaisse caisse = Mockito.spy(new MaCaisse(articleProduits_GLD));
        when(caisse.demandeRelecture()).thenReturn(false);
        caisse.connexion(spyScan_GLD);
        caisse.ouvrirSession();
        assertEquals(0, caisse.supprimer(3560070048786L));
        after_GLD();
    }


}