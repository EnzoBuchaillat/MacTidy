import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        final long BYTES_PER_MB = 1024 * 1024; // Le nombre d'octets dans un Mo

        Scanner scanner = new Scanner(System.in); // On créer un scanner pour lire les entrées utilisateur

        String userHome = System.getProperty("user.home"); // Créer la base du targetDirectory jusqu'à l'utilisateur

        Path targetDirectory; // On créer la variable targetDirectory vide pour lui assigner une valeur juste après

        boolean isDirectory; // On créer la variable isDirectory pour stocker si c'est un dossier ou non

        // Une boucle pour que l'utilisateur entre un targetDirectory valide
        do {
            System.out.print("Vous êtes ici : " + userHome + "\nOù voulez vous allez ?\n: ");
            String inputPath = scanner.nextLine(); // Stock la suite du chemin de l'utilisateur
            targetDirectory = Path.of(userHome, inputPath); // Créer le chemin complet avec la base du chemin et la suite de l'utilisateur
            isDirectory = Files.isDirectory(targetDirectory); // Met isDirectory à jour pour savoir si c'est un dossier ou non
            if(!isDirectory){
                if(!Files.exists(targetDirectory)){
                    System.out.println("ERREUR : Le chemin n'existe pas\n"); // Affiche si le chemin n'existe pas
                } else {
                    System.out.println("ERREUR : Le chemin n'est pas un dossier\n"); // Affiche si le chemin existe mais n'est pas un dossier
                }
            }
        } while (!isDirectory);

        long minSizeMb = -1;

        System.out.print("Taille minimum des fichiers à cibler (en Mo) : "); // Demande la taille minimum des fichiers à cibler en Mo

        // Boucle pour vérifier que la valeur taper par l'utilisateur est bien un nombre égal ou supérieur à 0
        while(minSizeMb < 0){
            // Vérifie que la valeur de l'utilisateur est bien un chiffre
            while(!scanner.hasNextLong()){
                scanner.next();
                System.out.print("Entrez une valeur valide : ");
            }
            minSizeMb = scanner.nextLong();
            if(minSizeMb < 0){
                System.out.print("Entrez un nombre positif : ");
            }
        }

        long minSizeBytes = minSizeMb * BYTES_PER_MB; // On convertit la valeur de l'utilisateur (Mo) en Octets pour faire la comparaison

        long minInactiveDays = -1;

        System.out.print("Depuis combien de jours minimum le fichier doit-il être inactif ? : "); // Demande combien de jours minimum le fichier doit-il être inactif

        // Boucle qui vérifie que la valeur taper par l'utilisateur soit bien un nombre égal à 0 ou positif
        while(minInactiveDays < 0){
            while(!scanner.hasNextLong()){
                scanner.next();
                System.out.print("Entrez une valeur valide : ");
            }
            minInactiveDays = scanner.nextLong();
            if(minInactiveDays < 0){
                System.out.print("Entrez un nombre positif : ");
            }
        }

        // On créer une liste qui stokera les fichiers scanné
        List<ScannedFile> matchingFiles = new ArrayList<>();


        /* Créer un nouveau flux qui va servir de "tapis roulant" pour
           faire défiler les élements en les enlevants de la mémoire à chaque fois.*/
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(targetDirectory)){

            // On boucle sur le flux pour ciblé chaque élément un par un.
            for(Path entry : stream){

                // Vérifie si le fichier est bien un fichier standard
                if(Files.isRegularFile(entry)){

                    long sizeByte = Files.size(entry); // Stock la taille du fichier en Octet
                    FileTime lastModifiedTime = Files.getLastModifiedTime(entry); // Récupère et stock la dernière date de modification du fichier
                    Instant today = Instant.now(); // Récupère et stock la date d'aujourd'hui
                    long modifiedDays = ChronoUnit.DAYS.between(lastModifiedTime.toInstant(), today);  // Récupère et stock la dernière modif en jour, grâce à chronoUnit.

                    // Filtre l'affichage sur la taille et le nombre de jours choisi
                    if(sizeByte > minSizeBytes && modifiedDays > minInactiveDays){
                        matchingFiles.add(new ScannedFile(entry, sizeByte, modifiedDays));
                    }
                }
            }
            System.out.println("Liste des fichiers de plus de " + minInactiveDays + (minInactiveDays > 1 ? " Jours : " : " Jour : "));

            for(ScannedFile file : matchingFiles){
                double sizeMb = (double)file.sizeBytes() / BYTES_PER_MB;
                System.out.printf("Nom : %s \nTaille : %.2f Mo\nDernière modification : %d %s%n", file.path(), sizeMb, file.inactiveDays(), file.inactiveDays() > 1 ? "jours" : "jour");
            }
        }

        /* Gère les exceptions possible en affichant l'historique détaillé et le
           cheminement de l'erreur qui vient de se déclancher */
        catch (IOException e){
            e.printStackTrace();
        }
    }
}