import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import BouncyBall.Items.Ball;
import BouncyBall.Items.Table;
import BouncyBall.Items.Hole;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConfigReader {

	/**
	 * You will probably not want to use a static method/class for this.
	 * 
	 * This is just an example of how to access different parts of the json
	 * 
	 * @param path The path of the json file to read
	 */
	// parse()改写为三种方法，分别创建桌子、桌球和洞

	public static Table createTable(String path) {

		JSONParser parser = new JSONParser();
		try {
			Object object = parser.parse(new FileReader(path));

			// convert Object to JSONObject
			JSONObject jsonObject = (JSONObject) object;

			// reading the Table section:
			JSONObject jsonTable = (JSONObject) jsonObject.get("Table");

			// reading a value from the table section
			String tableColour = (String) jsonTable.get("colour");

			// reading a coordinate from the nested section within the table
			// note that the table x and y are of type Long (i.e. they are integers)
			Long tableX = (Long) ((JSONObject) jsonTable.get("size")).get("x");
			// TODO: Long tableY =
			Long tableY = (Long) ((JSONObject) jsonTable.get("size")).get("y");

			// getting the friction level.
			// This is a double which should affect the rate at which the balls slow down
			Double tableFriction = (Double) jsonTable.get("friction");

			// TODO: delete me, this is just a demonstration:
			// System.out.println("Table colour: " + tableColour + ", x: " + tableX + ", y: " + tableY + ", friction: " + tableFriction);

			// 使用 Table.Constructor 创建 Table 实例
			Table table = new Table.Constructor(tableColour, tableX, tableY, tableFriction).build();

			return table;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 如果抛出异常返回null
		return null;
	}

	public static List<Ball> createBalls(String path) {
		List<Ball> balls = new ArrayList<>();

		JSONParser parser = new JSONParser();
		try {
			Object object = parser.parse(new FileReader(path));

			// convert Object to JSONObject
			JSONObject jsonObject = (JSONObject) object;

			// reading the "Balls" section:
			JSONObject jsonBalls = (JSONObject) jsonObject.get("Balls");

			// reading the "Balls: ball" array:
			JSONArray jsonBallsBall = (JSONArray) jsonBalls.get("ball");

			// reading from the array:
			for (Object obj : jsonBallsBall) {
				JSONObject jsonBall = (JSONObject) obj;

				// the ball colour is a String
				// TODO: String colour =
				String colour = (String) jsonBall.get("colour");
				// the ball position, velocity, mass are all doubles
				Double positionX = (Double) ((JSONObject) jsonBall.get("position")).get("x");
				// TODO: Double positionY =
				Double positionY = (Double) ((JSONObject) jsonBall.get("position")).get("y");
				// TODO: Double velocityX =
				Double velocityX = (Double) ((JSONObject) jsonBall.get("velocity")).get("x");
				// TODO: Double velocityY =
				Double velocityY = (Double) ((JSONObject) jsonBall.get("velocity")).get("y");

				Double mass = (Double) jsonBall.get("mass");

				// TODO: delete me, this is just a demonstration:
				// System.out.println("Ball x: " + positionX + ",Ball y: " + positionY + "V x: " + velocityX + "V y: " + velocityY + "Colour: " + colour + ", mass: " + mass);

				// 使用 Ball.Constructor 创建 Ball 实例
				Ball ball = new Ball.Constructor(colour, positionX, positionY, velocityX, velocityY, mass).build();

				balls.add(ball);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return balls;
	}

	public static List<Hole> createHoles(String path) {
		List<Hole> holes = new ArrayList<>();

		JSONParser parser = new JSONParser();
		try {
			Object object = parser.parse(new FileReader(path));

			// 将 Object 转换为 JSONObject
			JSONObject jsonObject = (JSONObject) object;

			// 读取 "Holes" 部分：
			JSONObject jsonHoles = (JSONObject) jsonObject.get("Holes");

			// 读取 "Holes: hole" 数组：
			JSONArray jsonHoleshole = (JSONArray) jsonHoles.get("hole");

			// 从数组中读取：
			for (Object obj : jsonHoleshole) {
				JSONObject jsonHole = (JSONObject) obj;

				double positionX = (Double) ((JSONObject) jsonHole.get("position")).get("x");
				double positionY = (Double) ((JSONObject) jsonHole.get("position")).get("y");

				// 使用 Hole.Constructor 创建 Hole 实例
				Hole hole = new Hole.Constructor(positionX, positionY).build();

				holes.add(hole);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return holes;
	}

	// ConfigReader的main()方法放入Main类中

	/**
	 * Your main method will probably be in another file!
	 *
	 * @param args First argument is the path to the config file
	 */
	/*
	public static void main(String[] args) {
		// if a command line argument is provided, that should be used as the path
		// if not, you can hard-code a default. e.g. "src/main/resources/config.json"
		// this makes it easier to test your program with different config files
		String configPath;
		if (args.length > 0) {
			configPath = args[0];
		} else {
			configPath = "src/main/resources/config.json";
		}
		// parse the file:
		ConfigReader.parse(configPath);
	}
	*/
}
