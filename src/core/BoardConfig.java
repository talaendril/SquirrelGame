package core;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import location.XY;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BoardConfig {
	
	private XY size = new XY(28, 28);

	private int maximumSteps = 10;

	private String[] botNames = {"randombot", "dijkstrabot"};
	
	private int numberOfGoodPlants = 20;
	private int numberOfBadPlants = 15;
	private int numberOfBadBeasts = 9;
	private int numberOfGoodBeasts = 25;
	private int numberOfRandomWalls = 40;
    private int totalEntities;
	
	public BoardConfig() {
	    //loadConfig();
        int numberOfWalls = 2 * size.x + 2 * size.y - 4 + numberOfRandomWalls;
		totalEntities = numberOfGoodPlants + numberOfBadPlants +
						numberOfGoodBeasts + numberOfBadBeasts +
                        numberOfWalls;
	}

	public void loadConfig() {
        Path p = Paths.get("config.json");
	    try(InputStream is = Files.newInputStream(p)) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(is);
            numberOfGoodPlants = node.path("numberOfGoodPlants").asInt();
            numberOfBadPlants = node.path("numberOfBadPlants").asInt();
            numberOfBadBeasts = node.path("numberOfBadBeasts").asInt();
            numberOfGoodBeasts = node.path("numberOfGoodBeasts").asInt();
            numberOfRandomWalls = node.path("numberOfRandomWalls").asInt();
        } catch (JsonParseException e) {
	        e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
	
	public int getNumberOfGoodPlants() {
		return this.numberOfGoodPlants;
	}
}