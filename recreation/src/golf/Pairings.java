package golf;

import java.text.DecimalFormat;
import java.util.Random;

import common.Logger;

@SuppressWarnings("unused")
public class Pairings {

	// Change these variables as needed
	public static final String[] PLAYER_NAMES = {"GD", "BD", "KW", "FK", "JS", "TC", "GM", "JG", "GH", "JR", "DP"};

	public static final int ROUNDS = 6;
	public static final int MINIMUM_TIMES_WITH_EACH_PLAYER = 1;
	public static final int MAXIMUM_TIMES_WITH_EACH_PLAYER = 3;
	public static final int MINIMUM_PLAYERS_PER_GROUP = 3;
	public static final int MAXIMUM_ATTEMPTS_TO_FIND_PARINGS = 1000000;

	// Do not change these variables
	public static final int PLAYERS_IN_A_GROUP = 4;
	public static final int PLAYERS = PLAYER_NAMES.length;
	public static final int GROUPS = (PLAYERS / PLAYERS_IN_A_GROUP) + (PLAYERS % PLAYERS_IN_A_GROUP > 0 ? 1 : 0);

	// Three dimensional array containing rounds, groups, and players
	private static Integer[][][] pairings; // = new Integer[ROUNDS][GROUPS][PLAYERS_IN_A_GROUP];

	public static void main(String[] args) {
		try {
			System.out.println("Begin");
			System.out.println("Players: " + PLAYERS + " Groups: " + GROUPS + " Rounds: " + ROUNDS + " Min times: " + MINIMUM_TIMES_WITH_EACH_PLAYER 
					+ " Max times: " + MAXIMUM_TIMES_WITH_EACH_PLAYER + " Min players per group: " + MINIMUM_PLAYERS_PER_GROUP + " Max attempts: " 
					+ MAXIMUM_ATTEMPTS_TO_FIND_PARINGS);
			Random random = new Random();
			int randomGroup = 0;
			
			// Loop until a valid pairing is found or the maximum attempts is reached 
			int attempts = 0;
			boolean validPairingsFound = false;
			while (attempts < MAXIMUM_ATTEMPTS_TO_FIND_PARINGS && !validPairingsFound) {
				attempts++;

				// Initialize pairings
				pairings = new Integer[ROUNDS][GROUPS][PLAYERS_IN_A_GROUP];
						
				// Loop based on the number of rounds
				for (int round = 0; round < ROUNDS; round++) {

					// Loop based on the number of players putting each player in a random group that is not full
					for (int player = 0; player < PLAYERS; player++) {

						// Open slot is 0 through 3 representing one of the 4 slots in a group
						Integer playerInGroup = null;
						while (playerInGroup == null) {
							randomGroup = random.nextInt(PLAYERS_IN_A_GROUP - 1);
							playerInGroup = getOpenPositionInGroup(round, randomGroup);
						}
						pairings[round][randomGroup][playerInGroup] = player;
					}
				}
				if (isPairingsAcceptable()) {
					validPairingsFound = true;
				}
			}
			
			// Display pairings if acceptable pairing found within the maximum number of attempts
			if (validPairingsFound) {
				System.out.println("Valid pairing found in " + attempts + " attempts");
				displayPairings();
			} else {
				System.out.println("No acceptable pairings found after " + MAXIMUM_ATTEMPTS_TO_FIND_PARINGS + " attempts");
			}
			System.out.println("End");
		} catch(Exception e) {
			Logger.log(e);
		}
	}

	// Return the next open array position in the group if the group is not full. If the group is full, return null. 
	private static Integer getOpenPositionInGroup(int round, int randomGroup) {
		int openPositionInGroup = 0;
		for (int i = 0; i < PLAYERS_IN_A_GROUP; i++) {
			if (pairings[round][randomGroup][i] != null) { // was > 0
				openPositionInGroup++;
			}
		}
		return openPositionInGroup < PLAYERS_IN_A_GROUP ? openPositionInGroup : null;
	}

	private static boolean isPairingsAcceptable() {
		boolean pairingsAcceptable = false;
		
		// Determine how many times each player plays with the other players
		int[][] times = new int[PLAYERS][PLAYERS];
		
		// Loop over each player determining who he plays with
		for (int player = 0; player < PLAYERS; player++) {

			// Loop based on the number of rounds
			for (int round = 0; round < ROUNDS; round++) {

				// Determine which group the player is in
				Integer playerInGroup = null;
				for (int group = 0; group < GROUPS; group++) {
					for (int member = 0; member < PLAYERS_IN_A_GROUP; member++) {
						if (pairings[round][group][member] != null && pairings[round][group][member] == player) {
							playerInGroup = group;
							break;
						}
					}
					if (playerInGroup != null) {
						break;
					}
				}
				
				// Increment the count for the other players in the group
				for (int member = 0; member < PLAYERS_IN_A_GROUP; member++) {
					if (pairings[round][playerInGroup][member] != null && pairings[round][playerInGroup][member] != player) { 
						times[player][pairings[round][playerInGroup][member]]++;
					}
				}
			}
		}
		
		// Determine the minimum and maximum times players are in the same group
		Integer min = null;
		Integer max = null;
		for (int i = 0; i < times.length; i++) {
			for (int j = 0; j < times.length; j++) {
				if (i != j) {
					if (min == null) {
						min = times[i][j];
						max = times[i][j];
					} else if (times[i][j] < min) {
						min = times[i][j];
					} else if (times[i][j] > max) {
						max = times[i][j];
					}
				}
			}			
		}
//		System.out.println("min: " + min + " max: " + max);
		if (min >= MINIMUM_TIMES_WITH_EACH_PLAYER && max <= MAXIMUM_TIMES_WITH_EACH_PLAYER) {
			pairingsAcceptable = true;

			// Display times
			for (int i = 0; i < times.length; i++) {
				String row = "";
				for (int j = 0; j < times.length; j++) {
					if (j > 0) {
						row+=" ";
					}
					row+= times[i][j];
				}			
				System.out.println(row);
			}
		}
		
		return pairingsAcceptable;
	}
	
	private static void displayPairings() {
		DecimalFormat df = new DecimalFormat( "00");
		String s = "";
		for (int round = 0; round < ROUNDS; round++) {
			if (round > 0) {
				s+="\n";
			}
			s+="Round " + (round + 1) + " ";
			for (int group = 0; group < GROUPS; group++) {
				s+="[";
				for (int playerInGroup = 0; playerInGroup < PLAYERS_IN_A_GROUP; playerInGroup++) {
					if (playerInGroup > 0) {
						s+= " ";
					}
//					s+= pairings[round][group][playerInGroup] != null ? PLAYER_NAMES[pairings[round][group][playerInGroup]] : "  ";
					s+= pairings[round][group][playerInGroup] != null ? df.format(pairings[round][group][playerInGroup] + 1) : "  ";
				}
				s+="] ";
			}
		}
		System.out.println(s);
	}

}