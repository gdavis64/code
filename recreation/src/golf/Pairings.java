package golf;

import java.util.Random;

import common.Logger;

@SuppressWarnings("unused")
public class Pairings {
	
	// Do not change these variables
	public static final int PLAYERS_IN_A_GROUP = 4;

	// Change these variables as needed
	public static final int PLAYERS = 11;
	public static final int ROUNDS = 6;
	public static final int MINIMUM_TIMES_WITH_EACH_PLAYER = 1;
	public static final int MAXIMUM_TIMES_WITH_EACH_PLAYER = 3;
	public static final int MINIMUM_PLAYERS_PER_GROUP = 3;
	public static final int MAXIMUM_ATTEMPTS_TO_FIND_PARINGS = 10000;

	// Number of groups based on number of players
	public static final int GROUPS = (PLAYERS / PLAYERS_IN_A_GROUP) + (PLAYERS % PLAYERS_IN_A_GROUP > 0 ? 1 : 0);

	// Three dimensional array containing rounds, groups, and players
	private static int[][][] pairings = new int[ROUNDS][GROUPS][PLAYERS_IN_A_GROUP];

	public static void main(String[] args) {
		try {
			Logger.log("Begin");
			Logger.log("Players: " + PLAYERS + " Groups: " + GROUPS + " Rounds: " + ROUNDS + " Min times: " + MINIMUM_TIMES_WITH_EACH_PLAYER 
					+ " Max times: " + MAXIMUM_TIMES_WITH_EACH_PLAYER + " Min players per group: " + MINIMUM_PLAYERS_PER_GROUP + " Max attempts: " 
					+ MAXIMUM_ATTEMPTS_TO_FIND_PARINGS);
			Random random = new Random();
			
			// Loop based on the number of rounds
			for (int round = 0; round < ROUNDS; round++) {
				
				// Loop based on the number of groups
				for (int group = 0; group < GROUPS; group++) {
					
					// Loop based on the number of players
					for (int player = 0; player < PLAYERS; player++) {
						
						// Put player in a random group that is not full
						int randomGroup = random.nextInt(PLAYERS_IN_A_GROUP - 1);
						
						// Open slot is 0 through 3 representing one of the 4 slots in a group
						Integer openSlot = getOpenPositionInGroup(round, randomGroup); 
						if (openSlot != null) {
							pairings[round][randomGroup][openSlot] = player + 1;
						}
					}
				}
			}
			displayPairings();
			Logger.log("End");
		} catch(Exception e) {
			Logger.log(e);
		}
	}
	
	// Return the next open array position in the group if the group is not full. If the group is full, return null. 
	private static Integer getOpenPositionInGroup(int round, int group) {
		int openPositionInGroup = 0;
		for (int i = 0; i < PLAYERS_IN_A_GROUP - 1; i++) {
			if (pairings[round][group][i] > 0) {
				openPositionInGroup++;
			}
		}
		return openPositionInGroup < PLAYERS_IN_A_GROUP - 1 ? openPositionInGroup : null;
	}

	private static void displayPairings() {
		String s = "";
		for (int round = 0; round < ROUNDS; round++) {
			s+="\nRound " + (round + 1) + " ";
			for (int group = 0; group < GROUPS; group++) {
				s+="[";
				for (int slot = 0; slot < PLAYERS_IN_A_GROUP; slot++) {
					if (slot > 0) {
						s+= " ";
					}
					s+= pairings[round][group][slot];
				}
				s+="] ";
			}
		}
		Logger.log(s);
	}
}