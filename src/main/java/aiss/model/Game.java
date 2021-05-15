package aiss.model;



import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveException;
import com.netsensia.rivalchess.engine.search.Search;


public class Game {
	private String id;
	private String fen;
	private String year;
	private Player white;
	private Player black;
	private Results result;
	private String image;
	private String bestMove;
	
	public Game() {}
	
	public Game(String fen) {
		this.fen = fen;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFen() {
		return fen;
	}

	public void setFen(String fen) {
		this.fen = fen;
		this.image = String.format("http://chessboardimage.com/%s.png",fen.split(" ")[0]);
		com.netsensia.rivalchess.model.Board board = com.netsensia.rivalchess.model.Board.fromFen(fen);
		Search searcher = new Search();
		searcher.setBoard(board);
	    searcher.setMillisToThink(5000);
	    searcher.setSearchDepth(5);
	    searcher.go();
		this.bestMove = searcher.getCurrentPath().toString().split(" ")[0];
		
	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Player getWhite() {
		return white;
	}

	public void setWhite(Player white) {
		this.white = white;
	}

	public Player getBlack() {
		return black;
	}

	public void setBlack(Player black) {
		this.black = black;
	}
	
	public Results getResult() {
		return result;
	}

	public void setResult(Results result) {
		this.result = result;
	}
	
	public String getimage() {
		return image;
	}
	public String getBestMove() {
		return bestMove;
	}
	
	public void addMove(String move) {
		Board board = new Board();
		board.loadFromFen(fen);
		String side = board.getSideToMove().toString();
		
		Move m = new Move(Square.valueOf(move.substring(0, 2).toUpperCase()), Square.valueOf(move.substring(2, 4).toUpperCase()));
		
		if (board.legalMoves().contains(m)) {
			board.doMove(m,false);
			setFen(board.getFen());
		} else {
			throw new MoveException("The input move is not a legal move for the current position");
		}
		
		if (board.isDraw()) {
			setResult(Results.draw);
		} else if (board.isMated()) {
			Results result = side.toString().equals("WHITE")? Results.white_wins: Results.black_wins;
			setResult(result);
		}
	}
	
	public void addPlayMove(String move) {
		Board board = new Board();
		addMove(move);
		board.loadFromFen(fen);
		if (!(board.isDraw()||board.isMated())) {
			addMove(getBestMove());
		}
	}
}
