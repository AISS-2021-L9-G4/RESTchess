package aiss.api.resources;

import javax.ws.rs.Path;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import aiss.api.resources.comparators.ComparatorRatingGame;
import aiss.api.resources.comparators.ComparatorRatingGameReversed;
import aiss.api.resources.comparators.ComparatorResultGame;
import aiss.api.resources.comparators.ComparatorResultGameReversed;
import aiss.api.resources.comparators.ComparatorYearGame;
import aiss.api.resources.comparators.ComparatorYearGameReversed;
import aiss.model.Game;
import aiss.model.Results;
import aiss.model.repository.GameRepository;
import aiss.model.repository.MapGameRepository;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;

import com.github.bhlangonijr.chesslib.move.MoveException;


@Path("/games")
public class GameResource {
		
	/* Singleton */
	private static GameResource _instance=null;
	GameRepository repository;
	

	private GameResource() {
		repository=MapGameRepository.getInstance();

	}
	
	public static GameResource getInstance()
	{
		if(_instance==null)
				_instance=new GameResource();
		return _instance;
	}
	@GET
	@Produces("application/json")
	public Collection<Game> getAllGames(@QueryParam("order") String order, 
			@QueryParam("fen") String fen, @QueryParam("year") String year,
			@QueryParam("result") Results result, @QueryParam("offset") Integer offset, 
			@QueryParam("limit") Integer limit)
	{
		List<Game> res = new ArrayList<Game>();
		
		// Filter
		for(Game game : repository.getAllGames()) {
			if(fen == null || fen.equals(game.getFen())) { 			// Fen filter
				if(year == null || year.equals(game.getYear())) {	// Year filter
					if(result == null || result.equals(game.getResult())) {	// Result enumerated filter
						res.add(game);
					}
				}
			}
		}
		
		// Order
		if(order != null) {
			if(order.equals("rating"))
				Collections.sort(res, new ComparatorRatingGame());
			else if(order.equals("-rating"))
				Collections.sort(res, new ComparatorRatingGameReversed());
			else if(order.equals("year"))
				Collections.sort(res, new ComparatorYearGame());
			else if(order.equals("-year"))
				Collections.sort(res, new ComparatorYearGameReversed());
			else if(order.equals("result"))
				Collections.sort(res, new ComparatorResultGame());
			else if(order.equals("-result"))
				Collections.sort(res, new ComparatorResultGameReversed());
			else 
				throw new BadRequestException("The order parameter must be 'rating', '-rating', 'year', '-year', 'result' or '-result'");	
		}
		
		// Pagination
		int size = repository.getAllGames().size();
		List<Game> resultPagination = new ArrayList<Game>();
		
		if(offset != null && offset > 0 && offset < size) {
			if (limit != null && limit > 0 && (offset + limit) <= size) {
				for(int i = offset; i < (offset + limit); i++) {
					resultPagination.add(res.get(i));
				}
			} else if(limit == null || (offset + limit) > size) {
				for(int i = offset; i < size; i++) {
					resultPagination.add(res.get(i));
				}
			} else {
				throw new BadRequestException("The limit parameter must be greater than 0 and lower than " + res.size() + ".");
			}
		} else if(offset == null) {
			if(limit != null && limit < size) {
				for(int i = 0; i < limit; i++) {
					resultPagination.add(res.get(i));
				}
			} else {
				for(Game game : res) {
					resultPagination.add(game);
				}
			}
		} else {
			throw new BadRequestException("The offset parameter must be greater than 0 and lower than " + res.size() + ".");
		}
		
		if(offset != null || limit != null) {
			return resultPagination;
		} else {
			return res;
		}
	}
	
	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Game get(@PathParam("id") String id)
	{
		Game game = repository.getGame(id);
		
		if (game == null) {
			throw new NotFoundException("The game with id="+ id +" was not found");			
		}
		
		return game;
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addGame(@Context UriInfo uriInfo, Game game) {
		if (game.getFen() == null || "".equals(game.getFen()))
			throw new BadRequestException("The fen must not be null");
		
		repository.addGame(game);

		// Builds the response. Returns the game that has just been added.
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(game.getId());
		ResponseBuilder resp = Response.created(uri);
		resp.entity(game);
		return resp.build();
	}

	
	@PUT
	@Consumes("application/json")
	public Response updateGame(Game game) {
		Game oldgame = repository.getGame(game.getId());
		if (oldgame == null) {
			throw new NotFoundException("The game with id="+ game.getId() +" was not found");			
		}
		
		// Update fen
		if (game.getFen() !=null)
			game.setFen(game.getFen());
		
		// Update year
		if (game.getYear() !=null)
			oldgame.setYear(game.getYear());
		
		// Update result
		if (game.getResult() !=null)
			oldgame.setResult(game.getResult());
		
		//Update Players
		if (game.getWhite() !=null)
			oldgame.setWhite(game.getWhite());
		
		if (game.getBlack() !=null)
			oldgame.setBlack(game.getBlack());
		return Response.noContent().build();
	}
	
	
	@DELETE
	@Path("/{id}")
	public Response removeGame(@PathParam("id") String gameId) {
		Game game = repository.getGame(gameId);
		
		if (game==null)
			throw new NotFoundException("The game with id=" + gameId + " was not found");
		
		repository.deleteGame(gameId);	
		
		return Response.noContent().build();
	}
	
	@POST	
	@Path("/{gameId}/{move}")
	@Produces("application/json")
	public Response addMove(@Context UriInfo uriInfo,@PathParam("gameId") String gameId, @PathParam("move") String move)
	{				
		
		Game game = repository.getGame(gameId);
		
		if (game==null)
			throw new NotFoundException("The game with id=" + gameId + " was not found");
			
		try {
			repository.addMove(gameId, move);
		} catch (MoveException e) {
			throw new BadRequestException(String.format("Ilegal Move (%s) for the current position. fen: %s; Image: %s",move,game.getFen(),game.getimage()));
		}
		// Builds the response
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(gameId);
		ResponseBuilder resp = Response.created(uri);
		resp.entity(game);			
		return resp.build();
	}
	@POST	
	@Path("/play/{gameId}/{move}")
	@Produces("application/json")
	public Response addPlayMove(@Context UriInfo uriInfo,@PathParam("gameId") String gameId, @PathParam("move") String move)
	{				
		
		Game game = repository.getGame(gameId);
		
		if (game==null)
			throw new NotFoundException("The game with id=" + gameId + " was not found");
		try {
			repository.addPlayMove(gameId, move);
		} catch (MoveException e) {
			throw new BadRequestException(String.format("Ilegal Move (%s) for the current position. fen: %s; Image: %s",move,game.getFen(),game.getimage()));
		}
		
		// Builds the response
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(gameId);
		ResponseBuilder resp = Response.created(uri);
		resp.entity(game);			
		return resp.build();
	}
}
