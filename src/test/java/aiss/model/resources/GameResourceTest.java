package aiss.model.resources;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.bhlangonijr.chesslib.Board;

import aiss.model.Game;

public class GameResourceTest {
	
	@BeforeClass
	public static void setUp() throws Exception {
		
		
		
	}

	@AfterClass
	public static void tearDown() throws Exception {
		
	}

	@Test
	public void testGame() {
//		String m = Game.getBestMove("r2n1rk1/1b2qpp1/3p1P1p/p2Pp3/Pp2Q3/3B1N2/1PP3PP/R4RK1 b - - 0 19");
//		System.out.println(m);
		Game game = new Game();
		game.setFen("r2n1rk1/1b2qpp1/3p1P1p/p2Pp3/Pp2Q3/3B1N2/1PP3PP/R4RK1 b - - 0 19");
		System.out.println(game.getBestMove());
		System.out.println(game.getimage());
		Board b = new Board();
		b.loadFromFen(game.getFen());
		
		game.addMove("Qd7");
		game.addMove("Qh7");
		System.out.println(game.getimage()+"\n"+game.getResult());

		
		
	}
}
