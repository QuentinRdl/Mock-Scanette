package fr.l3info.co;

// Mockito
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

// For the tests

import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

// JUnit
import org.junit.runner.RunWith;

// For the usage of files
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TestCaisse {
    ArticleDB articleProduits = new ArticleDB();
    ArticleDB articleVoidProduits = new ArticleDB();

    // Path to the csv files

    String pathArticleProduits = "target/classes/csv/produits.csv";
    String pathArticleVoidProduits ="target/classes/csv/void.csv";

    Scanette mockScan;

    @Before
    public void setup() throws FileFormatException {
        articleProduits.init(new File(pathArticleProduits));
        articleVoidProduits.init(new File(pathArticleVoidProduits));

        mockScan = Mockito.spy(new Scanette(articleProduits));
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





}