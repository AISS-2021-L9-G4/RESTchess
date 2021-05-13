package aiss.api.resources;

import javax.ws.rs.Path;

import aiss.model.repository.GameRepository;
import aiss.model.repository.MapGameRepository;

@Path("/players")
public class PlayerResource {
		
	/* Singleton */
	private static PlayerResource _instance=null;
	GameRepository repository;
	
//	private PlayerResource() {
//		repository=MapGameRepository.getInstance();
//
//	}
	
	public static PlayerResource getInstance()
	{
		if(_instance==null)
				_instance=new PlayerResource();
		return _instance;
	}
		
}
