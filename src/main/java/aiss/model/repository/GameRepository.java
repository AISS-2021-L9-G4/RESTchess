package aiss.model.repository;

import java.util.Collection;

import aiss.model.Game;
import aiss.model.Player;

public interface GameRepository {
	// Players
	public void addPlayer(Player p);
	public Collection<Player> getAllPlayers();
	public Player getPlayer(String playerId);
	public void updatePlayer(Player p);
	public void deletePlayer(String playerId);
	
	// Games
	public void addGame(Game g);
	public Collection<Game> getAllGames();
	public Game getGame(String gameId);
	public void updateGame(Game g);
	public void deleteGame(String gameId);
	public void addMove(String gameId,String move);
	public void addPlayMove(String gameId,String move);
	
}
