package org.brotherhood.mutantdna.helpers;

public class DnaHelper {

	/*
	 * Asuming that mutant-human ratio is low, the fastest way to detect mutant DNA chains is 
	 * visiting each character as less as possible.
	 * If we check the 4 directions around a given position (East, South, SE, NE) the probability
	 * of finding an equal character is around 68% but the probability of it being part of a mutant
	 * DNA chain is much lower and it can be visited multiple times to detect it which implies more 
	 * execution time, so the best way to detect mutants is checking the DNA in each direction. 
	 */
	public static boolean isMutant(String[] dna) {
		int mutantChainsCounter = 0;

		mutantChainsCounter = countMutantChainsInDirection(dna, mutantChainsCounter, readHorizontally, 0, dna.length, horizontalOrVerticalLimit); 
		if (mutantChainsCounter >= MUTANT_CHAINS_NEEDED)
			return true;

		mutantChainsCounter = countMutantChainsInDirection(dna, mutantChainsCounter, readVertically, 0, dna.length, horizontalOrVerticalLimit); 
		if (mutantChainsCounter >= MUTANT_CHAINS_NEEDED)
			return true;

		//Each diagonal search is divided in two parts by the main diagonals to simplify code
		
		mutantChainsCounter = countMutantChainsInDirection(dna, mutantChainsCounter, readDiagonallySE_Upper, 0, dna.length - 3, diagonalLimit); 
		if (mutantChainsCounter >= MUTANT_CHAINS_NEEDED)
			return true;
		
		mutantChainsCounter = countMutantChainsInDirection(dna, mutantChainsCounter, readDiagonallySE_Lower, 1, dna.length - 3, diagonalLimit); 
		if (mutantChainsCounter >= MUTANT_CHAINS_NEEDED)
			return true;

		mutantChainsCounter = countMutantChainsInDirection(dna, mutantChainsCounter, readDiagonallyNE_Upper, 0, dna.length - 3, diagonalLimit); 
		if (mutantChainsCounter >= MUTANT_CHAINS_NEEDED)
			return true;
		
		mutantChainsCounter = countMutantChainsInDirection(dna, mutantChainsCounter, readDiagonallyNE_Lower, 1, dna.length - 3, diagonalLimit); 
		if (mutantChainsCounter >= MUTANT_CHAINS_NEEDED)
			return true;
		
		return false;
	}
	
	private static final int MUTANT_CHAIN_LENGTH = 4;
	private static final int MUTANT_CHAINS_NEEDED = 2;

	private final static ReaderDirectionModifier readHorizontally = (dna, lineIdx, charIdx) -> dna[lineIdx].charAt(charIdx);
	private final static ReaderDirectionModifier readVertically = (dna, lineIdx, charIdx) -> dna[charIdx].charAt(lineIdx);
	private final static ReaderDirectionModifier readDiagonallySE_Upper = (dna, lineIdx, charIdx) -> dna[charIdx].charAt(charIdx + lineIdx);
	private final static ReaderDirectionModifier readDiagonallySE_Lower = (dna, lineIdx, charIdx) -> dna[charIdx + lineIdx].charAt(charIdx);
	private final static ReaderDirectionModifier readDiagonallyNE_Upper = (dna, lineIdx, charIdx) -> dna[dna.length - lineIdx - charIdx -1].charAt(charIdx);
	private final static ReaderDirectionModifier readDiagonallyNE_Lower = (dna, lineIdx, charIdx) -> dna[dna.length - charIdx -1].charAt(charIdx + lineIdx);
	
	private final static CharIndexLimiter horizontalOrVerticalLimit = (lineIdx, charIdx, dnaWidth, chainLength) -> (charIdx < dnaWidth) && (charIdx < dnaWidth - 3 + chainLength);
	private final static CharIndexLimiter diagonalLimit = (lineIdx, charIdx, dnaWidth, chainLength) -> (charIdx < dnaWidth - lineIdx) && (charIdx < dnaWidth - lineIdx - 3 + chainLength);
	
	/**
	 * Declares Lambda expressions that maps 2 counters (lineIndex and charIndex) to a DNA matrix position
	 * thus changing the direction in which each line travels across the matrix
	 */
	private static interface ReaderDirectionModifier {
		char getChar(String[] dna, int lineIndex, int charIndex);
	}

	/**
	 * Declares Lambda expressions used to limit the lines defined using ReaderDirectionModifier
	 * to restrict them to be inside the DNA matrix.
	 */
	private static interface CharIndexLimiter{
		boolean check(int lineIndex, int charIndex, int dnaWidth, int currentChainLength);
	}
	
	/**
	 * Reads the DNA matrix splitting it in lines going in the direction defined by the ReaderDirectionModifier 
	 * and limited by CharIndexLimiter, counting the amount of mutant DNA chains and stopping as soon 
	 * as it finds MUTANT_CHAINS_NEEDED chains of MUTANT_CHAIN_LENGTH characters.
	 * @param dna
	 * @param mutantChainsCounter
	 * @param reader
	 * @param lineIdxBegin
	 * @param lineIdxLimit
	 * @param charIdxLimiter
	 * @return
	 */
	private static int countMutantChainsInDirection(String[] dna, int mutantChainsCounter, ReaderDirectionModifier reader, int lineIdxBegin, int lineIdxLimit, CharIndexLimiter charIdxLimiter) {

		int dnaWidth=dna.length;
		int chainLength;
		char lastChar;
		char currentChar;

		for (int lineIdx = lineIdxBegin; lineIdx < lineIdxLimit && mutantChainsCounter < MUTANT_CHAINS_NEEDED; lineIdx++) {
			lastChar = reader.getChar(dna, lineIdx, 0);
			chainLength = 1;
			for (int charIdx = 1; charIdxLimiter.check(lineIdx, charIdx, dnaWidth, chainLength) && mutantChainsCounter < MUTANT_CHAINS_NEEDED; charIdx++) {
				currentChar = reader.getChar(dna, lineIdx, charIdx);
				if (lastChar == currentChar) {
					chainLength++;
				}
				else {
					lastChar = currentChar;
					chainLength = 1;
				}
				if (chainLength == MUTANT_CHAIN_LENGTH) {
					mutantChainsCounter ++;
					chainLength = 0;
				}
			}
		}

		return mutantChainsCounter;
	}
}
