package config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;


public class ConfigParser {
    static private final String[] paramsNames = {
            "mapSize", "maxTankSpawnTimeGap", "maxObstacleSpawnTimeGap",
    };

    public static Config parse(String filePath) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        try (FileReader paramsFile = new FileReader(filePath)) {
            JSONObject params = (JSONObject) jsonParser.parse(paramsFile);
            for (String param: paramsNames) {
                if (params.get(param) == null) {
                    throw new InvalidParameterException("Parameter " + param + " not found in " + filePath);
                }
            }
            return new Config(
                (int)(long) params.get("mapSize"),
                (int)(long) params.get("maxTankSpawnTimeGap"),
                (int)(long) params.get("maxObstacleSpawnTimeGap")
            );
        }
    }
}
