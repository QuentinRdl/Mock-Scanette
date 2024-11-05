package fr.l3info.co;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TestUtils {

    public static final String TEST_CSV_CONTENT = "5410188006711,2.15,Tropicana Tonic Breakfast\n" +
                                         "3560070048786,0.87,Cookies choco\n" +
                                         "3017800238592,2.20,Daucy Curry vert de légumes riz graines de courge et tournesol\n" +
                                         "3560070976478,1.94,Poulet satay et son riz\n" +
                                         "3046920010856,2.01,Lindt Excellence Citron à la pointe de Gingembre\n" +
                                         "8715700110622,0.96,Ketchup\n" +
                                         "3570590109324,7.48,Vin blanc Arbois Vieilles Vignes \n" +
                                         "3520115810259,8.49,Mont d'or moyen Napiot\n" +
                                         "3270190022534,0.58,Pâte feuilletée\n" +
                                         "8718309259938,4.65,Soda stream saveur agrumes\n" +
                                         "3560071097424,2.40,Tartelettes carrées fraise \n" +
                                         "3017620402678,1.86,Nutella 220g\n" +
                                         "3245412567216,1.47,Pain de mie\n" +
                                         "0454964205986,54.99,Jeu switch Minecraft\n" +
                                         "7640164630021,229.90,Robot éducatif Thymio\n" +
                                         "3560070139675,1.94,Boîte de 110 mouchoirs en papier\n"
                                         ;

    public static final String TEMP_DIR = System.getProperty("os.name").toLowerCase().contains("win") ? System.getProperty("java.io.tmpdir") : "/tmp/";


    public static File generateCsvFile() throws IOException {
        Random rdm = new Random();
        File csv_articles_file;
        do csv_articles_file = new File(TEMP_DIR + "csv_articles" + rdm.nextInt(65535) + ".csv");
        while (csv_articles_file.exists());

        csv_articles_file.createNewFile();

        if(!csv_articles_file.canWrite()) throw new IOException("Can't write on " + csv_articles_file.getAbsolutePath());

        FileWriter fw = new FileWriter(csv_articles_file);
        fw.write(TEST_CSV_CONTENT);
        fw.close();
        return csv_articles_file;
    }

    public static ArticleDB generateArticleDB(File csv_file) throws FileFormatException {
        ArticleDB db = new ArticleDB();
        db.init(csv_file);
        return db;
    }

}
