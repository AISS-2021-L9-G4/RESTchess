package aiss.api.resources;

import javax.ws.rs.Path;

import aiss.model.repository.GameRepository;
import aiss.model.repository.MapGameRepository;

@Path("/games")
public class GameResource {
		
	/* Singleton */
	private static GameResource _instance=null;
	GameRepository repository;
	

//	private GameResource() {
//		repository=MapGameRepository.getInstance();
//
//	}

//	private GameResource() {
//		repository=MapGameRepository.getInstance();
//
//	}
	
	public static GameResource getInstance()
	{
		if(_instance==null)
				_instance=new GameResource();
		return _instance;
	}
		
}
