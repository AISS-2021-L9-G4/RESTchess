package aiss.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import aiss.api.resources.GameResource;
import aiss.api.resources.PlayerResource;

public class ChessApplication extends Application {
	
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();
	
	public ChessApplication() {
		singletons.add(GameResource.getInstance());
		singletons.add(PlayerResource.getInstance());
	}
	
	@Override
	public Set<Class<?>> getClasses(){
		return classes;
	}
	
	@Override
	public Set<Object> getSingletons(){
		return singletons;
	}
}
