//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.impl.crossover.BusProblemCrossover;
import org.uma.jmetal.operator.impl.mutation.BusProblemMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.ProyectoAE;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.BusSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.*;
import org.uma.jmetal.util.experiment.util.TaggedAlgorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example of experimental study based on solving the ZDT problems with four versions of NSGA-II, each
 * of them applying a different crossover probability (from 0.7 to 1.0).
 *
 * This experiment assumes that the reference Pareto front are not known, so the names of files containing
 * them and the directory where they are located must be specified.
 *
 * Six quality indicators are used for performance assessment.
 *
 * The steps to carry out the experiment are:
 * 1. Configure the experiment
 * 2. Execute the algorithms
 * 3. Generate the reference Pareto fronts
 * 4. Compute the quality indicators
 * 5. Generate Latex tables reporting means and medians
 * 6. Generate Latex tables with the result of applying the Wilcoxon Rank Sum Test
 * 7. Generate Latex tables with the ranking obtained by applying the Friedman test
 * 8. Generate R scripts to obtain boxplots
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class BusProblemStudy2 {
  private static final int INDEPENDENT_RUNS = 1 ;

  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      throw new JMetalException("Needed arguments: experimentBaseDirectory referenceFrontDirectory") ;
    }
    String experimentBaseDirectory = args[0] ;
    String referenceFrontDirectory = args[1] ;

    List<Problem<BusSolution>> problemList = Arrays.<Problem<BusSolution>>asList(new ProyectoAE("lineas")) ;

    List<TaggedAlgorithm<List<BusSolution>>> algorithmList = configureAlgorithmList(problemList, INDEPENDENT_RUNS) ;

    Experiment<BusSolution, List<BusSolution>> experiment =
        new ExperimentBuilder<BusSolution, List<BusSolution>>("BusProblemStudy2")
            .setAlgorithmList(algorithmList)
            .setProblemList(problemList)
            .setExperimentBaseDirectory(experimentBaseDirectory)
            .setOutputParetoFrontFileName("FUN")
            .setOutputParetoSetFileName("VAR")
            .setReferenceFrontDirectory(referenceFrontDirectory)
            .setIndicatorList(Arrays.asList(
                new Epsilon<BusSolution>(), new Spread<BusSolution>(), new GenerationalDistance<BusSolution>(),
                new PISAHypervolume<BusSolution>(),
                new InvertedGenerationalDistance<BusSolution>(), new InvertedGenerationalDistancePlus<BusSolution>()))
            .setIndependentRuns(INDEPENDENT_RUNS)
            .setNumberOfCores(1)
            .build();

    new ExecuteAlgorithms<>(experiment).run();
    //new GenerateReferenceParetoSetAndFrontFromDoubleSolutions(experiment).run();
    //new ComputeQualityIndicators<>(experiment).run() ;
    //new GenerateLatexTablesWithStatistics(experiment).run() ;
    //new GenerateWilcoxonTestTablesWithR<>(experiment).run() ;
    //new GenerateFriedmanTestTables<>(experiment).run();
    //new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).run() ;
  }

  /**
   * The algorithm list is composed of pairs {@link Algorithm} + {@link Problem} which form part of a
   * {@link TaggedAlgorithm}, which is a decorator for class {@link Algorithm}. The {@link TaggedAlgorithm}
   * has an optional tag component, that can be set as it is shown in this example, where four variants of a
   * same algorithm are defined.
   *
   * @param problemList
   * @return
   */
  static List<TaggedAlgorithm<List<BusSolution>>> configureAlgorithmList(
      List<Problem<BusSolution>> problemList,
      int independentRuns) {
    List<TaggedAlgorithm<List<BusSolution>>> algorithms = new ArrayList<>() ;

    for (int run = 0; run < independentRuns; run++) {
      for (int i = 0; i < problemList.size(); i++) {
        Algorithm<List<BusSolution>> algorithm = new NSGAIIBuilder<>(problemList.get(i), new BusProblemCrossover(0.75),
            new BusProblemMutation(0.01))
            .setMaxEvaluations(25000)
            .setPopulationSize(100)
            .build();
        algorithms.add(new TaggedAlgorithm<List<BusSolution>>(algorithm, "BusProblemA", problemList.get(i), run));
      }

/*      for (int i = 0; i < problemList.size(); i++) {
        Algorithm<List<BusSolution>> algorithm = new NSGAIIBuilder<>(problemList.get(i), new BusProblemCrossover(0.75),
            new BusProblemMutation(0.05))
            .setMaxEvaluations(25000)
            .setPopulationSize(100)
            .build();
        algorithms.add(new TaggedAlgorithm<List<BusSolution>>(algorithm, "BusProblemB", problemList.get(i), run));
      }

      for (int i = 0; i < problemList.size(); i++) {
        Algorithm<List<BusSolution>> algorithm = new NSGAIIBuilder<>(problemList.get(i), new BusProblemCrossover(0.60),
            new BusProblemMutation(0.01))
            .setMaxEvaluations(25000)
            .setPopulationSize(100)
            .build();
        algorithms.add(new TaggedAlgorithm<List<BusSolution>>(algorithm, "BusProblemC", problemList.get(i), run));
      }

      for (int i = 0; i < problemList.size(); i++) {
        Algorithm<List<BusSolution>> algorithm = new NSGAIIBuilder<>(problemList.get(i), new BusProblemCrossover(0.60),
            new BusProblemMutation(0.005))
            .setMaxEvaluations(25000)
            .setPopulationSize(100)
            .build();
        algorithms.add(new TaggedAlgorithm<List<BusSolution>>(algorithm, "BusProblemD", problemList.get(i), run));
      }*/
    }
    return algorithms ;
  }
}