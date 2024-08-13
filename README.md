import java.util.Map;
import java.util.TreeMap;

public class SortQueryString {
    public static void main(String[] args) {
        String query = "slimite=10&offset=2";
        String sortedQuery = sortQueryString(query);
        System.out.println(sortedQuery); // Affiche: offset=2&slimite=10
    }

    public static String sortQueryString(String query) {
        // Utiliser un TreeMap pour trier les clés
        Map<String, String> paramsMap = new TreeMap<>();

        // Diviser la chaîne en paires clé=valeur
        String[] params = query.split("&");

        // Ajouter chaque paramètre dans la map
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                paramsMap.put(keyValue[0], keyValue[1]);
            }
        }

        // Construire la chaîne triée
        StringBuilder sortedQuery = new StringBuilder();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            if (sortedQuery.length() > 0) {
                sortedQuery.append("&");
            }
            sortedQuery.append(entry.getKey()).append("=").append(entry.getValue());
        }

        return sortedQuery.toString();
    }
}
