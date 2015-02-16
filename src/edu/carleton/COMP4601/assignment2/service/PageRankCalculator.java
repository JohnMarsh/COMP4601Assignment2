package edu.carleton.COMP4601.assignment2.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Jama.Matrix;
import edu.carleton.COMP4601.assignment2.graph.CrawlerGraph;
import edu.carleton.COMP4601.assignment2.graph.CrawlerVertex;

public class PageRankCalculator {
	
	public static Map<CrawlerVertex, Double> getPageRankScores(CrawlerGraph graph) {
		List<CrawlerVertex> vertexList = new ArrayList<CrawlerVertex>(graph.getGraph().vertexSet());
		Matrix matrix = createConnectionMatrixFromGraph(graph, vertexList);
		matrix = createProbabilityMatrix(matrix, 0.5);
		matrix = createPageRankVectorsFromProbabilityMatrix(matrix);
		
		Map<CrawlerVertex, Double> list = new HashMap<CrawlerVertex, Double>();
		for(int i =0; i < matrix.getColumnDimension(); i++) {
			list.put(vertexList.get(i), matrix.get(0, i));
		}
		return list;
	}
	
	private static Matrix createPageRankVectorsFromProbabilityMatrix(Matrix matrix) {
		List<Matrix> arr = new ArrayList<Matrix>();
		
		arr.add(0, new Matrix(1, matrix.getColumnDimension()));
		arr.get(0).set(0, 0, 1);
		arr.add(1, new Matrix(1, matrix.getColumnDimension()));
		for(int z = 0; z < matrix.getColumnDimension(); z++) {
			arr.get(1).set(0, z, matrix.get(0, z));
		}
		
		int i = 1;
		while(true) {
			Matrix vector = arr.get(i).times(matrix);
			arr.add(vector);
			
			Matrix difference = vector.minus(arr.get(i));
			boolean shouldStop = true;
			for(int z =0; z< difference.getColumnDimension(); z++) {
				if(difference.get(0, z) > 0.001) {
					shouldStop = false;
				}
			}
			
			if(i > 1000 || shouldStop)
				break;
			i++;
		}
		
		return arr.get(arr.size()-1);
	}
	
	private static Matrix createProbabilityMatrix(Matrix matrix, double probability) {
		int[] numOnesPerRow = new int[matrix.getRowDimension()];
		
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			for(int z = 0; z < matrix.getColumnDimension(); z++) {
				double d = matrix.get(i, z);
				if(d >= 1) {
					numOnesPerRow[i] = numOnesPerRow[i] + 1;
				}
			}
		}
		
		Matrix newMatrix = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension());
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			int numOnesForRow = numOnesPerRow[i];
			for(int z = 0; z < matrix.getColumnDimension(); z++) {
				if(matrix.get(i, z) == 0)
					continue;
				if(numOnesForRow == 0) {
					newMatrix.set(i, z, 1.0/matrix.getColumnDimension());
				} else {
					newMatrix.set(i, z, 1.0/numOnesForRow);
				}
			}
		}
		
		Matrix m3 = new Matrix(matrix.getRowDimension(), matrix.getColumnDimension(), probability/matrix.getColumnDimension());
		return newMatrix.times(1.0-probability).plus(m3);
	}
	
	private static Matrix createConnectionMatrixFromGraph(CrawlerGraph loadGraph, List<CrawlerVertex> vertexList) {
		Iterator<CrawlerVertex> iterator = vertexList.iterator();
		Matrix matrix = new Matrix(vertexList.size(), vertexList.size());
		int i = 0;
		while(iterator.hasNext()) {
			CrawlerVertex vertex = iterator.next();
			for(int z = 0; z < vertexList.size(); z++) {
				CrawlerVertex other = vertexList.get(z);
				int val = 0;
				if(!other.equals(vertex))
					val = loadGraph.getGraph().containsEdge(vertex, other) ? 1 : 0;
				matrix.set(i, z, val);
			}
			i++;
		}
		return matrix;
	}
	
	public static void printMatric(Matrix matrix) {
		for(int i = 0; i < matrix.getRowDimension(); i++) {
			for(int z = 0; z < matrix.getColumnDimension(); z++) {
				System.out.print(matrix.get(i, z) + " ");
			}
			System.out.println();
		}
	}
	
	
	public static void main(String[] args) {
		A2DocumentServiceImpl serviceImpl = new A2DocumentServiceImpl();
		serviceImpl.calculateAndSavePageRankScores();
	}
}
