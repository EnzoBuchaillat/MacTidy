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

        String userHome = System.getProperty("user.home"); // Créer la base du targetDirectory jusqu'à l'utilisateur

        Path targetDirectory; // On créer la variable targetDirectory vide pour lui assigner une valeur juste après

        boolean isDirectory; // On créer la variable isDirectory pour stocker si c'est un dossier ou non

        // Une boucle pour que l'utilisateur entre un targetDirectory valide
        do {
            System.out.print("Vous êtes ici : " + userHome + "\nOù voulez vous allez ?\n: ");
            String inputPath = scanner.nextLine(); // Stock la suite du targetDirectory de l'utilisateur
            targetDirectory = Path.of(userHome, inputPath); // Créer le targetDirectory complet avec la base du targetDirectory et la suite de l'utilisateur
            isDirectory = Files.isDirectory(targetDirectory); // Met isDirectory à jour
            if(!isDirectory){
                if(!Files.exists(targetDirectory)){
                    System.out.println("ERREUR : Le chemin n'existe pas\n"); // Affiche si le targetDirectory n'existe pas
                } else {
                    System.out.println("ERREUR : Le chemin n'est pas un dossier\n"); // Affiche si le targetDirectory existe mais n'est pas un dossier
                }
            }
        } while (!isDirectory);


        System.out.print("Taille minimum des fichiers à cibler (en Mo) : "); // Demande la taille minimum des fichiers à cibler en Mo
        long minSizeMb = scanner.nextLong(); // Stock la taille minimum choisi

        System.out.print("Depuis combien de jours minimum le fichier doit-il être inactif ? : "); // Demande combien de jours minimum le fichier doit-il être inactif
        long minInactiveDays = scanner.nextLong(); // Stock le nombre de jours d'inactivité choisi

        /* Créer un nouveau flux qui va servir de "tapis roulant" pour
           faire défiler les élements en les enlevants de la mémoire à chaque fois.*/
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(targetDirectory)){

            // On boucle sur le flux pour ciblé chaque élément un par un.
            for(Path entry : stream){

                // Vérifie si le fichier est bien un fichier standard
                if(Files.isRegularFile(entry)){

                    long sizeByte = Files.size(entry); // Stock la taille du fichier en Octet
                    long sizeMb = sizeByte / 1048576; // Stock la taille du fichier en Mo
                    FileTime lastModifiedTime = Files.getLastModifiedTime(entry); // Récupère et stock la dernière date de modification du fichier
                    Instant today = Instant.now(); // Récupère et stock la date d'aujourd'hui
                    long modifiedDays = ChronoUnit.DAYS.between(lastModifiedTime.toInstant(), today);  // Récupère et stock la dernière modif en jour, grâce à chronoUnit.
                    // Filtre l'affichage sur la taille et le nombre de jours choisi
                    if(sizeMb > minSizeMb && modifiedDays > minInactiveDays){
                        System.out.println(entry + "\n taille : " + sizeMb + " Mo" + "\n Dernière modifications : " + modifiedDays + " Jours"); /* affiche le targetDirectory complet de chaque fichier avec sa taille en Mo */
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