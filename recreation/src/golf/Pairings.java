package golf;

import java.text.DecimalFormat;
import java.util.Random;

import common.Logger;

@SuppressWarnings("unused")
public class Pairings {

	// Change these variables as needed
	public static final int ROUNDS = 6;
	public static final int PLAYERS = 6;
	public static final int MINIMUM_TIMES_WITH_EACH_PLAYER = 1;
	public static final int MAXIMUM_TIMES_WITH_EACH_PLAYER = 4;
	public static final int MINIMUM_PLAYERS_PER_GROUP = 3;
	public static final int MAXIMUM_ATTEMPTS_TO_FIND_PARINGS = 1000000;

	// Do not change these variables
	public static final int PLAYERS_IN_A_GROUP = 3;
	public static final int GROUPS = (PLAYERS / PLAYERS_IN_A_GROUP) + (PLAYERS % PLAYERS_IN_A_GROUP > 0 ? 1 : 0);

	// Three dimensional array containing rounds, groups, and players
	private static Integer[][][] pairings = new Integer[ROUNDS][GROUPS][PLAYERS_IN_A_GROUP];

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
						
				// GD-0 BD-1 KW-2 FK-3 TC-4 JS-5
				
				// Prepopulate the first round
				pairings[0][0][0] = 0; 	pairings[0][1][0] = 3;	
				pairings[0][0][1] = 1;	pairings[0][1][1] = 4;	
				pairings[0][0][2] = 2;	pairings[0][1][2] = 5;	
				
				// Prepopulate the second round
				pairings[1][0][0] = 0; 	pairings[1][1][0] = 2;	
				pairings[1][0][1] = 1;	pairings[1][1][1] = 4;	
				pairings[1][0][2] = 3;	pairings[1][1][2] = 5;	

				// Loop based on the number of rounds
				for (int round = 2; round < ROUNDS; round++) {

					// Loop based on the number of players putting each player in a random group that is not full
					for (int player = 0; player < PLAYERS; player++) {

						// Find a random group that is not full
						Integer openPositionInGroup = null;
						while (openPositionInGroup == null) {
							randomGroup = random.nextInt(PLAYERS_IN_A_GROUP - 1);
							openPositionInGroup = getOpenPositionInGroup(round, randomGroup);
						}
						
						// Put player in the random group that had an opening
						pairings[round][randomGroup][openPositionInGroup] = player;
					}
				}
				
				// Determine if the pairings meet the specified criteria
				if (validPairings()) {
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

	private static boolean validPairings() {
		boolean validPairingsFound = false;
		
		// Determine how many times each player plays with the other players
		int[][] timesWithEachPlayer = new int[PLAYERS][PLAYERS];
		
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
						timesWithEachPlayer[player][pairings[round][playerInGroup][member]]++;
					}
				}
			}
		}
		
		// Determine how many players have the maximum times with each player
		int playersWithMaxTimesWithEachPlayer = 0;
		for (int x = 0; x < timesWithEachPlayer.length; x++) {
			for (int y = 0; y < timesWithEachPlayer.length; y++) {
				if (timesWithEachPlayer[x][y] == MAXIMUM_TIMES_WITH_EACH_PLAYER) {
					playersWithMaxTimesWithEachPlayer++;
					break;
				}
			}
		}
//		System.out.println("max with each: " + playersWithMaxTimesWithEachPlayer);
		boolean firstEditPassed = false;
		if (playersWithMaxTimesWithEachPlayer <= 11) { //Should be maybe 6 or so
			firstEditPassed = true;
		}

		// Determine how many times a player is in a threesome --------------------------------------
		int[] groupNotFull = new int[PLAYERS];

		// Loop on the number of players
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

				// Determine if the group is a threesome
				boolean notFull = false;
				for (int member = 0; member < PLAYERS_IN_A_GROUP; member++) {
					if (pairings[round][playerInGroup][member] == null) {
						notFull = true;
						break;
					}
				}

				if (notFull) {
					groupNotFull[player]++;
				}
			}			
		}
		
		// Get maximum times a player is in a group that is not full
		int maxTimesAPlayerInNonFullGroup = 0;
		for (int x = 0; x < PLAYERS; x++) {
			if (groupNotFull[x] > maxTimesAPlayerInNonFullGroup) {
				maxTimesAPlayerInNonFullGroup = groupNotFull[x];
			}
		}
		
		boolean secondEditPassed = false;
		if (maxTimesAPlayerInNonFullGroup < 3) {
			secondEditPassed = true;
		}
		
		// Determine the minimum and maximum times players are in the same group
		Integer min = null;
		Integer max = null;
		for (int x = 0; x < timesWithEachPlayer.length; x++) {
			for (int y = 0; y < timesWithEachPlayer.length; y++) {
				if (x != y) {
					if (min == null) {
						min = timesWithEachPlayer[x][y];
						max = timesWithEachPlayer[x][y];
					} else if (timesWithEachPlayer[x][y] < min) {
						min = timesWithEachPlayer[x][y];
					} else if (timesWithEachPlayer[x][y] > max) {
						max = timesWithEachPlayer[x][y];
					}
				}
			}			
		}

		if (firstEditPassed && secondEditPassed && min >= MINIMUM_TIMES_WITH_EACH_PLAYER && max <= MAXIMUM_TIMES_WITH_EACH_PLAYER) {
			validPairingsFound = true;

			// Display times with each player
			for (int x = 0; x < timesWithEachPlayer.length; x++) {
				String row = "P" + (x+1) + ": ";
				for (int y = 0; y < timesWithEachPlayer.length; y++) {
					if (y > 0) {
						row+=" ";
					}
//					row +=  "P" + (y+1) + "-" + ((x == y) ? "x" : timesWithEachPlayer[x][y]);
					row +=  (x == y) ? "x" : timesWithEachPlayer[x][y];
				}			
				System.out.println(row);
			}
			
//			for (int x = 0; x < PLAYERS; x++) {
//				System.out.println("P" + (x+1) + ": " + groupNotFull[x]);
//			}

		}
		
		return validPairingsFound;
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