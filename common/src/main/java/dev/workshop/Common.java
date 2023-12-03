package dev.workshop;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Common {
    public static final System.Logger logger = System.getLogger(Common.class.getName());

    private static final String URI_PROPERTY_KEY = "uri";
    private static final String PATH_PROPERTY_KEY = "path";
    private static final String COOKIE_PROPERTY_KEY = "cookie";

    private Common() {}

    public static class Configuration {
        private final Map<String, String> properties;

        public Configuration() throws IOException {
            this(Paths.get(System.getProperty("user.dir"), "common", "src", "main", "resources", "common.env"));
        }

        public Configuration(Path path) throws IOException {
            this.properties = this.loadProperties(path);
        }

        public String getProperty(String key) {
            return this.properties.get(key);
        }

        public void addProperty(String key, String value) {
            this.properties.put(Objects.requireNonNull(key), value);
        }

        private Map<String, String> loadProperties(Path path) throws IOException {
            Map<String, String> props = new HashMap<>();
            String file = Files.readString(path);

            String[] splitProperties = file.split(";");
            for (String property : splitProperties) {
                String[] splitProperty = property.trim().split("=");
                props.put(splitProperty[0].trim(), splitProperty[1].trim());
            }

            return props;
        }
    }

    public static String readInputFromSource(String day, String cookie) throws IOException, InterruptedException {
        Configuration configuration = new Configuration();
        configuration.addProperty(Common.URI_PROPERTY_KEY, configuration.getProperty("uri.format").formatted(day));
        configuration.addProperty(Common.PATH_PROPERTY_KEY, configuration.getProperty("path.format").formatted("day" + day));
        configuration.addProperty(Common.COOKIE_PROPERTY_KEY, cookie);

        return Common.readInputFromSource(configuration);
    }

    public static String readInputFromSource(Configuration configuration) throws IOException, InterruptedException {
        Path path = Paths.get(configuration.getProperty(Common.PATH_PROPERTY_KEY));
        if (Files.exists(path)) {
            Common.logger.log(System.Logger.Level.INFO, "Input file exists, skipping fetch...");
            return Common.readInputFromFile(path);
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(configuration.getProperty(Common.URI_PROPERTY_KEY)))
                .header("Cookie", configuration.getProperty(Common.COOKIE_PROPERTY_KEY))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
        Files.writeString(path, body);

        return body;
    }

    public static String readInputFromFile(Path path) throws IOException {
        return Files.readString(path, Charset.defaultCharset());
    }
}