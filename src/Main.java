import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in); // On créer un scanner pour lire les entrées utilisateur

        String baseChemin = System.getProperty("user.home"); // Créer la base du chemin jusqu'à l'utilisateur

        Path chemin; // On créer la variable chemin vide pour lui assigner une valeur juste après

        // Une boucle pour que l'utilisateur entre un chemin valide
        do {
            System.out.print("Vous êtes ici : " + baseChemin + "\nOù voulez vous allez ?\n: ");
            String suiteChemin = scanner.nextLine(); // Créer un nouveau scanner pour stocker la suite du chemin de l'utilisateur
            chemin = Path.of(baseChemin, suiteChemin); // créer le chemin complet avec la base du chemin et la suite de l'utilisateur
            if(!Files.exists(chemin)){
                System.out.println("Erreur : Entrez un chemin valide"); // Si le chemin n'existe pas on affiche un message d'erreur
            }
        } while (!Files.exists(chemin));


        System.out.print("Taille minimum des fichiers à cibler (en Mo) : "); // Demande la taille minimum des fichiers à cibler en Mo
        long tailleChoisi = scanner.nextLong(); // Stock la taille minimum choisi

        System.out.print("Depuis combien de jours minimum le fichier doit-il être inactif ? : "); // Demande combien de jours minimum le fichier doit-il être inactif
        long nbJourChoisi = scanner.nextLong(); // Stock le nombre de jours d'inactivité choisi

        //Le if vérifie si le chemin exist bien sur notre machine
        if(Files.exists(chemin)){

            /* Créer un nouveau flux qui va servir de "tapis roulant" pour
              faire défiler les élements en les enlevants de la mémoire à chaque fois.*/
            try(DirectoryStream<Path> stream = Files.newDirectoryStream(chemin)){

                // On boucle sur le flux pour ciblé chaque élément un par un.
                for(Path entry : stream){

                    // Vérifie si le fichier est bien un fichier standard
                    if(Files.isRegularFile(entry)){

                        long tailleOctets = Files.size(entry); // Stock la taille du fichier en Octet
                        long tailleMo = tailleOctets / 1048576; // Stock la taille du fichier en Mo
                        FileTime dateModification = Files.getLastModifiedTime(entry); // Récupère et stock la dernière date de modification du fichier
                        Instant dateAujourdhui = Instant.now(); // Récupère et stock la date d'aujourd'hui
                        long derniereModifJour = ChronoUnit.DAYS.between(dateModification.toInstant(), dateAujourdhui);  // Récupère et stock la dernière modif en jour, grâce à chronoUnit.
                        // Filtre l'affichage sur la taille et le nombre de jours choisi
                        if(tailleMo > tailleChoisi && derniereModifJour > nbJourChoisi){
                            System.out.println(entry + "\n taille : " + tailleMo + " Mo" + "\n Dernière modifications : " + derniereModifJour + " Jours"); /* affiche le chemin complet de chaque fichier avec sa taille en Mo */
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