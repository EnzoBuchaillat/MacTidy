import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {
        //Créer la base du chemin jusqu'à l'utilisateur
        String baseChemin = System.getProperty("user.home");

        /* On créer le chemin complet avec les valeurs séparé par des virgules pour
          que le compilateur gère lui même les bon séparateur à mettre */
        Path chemin = Path.of(baseChemin,"Desktop", "test-projet");

        //Le if vérifie si le chemin exist bien sur notre machine
        if(Files.exists(chemin)){
            /* Créer un nouveau flux qui va servir de "tapis roulant" pour
              faire défiler les élements en les enlevants de la mémoire à chaque fois.*/
            try(DirectoryStream<Path> stream = Files.newDirectoryStream(chemin)){
                /* On boucle sur le flux pour ciblé chaque élément un par un. */
                for(Path entry : stream){
                    /* Si le fichier est bien un fichier standard */
                    if(Files.isRegularFile(entry)){
                        // Stock la taille du fichier en Octet
                        long tailleOctets = Files.size(entry);
                        // Stock la taille du fichier en Mo
                        long tailleMo = tailleOctets / 1048576;
                        // Récupère et stock la dernière date de modification du fichier
                        FileTime dateModification = Files.getLastModifiedTime(entry);
                        // Récupère et stock la date d'aujourd'hui
                        Instant dateAujourdhui = Instant.now();
                        // Récupère et stock la dernière modif en jour, grâce à chronoUnit.
                        long derniereModifJour = ChronoUnit.DAYS.between(dateModification.toInstant(), dateAujourdhui);
                        if(tailleMo > 1 && derniereModifJour > 30){
                            /* affiche le chemin complet de chaque fichier avec sa taille en Mo */
                            System.out.println(entry + "\ntaille : " + tailleMo + " Mo" + "\nDernière modifications : " + derniereModifJour + " Jours");
                        }
                    }
                }
            }

            /* Gère les exceptions possible en affichant l'historique détaillé et le
               cheminement de l'erreur qui vient de se déclancher */
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}