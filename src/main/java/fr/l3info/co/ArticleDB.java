package fr.l3info.co;

import java.io.*;
import java.util.HashMap;

/**
 * Classe représentant la base de données d'articles
 */
public class ArticleDB {

    private HashMap<Long, Article> theHashMap;

    public ArticleDB() {
        theHashMap = new HashMap<Long, Article>();

    }

    /** Initialise la base de données des articles avec un fichier CSV */
    public void init(File initFile) throws FileFormatException {

        try {
            FileReader reader = new FileReader(initFile);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                // TODO: compléter l'initialisation des articles à partir d'un fichier
                String[] theLine = line.split(",");
                Article articleToWrite = new Article(Long.parseLong(theLine[0]), Double.parseDouble(theLine[1]), theLine[2]);
                theHashMap.put(articleToWrite.getEAN13(), articleToWrite);
            }
        }
        catch (Exception e) {
            throw new FileFormatException(initFile);
        }
    }

    public Article getArticle(long ean13) {
        return theHashMap.get(ean13);
    }

    public int getTailleDB() {
        return theHashMap.size();
    }
}

class FileFormatException extends Exception {
    public FileFormatException(File f) {
        super("Le fichier " + f.getName() + " n'est pas correctement formatté.");
    }
}