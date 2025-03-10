package cl.previred.desafio.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Utility class for handling JSON serialization and deserialization in servlets.
 */
public class JsonUtils {

    public static final Gson gson = new Gson();
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * Sends a JSON response to the client.
     *
     * @param resp   HttpServletResponse object to send the response.
     * @param object The object to be serialized into JSON and sent.
     */
    public static void enviarRespuesta(HttpServletResponse resp, Object object) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            resp.getWriter().write(gson.toJson(object));
        } catch (IOException e) {
            logger.error("Error sending JSON response", e);
        }
    }

    /**
     * Reads the request body and deserializes it into an object of the specified class.
     *
     * @param req   HttpServletRequest object containing the request body.
     * @param clazz The class type to deserialize the JSON into.
     * @param <T>   The type of the class.
     * @return An object of type T deserialized from the JSON request body.
     */
    public static <T> T leerCuerpo(HttpServletRequest req, Class<T> clazz) {
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        } catch (IOException e) {
            logger.error("Error reading request body", e);
            return null; // Or throw an exception, depending on your needs.
        }
        try {
            return gson.fromJson(jsonBuffer.toString(), clazz);
        } catch (Exception e) {
            logger.error("Error deserializing JSON", e);
            return null;
        }

    }
}