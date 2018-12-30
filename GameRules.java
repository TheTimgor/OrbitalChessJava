
public class GameRules {
	
	// Board info
	//public static int[] ROT = { 0, 8, 4, 2, 0 };
	public static int[] ROT = { 0, 8, 8, 4, 4, 2, 2, 0, 0 };
	public static int THETA = 16;
	public static int RAD = 8;
	
	// Origin rules
	public static boolean ALL_PIECES_TAKE_ORIGIN = false;
	public static boolean CAPTURE_FROM_ORIGIN = false;
	public static boolean BISHOPS_TAKE_ORIGIN = true;
	
	// Bishop Rules
	public static boolean BOUNCE_AT_PERIHELION = true;
	public static boolean ORIGIN_FLYBY = false;
	
	// Knight Rules
	public static boolean PRANKSTER_KNIGHT = true;
	
	// Pawn Rules
	public static boolean PAWNS_PROMOTE_AT_ORIGIN = true;
	public static boolean PAWNS_PROMOTE_AT_R1 = false;
	public static boolean PASSERBY_ATTACK = true;
	public static boolean DOUBLE_MOVE = true;

}
