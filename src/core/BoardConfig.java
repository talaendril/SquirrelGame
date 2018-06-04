package core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import location.XY;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class BoardConfig {

    private static final Logger LOGGER = Logger.getLogger(BoardConfig.class.getName());
	
	private int numberOfGoodPlants = 20;
	private int numberOfBadPlants = 15;
	private int numberOfBadBeasts = 9;
	private int numberOfGoodBeasts = 25;
	private int numberOfRandomWalls = 40;

    private int maximumSteps = 10;

    private XY size = new XY(28, 28);

    private String[] botNames = {"randombot", "dijkstrabot"};
	
	public BoardConfig() {
	    loadConfig();
	}

	/*
	loads BoardConfig from config.json
	 */
	private void loadConfig() {
        Path p = Paths.get("config.json");
	    try(InputStream is = Files.newInputStream(p)) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(is);

            this.numberOfGoodPlants = node.path("numberOfGoodPlants").asInt();
            this.numberOfBadPlants = node.path("numberOfBadPlants").asInt();
            this.numberOfBadBeasts = node.path("numberOfBadBeasts").asInt();
            this.numberOfGoodBeasts = node.path("numberOfGoodBeasts").asInt();
            this.numberOfRandomWalls = node.path("numberOfRandomWalls").asInt();
            this.maximumSteps = node.path("maximumSteps").asInt();
            this.size = new XY(node.path("size").path("length").asInt(), node.path("size").path("width").asInt());
            JsonNode botNames = node.path("botNames");
            if(botNames.isArray()) {
                this.botNames = new String[botNames.size()];
                for(int i = 0; i < botNames.size(); i++) {
                    this.botNames[i] = botNames.path(i).asText();
                }
            }
        } catch (IOException e) {
            LOGGER.severe("Config File Loading Failed!");
        }
    }

    public int getNumberOfGoodPlants() {
        return this.numberOfGoodPlants;
    }
	
	public int getNumberOfBadPlants() {
		return numberOfBadPlants;
	}

	public int getNumberOfBadBeasts() {
		return numberOfBadBeasts;
	}

	public int getNumberOfGoodBeasts() {
		return numberOfGoodBeasts;
	}

	public int getNumberOfRandomWalls() {
		return numberOfRandomWalls;
	}

	public int getMaximumSteps() {
	    return this.maximumSteps;
    }
	
	public XY getSize() {
		return this.size;
	}

	public String[] getBotNames() {
	    return this.botNames;
    }
}