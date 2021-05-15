package aiss.model.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import aiss.model.Game;
import aiss.model.Player;
import aiss.model.Results;

public class MapGameRepository implements GameRepository {
	Map<String, Game> gameMap;
	Map<String, Player> playerMap;
	private static MapGameRepository instance=null;
	private int index=0;
	
	public static MapGameRepository getInstance() {
		
		if (instance==null) {
			instance = new MapGameRepository();
			instance.init();
		}
		
		return instance;
	}
	
	public void init() {
		
		gameMap = new HashMap<String,Game>();
		playerMap = new HashMap<String,Player>();
		
		// Create Players
		Player player1=new Player("Viswanathan Anand","2772");
		addPlayer(player1);
		
		Player player2=new Player("Levon Aronian","2802");
		addPlayer(player2);
		
		Player player3=new Player("Paul Morphy","2690");
		addPlayer(player3);
		
		Player player4=new Player("Johan Lowenthal","2510");
		addPlayer(player4);
		
		Player player5=new Player("Stockfish","3549");
		addPlayer(player5);
		
		Player player6=new Player("Player","1500");
		addPlayer(player6);
		
		// Create Games
		Game game1=new Game();
		game1.setFen("2r3k1/pb4p1/4p3/1p3p1q/5Pn1/P1NQb2P/1P4P1/R1B2R1K w - - 4 24");
		game1.setResult(Results.black_wins);
		game1.setWhite(player2);
		game1.setBlack(player1);
		game1.setYear("2013");
		addGame(game1);
		
		Game game2=new Game();
		game2.setFen("8/5pk1/8/4q1pp/8/2Q4P/1P3PP1/6K1 b - - 1 38");
		game2.setResult(Results.black_wins);
		game2.setWhite(player4);
		game2.setBlack(player3);
		game2.setYear("1859");
		addGame(game2);
		
		Game game3=new Game();
		game3.setFen("8/8/3b2k1/8/8/p7/P1K5/8 b - - 3 44");
		game3.setResult(Results.draw);
		game3.setWhite(player3);
		game3.setBlack(player4);
		game3.setYear("1859");
		addGame(game3);
		
		Game game5=new Game();
		game5.setFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		game5.setResult(Results.ongoing);
		game5.setWhite(player6);
		game5.setBlack(player5);
		game5.setYear("2021");
		addGame(game5);
	}
	
	// Game related operations
	@Override
	public void addGame(Game g) {
		String id = "g" + index++;	
		g.setId(id);
		gameMap.put(id,g);
	}
	
	@Override
	public Collection<Game> getAllGames() {
		return gameMap.values();
	}

	@Override
	public Game getGame(String id) {
		return gameMap.get(id);
	}
	
	@Override
	public void updateGame(Game g) {
		gameMap.put(g.getId(),g);
	}

	@Override
	public void deleteGame(String id) {	
		gameMap.remove(id);
	}

	@Override
	public void addMove(String gameId, String move) {
		gameMap.get(gameId).addMove(move);
		
	}

	@Override
	public void addPlayMove(String gameId, String move) {
		gameMap.get(gameId).addPlayMove(move);
	}
	
	
	// Player related operations
	
	@Override
	public void addPlayer(Player p) {
		String id = "p" + index++;
		p.setId(id);
		playerMap.put(id, p);
	}
	
	@Override
	public Collection<Player> getAllPlayers() {
			return playerMap.values();
	}

	@Override
	public Player getPlayer(String playerId) {
		return playerMap.get(playerId);
	}

	@Override
	public void updatePlayer(Player p) {
		Player player = playerMap.get(p.getId());
		player.setName(p.getName());
		player.setRating(p.getRating());
	}

	@Override
	public void deletePlayer(String playerId) {
		playerMap.remove(playerId);
	}
}
