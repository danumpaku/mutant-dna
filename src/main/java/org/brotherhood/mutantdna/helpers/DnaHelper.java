package org.brotherhood.mutantdna.helpers;

import java.util.ArrayList;
import java.util.List;

import org.brotherhood.mutantdna.entities.MutantDetector;
import org.brotherhood.mutantdna.entities.MutantDnaSequence;
import org.brotherhood.mutantdna.entities.Position;

public class DnaHelper {

	private static final int MUTANT_SEQUENCE_LENGTH = 4;

	/*
	 * Asuming that mutant-human ratio is low, the fastest way to detect mutant DNA chains is 
	 * visiting each character as less as possible.
	 * If we check the 4 directions around a given position (East, South, SE, NE) the probability
	 * of finding an equal character is around 68% but the probability of it being part of a mutant
	 * DNA chain is much lower and it can be visited multiple times to detect it which implies more 
	 * execution time, so the best way to detect mutants is checking the DNA in each direction. 
	 */
	public static boolean isMutant(String[] dna) {
		
		MutantDetector detector = new MutantDetector();

		findMutantDnaSequences(detector, dna, toHorizontal, 0, dna.length, horizontalOrVerticalLimit); 
		if (detector.isMutantDetected())
			return true;

		findMutantDnaSequences(detector, dna, toVertical, 0, dna.length, horizontalOrVerticalLimit); 
		if (detector.isMutantDetected())
			return true;

		//Each diagonal search is divided in two parts by the main diagonals to simplify code

		findMutantDnaSequences(detector, dna, toDiagonalSE_Upper, 0, dna.length - 3, diagonalLimit); 
		if (detector.isMutantDetected())
			return true;

		findMutantDnaSequences(detector, dna, toDiagonalSE_Lower, 1, dna.length - 3, diagonalLimit); 
		if (detector.isMutantDetected())
			return true;

		findMutantDnaSequences(detector, dna, toDiagonalNE_Upper, 0, dna.length - 3, diagonalLimit); 
		if (detector.isMutantDetected())
			return true;

		findMutantDnaSequences(detector, dna, toDiagonalNE_Lower, 1, dna.length - 3, diagonalLimit); 
		if (detector.isMutantDetected())
			return true;

		return false;
	}

	/**
	 * Declares Lambda expressions that maps 2 counters (lineIndex and charIndex) to a DNA matrix position with size n x n
	 * thus changing the direction in which each line travels across the matrix
	 */
	private static interface PositionMapper {
		Position map(int lineIndex, int charIndex, int n);
	}

	private final static PositionMapper toHorizontal = (lineIdx, charIdx, n) -> new Position(lineIdx, charIdx);
	private final static PositionMapper toVertical = (lineIdx, charIdx, n) -> new Position(charIdx, lineIdx);
	private final static PositionMapper toDiagonalSE_Upper = (lineIdx, charIdx, n) -> new Position(charIdx, charIdx + lineIdx);
	private final static PositionMapper toDiagonalSE_Lower = (lineIdx, charIdx, n) -> new Position(charIdx + lineIdx, charIdx);
	private final static PositionMapper toDiagonalNE_Upper = (lineIdx, charIdx, n) -> new Position(n - lineIdx - charIdx - 1, charIdx);
	private final static PositionMapper toDiagonalNE_Lower = (lineIdx, charIdx, n) -> new Position(n - charIdx - 1, charIdx + lineIdx);

	/**
	 * Declares Lambda expressions used to limit the lines defined using PositionMapper
	 * to restrict them to be inside the DNA matrix of size n x n.
	 */
	private static interface CharIndexLimiter{
		boolean check(int lineIndex, int charIndex, int n, int currentChainLength);
	}

	private final static CharIndexLimiter horizontalOrVerticalLimit = (lineIdx, charIdx, n, chainLength) -> (charIdx < n) && (charIdx < n - 3 + chainLength);
	private final static CharIndexLimiter diagonalLimit = (lineIdx, charIdx, n, chainLength) -> (charIdx < n - lineIdx) && (charIdx < n - lineIdx - 3 + chainLength);

	/**
	 * Reads the DNA matrix splitting it in lines going in the direction defined by the PositionMapper 
	 * and limited by CharIndexLimiter, finding the mutant DNA sequences and stopping as soon 
	 * as the detector informs it detected a mutant.
	 * @param detector mutant detector object. collects DNA sequences and analyzes if they imply a mutant was detected
	 * @param dna Contains the dna matrix
	 * @param positionMapper This mapper defines the direction in which the lines that visit the dna matrix, are oriented 
	 * @param lineIdxBegin Beginning of the lineIdx counter 
	 * @param lineIdxLimit Limit of the lineIdx counter
	 * @param charIdxLimiter Dynamic limit of the charIdx counter. charIdx always starts at 0. 
	 * @return
	 */
	private static void findMutantDnaSequences(MutantDetector detector, String[] dna, PositionMapper positionMapper, int lineIdxBegin, int lineIdxLimit, CharIndexLimiter charIdxLimiter) {

		int n = dna.length;
		int chainLength;
		char lastChar;
		char currentChar;

		for (int lineIdx = lineIdxBegin; lineIdx < lineIdxLimit && !detector.isMutantDetected(); lineIdx++) {
			lastChar = getCharAt(dna, positionMapper.map(lineIdx, 0, n));
			chainLength = 1;
			for (int charIdx = 1; charIdxLimiter.check(lineIdx, charIdx, n, chainLength) && !detector.isMutantDetected(); charIdx++) {
				currentChar = getCharAt(dna, positionMapper.map(lineIdx, charIdx, n));
				if (lastChar == currentChar) {
					chainLength++;
				}
				else {
					lastChar = currentChar;
					chainLength = 1;
				}
				if (chainLength == MUTANT_SEQUENCE_LENGTH) {
					
					MutantDnaSequence newSequence = createSequence(currentChar, n, lineIdx, charIdx - MUTANT_SEQUENCE_LENGTH + 1, charIdx, positionMapper);
					detector.addDnaSequence(newSequence);
					chainLength--; //allows the creation of an extra sequence if an extra equal character is found. Ex: AAAAA will generate [AAAA]A and A[AAAA]
				}
			}
		}
	}
	
	private static MutantDnaSequence createSequence(char dnaChar, int n, int lineIdx, int charIdxBegin, int charIdxEnd, PositionMapper positionMapper) {
		List<Position> positions = new ArrayList<Position>(MUTANT_SEQUENCE_LENGTH);					
		
		for (int i=charIdxBegin; i<=charIdxEnd; i++)
			positions.add(positionMapper.map(lineIdx, i, n));
	
		return new MutantDnaSequence(dnaChar, positions);
	}

	private static char getCharAt(String[] dna, Position pos) {
		return dna[pos.getRow()].charAt(pos.getCol());
	}
}
