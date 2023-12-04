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

    private static Configuration configuration = null;

    private Common() {}

    public static class Configuration {
        private final Map<String, String> properties;

        public Configuration(Path path) throws IOException {
            this.properties = this.loadProperties(path);
        }

        public Configuration() throws IOException {
            this(Paths.get(System.getProperty("user.dir"), "common", "src", "main", "resources", "common.env"));
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

    public static String readInputFromFile(Path path) throws IOException {
        return Files.readString(path, Charset.defaultCharset());
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

    public static String readInputFromSource(String day, String cookie) throws IOException, InterruptedException {
        if (Common.configuration == null) {
            Common.configuration = new Configuration();
        }

        Common.configuration.addProperty(Common.URI_PROPERTY_KEY, Common.configuration.getProperty("uri.format").formatted(day));
        Common.configuration.addProperty(Common.PATH_PROPERTY_KEY, Common.configuration.getProperty("path.format").formatted("day" + day));
        Common.configuration.addProperty(Common.COOKIE_PROPERTY_KEY, cookie);

        return Common.readInputFromSource(Common.configuration);
    }

    @FunctionalInterface
    public interface Solution<T> {
        T run(String input);
    }

    public static <T> T run(String[] args, String day, String cookie, Solution<T> solution) throws IOException, InterruptedException {
        return solution.run((args.length == 1) ? Common.readInputFromFile(Paths.get(args[0])) : Common.readInputFromSource(day, cookie));
    }
}