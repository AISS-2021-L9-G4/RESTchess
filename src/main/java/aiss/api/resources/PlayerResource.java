package aiss.api.resources;

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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;

import aiss.api.resources.comparators.ComparatorNamePlayer;
import aiss.api.resources.comparators.ComparatorNamePlayerReversed;
import aiss.api.resources.comparators.ComparatorRatingPlayer;
import aiss.api.resources.comparators.ComparatorRatingPlayerReversed;
import aiss.model.Player;
import aiss.model.repository.GameRepository;
import aiss.model.repository.MapGameRepository;

@Path("/players")
public class PlayerResource {
		
	/* Singleton */
	private static PlayerResource _instance=null;
	GameRepository repository;
	
	private PlayerResource() {
		repository=MapGameRepository.getInstance();

	}
	
	public static PlayerResource getInstance()
	{
		if(_instance==null)
				_instance=new PlayerResource();
		return _instance;
	}
	
	@GET
	@Produces("application/json")
	public Collection<Player> getAllPlayers(@QueryParam("order") String order, 
			@QueryParam("name") String name, @QueryParam("rating") String rating,
			@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit)
	{
		List<Player> result = new ArrayList<Player>();
		
		// Filter
		for(Player player : repository.getAllPlayers()) {
			if(name == null || name.equals(player.getName())) { 			// Name filter
				if(rating == null || rating.equals(player.getRating())) {	// Rating filter
					result.add(player);
				}
			}
		}
		
		// Order
		if(order != null) {
			if(order.equals("name"))
				Collections.sort(result, new ComparatorNamePlayer());
			else if(order.equals("-name"))
				Collections.sort(result, new ComparatorNamePlayerReversed());
			else if(order.equals("rating"))
				Collections.sort(result, new ComparatorRatingPlayer());
			else if(order.equals("-rating"))
				Collections.sort(result, new ComparatorRatingPlayerReversed());
			else 
				throw new BadRequestException("The order parameter must be 'name', '-name', 'rating' or '-rating'");	
		}
		
		// Pagination
		int size = repository.getAllPlayers().size();
		List<Player> resultPagination = new ArrayList<Player>();
		
		if(offset != null && offset > 0 && offset < size) {
			if (limit != null && limit > 0 && (offset + limit) <= size) {
				for(int i = offset; i < (offset + limit); i++) {
					resultPagination.add(result.get(i));
				}
			} else if(limit == null || (offset + limit) > size) {
				for(int i = offset; i < size; i++) {
					resultPagination.add(result.get(i));
				}
			} else {
				throw new BadRequestException("The limit parameter must be greater than 0 and lower than " + result.size() + ".");
			}
		} else if(offset == null) {
			if(limit != null && limit < size) {
				for(int i = 0; i < limit; i++) {
					resultPagination.add(result.get(i));
				}
			} else {
				for(Player player : result) {
					resultPagination.add(player);
				}
			}
		} else {
			throw new BadRequestException("The offset parameter must be greater than 0 and lower than " + result.size() + ".");
		}
		
		if(offset != null || limit != null) {
			return resultPagination;
		} else {
			return result;
		}
	}
	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Player get(@PathParam("id") String playerId)
	{
		Player player = repository.getPlayer(playerId);
		
		if(player == null) {
			throw new NotFoundException("The player with id="+ playerId +" was not found");
		}
		
		return player;
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addPlayer(@Context UriInfo uriInfo, Player player) {
		if(player.getName() == null || "".equals(player.getName())) 
			throw new BadRequestException("The name of the player must not be null");
		if(player.getRating() == null || "".equals(player.getRating()))
			throw new BadRequestException("The rating of the player must not be null");
		
		repository.addPlayer(player);
		
		// Builds the response. Returns the playlist the has just been added.
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(player.getId());
		ResponseBuilder resp = Response.created(uri);
		resp.entity(player);			
		return resp.build();
	}
	
	@PUT
	@Consumes("application/json")
	public Response updatePlayer(Player player) {
		Player oldPlayer = repository.getPlayer(player.getId());
		
		if (oldPlayer == null) 
			throw new NotFoundException("The player with id="+ player.getId() +" was not found");			
		
		// Update name
		if (player.getName() != null)
			oldPlayer.setName(player.getName());
		// Update rating
		if(player.getRating() != null)
			oldPlayer.setRating(player.getRating());
		
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response removePlayer(@PathParam("id") String playerId) {
		Player player = repository.getPlayer(playerId);
		
		if(player == null)
			throw new NotFoundException("The player with id=" + playerId + " was not found");
		else 
			repository.removePlayer(playerId);
		
		return Response.noContent().build();
	}
	
		
}
