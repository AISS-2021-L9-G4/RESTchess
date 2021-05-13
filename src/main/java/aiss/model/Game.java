package aiss.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveException;
import com.github.bhlangonijr.chesslib.move.MoveList;

public class Game {
	private String id;
	private String fen;
	private String year;
	private Player white;
	private String result;
	private Player black;
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
		this.image = String.format("http://chessboardimage.com/%s.png",fen);
		String bestMove = null;
		try {
			Process engine = Runtime.getRuntime().exec("src/main/chessengine/stockfish.exe");
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(engine.getOutputStream()));
			BufferedReader in = new BufferedReader(new InputStreamReader(engine.getInputStream()));
			out.write("isready\n");
			out.write("position fen "+ fen +" \ngo movetime 1\n");
			out.flush();
			String line;
			while((line = in.readLine()) != null) {
				
				if (line.contains("bestmove")){
					bestMove = line.split("ponder")[0].replace("bestmove", "").trim();
					engine.destroy();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.bestMove = bestMove;
		
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
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
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
		
		MoveList moves = new MoveList(this.getFen());
        moves.addSanMove(move, true, false);
        Move m = moves.removeLast();
		if (board.legalMoves().contains(m)) {
			board.doMove(m,false);
			setFen(board.getFen());
		} else {
			throw new MoveException("The input move is not a legal move for the current position");
		}
		
		if (board.isDraw()) {
			setResult("draw");
		} else if (board.isMated()) {
			setResult(String.format("%s wins",side.toLowerCase()));
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
