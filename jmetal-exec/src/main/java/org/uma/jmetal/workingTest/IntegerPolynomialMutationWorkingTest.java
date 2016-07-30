package org.uma.jmetal.workingTest;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.problem.singleobjective.NIntegerMin;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Antonio J. Nebro
 * @version 1.0
 *
 * This class is intended to verify the working of the polynomial mutation operator for Integer
 * encoding.
 *
 * A figure depicting the values obtained when generating 100000 points, a granularity of 200, and a number
 * of different distribution index values (5, 10, 20) can be found here:
 * <a href="https://github.com/jMetal/jMetal/blob/master/figures/integerPolynomialMutation.png">
 * Polynomial mutation (Integer) </a>
 */
public class IntegerPolynomialMutationWorkingTest {
  /**
   * Program to generate data representing the distribution of points generated by a polynomial
   * mutation operator. The parameters to be introduced by the command line are:
   * - numberOfSolutions: number of solutions to generate
   * - granularity: number of subdivisions to be considered.
   * - distributionIndex: distribution index of the polynomial mutation operator
   * - outputFile: file containing the results
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) throws FileNotFoundException {
    int numberOfPoints ;
    int granularity ;
    double distributionIndex ;
    String outputFileName ;

    if (args.length !=4) {
      JMetalLogger.logger.info("Usage: numberOfSolutions granularity distributionIndex outputFile") ;
      JMetalLogger.logger.info("Using default parameters") ;

      numberOfPoints = 10000 ;
      granularity = 100 ;
      distributionIndex = 20.0 ;
      outputFileName = "integerPolynomialData.txt" ;
    } else {
      numberOfPoints = Integer.valueOf(args[0]);
      granularity = Integer.valueOf(args[1]);
      distributionIndex = Double.valueOf(args[2]);
      outputFileName = args[3];
    }

    IntegerProblem problem ;

    problem = new NIntegerMin(1, 10, -1000, 1000);
    MutationOperator<IntegerSolution> mutation = new IntegerPolynomialMutation(1.0, distributionIndex) ;

    IntegerSolution solution = problem.createSolution() ;
    solution.setVariableValue(0, 0);

    List<IntegerSolution> population = new ArrayList<>(numberOfPoints) ;
    for (int i = 0 ; i < numberOfPoints ; i++) {
      IntegerSolution newSolution = (IntegerSolution) solution.copy();
      mutation.execute(newSolution) ;
      population.add(newSolution) ;
    }

    Collections.sort(population, new VariableComparator()) ;
    double[][] classifier = classify(population, problem, granularity);


    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)));

    try {
      for (int i = 0; i < classifier.length; i++) {
        bufferedWriter
            .write(classifier[i][0] + "\t" + classifier[i][1]);
        bufferedWriter.newLine();
      }
      bufferedWriter.close();
    } catch (IOException e) {
      throw new JMetalException("Error reading data ", e) ;
    }
  }

  private static double[][] classify(List<IntegerSolution> solutions, IntegerProblem problem, int granularity) {
    double grain = (problem.getUpperBound(0) - problem.getLowerBound(0)) / granularity ;
    double[][] classifier = new double[granularity][] ;
    for (int i = 0 ; i < granularity; i++) {
      classifier[i] = new double[2] ;
      classifier[i][0] = problem.getLowerBound(0) + i * grain ;
      classifier[i][1] = 0 ;
    }

    for (IntegerSolution solution : solutions) {
      boolean found = false ;
      int index = 0 ;
      while (!found) {
        if (solution.getVariableValue(0) <= classifier[index][0]) {
          classifier[index][1] ++ ;
          found = true ;
        } else {
          if (index == (granularity - 1)) {
            classifier[index][1] ++ ;
            found = true ;
          } else {
            index++;
          }
        }
      }
    }

    return classifier ;
  }

  public static class VariableComparator implements Comparator<IntegerSolution> {
    /**
     * Compares two solutions according to the first variable value
     *
     * @param solution1 Object representing the first <code>Solution</code>.
     * @param solution2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
     * respectively.
     */
    @Override
    public int compare(IntegerSolution solution1, IntegerSolution solution2) {
      if (solution1 == null) {
        return 1;
      } else if (solution2 == null) {
        return -1;
      }

      if (solution1.getVariableValue(0) < solution2.getVariableValue(0)) {
        return -1;
      }

      if (solution1.getVariableValue(0) > solution2.getVariableValue(0)) {
        return 1;
      }

      return 0;
    }
  }
}
