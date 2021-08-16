from pathlib import Path

import matplotlib.pyplot as plt
import pyswarms as ps
import numpy as np
from pyswarms.utils.plotters import plot_cost_history
from scipy.stats import ranksums

from config import Config

from pso import PSO, Fitness
from particle import Solution


def optimize():
    for fitnessMetric in [Fitness.GINI, Fitness.GDP, Fitness.NB_TRANSACTIONS]:
        print(fitnessMetric, flush=True)
        optimizeLibrary(fitnessMetric)


def func(particles, fitnessMetric):
    """
    Function to optimize.
    We run all configurations (given by the particles) in parallel.
    We do this NB_RUNS times and get the average to have a better precision.
    :param particles: Numpy array of particles, each containing all params
    :param fitnessMetric: which metric we should use as Fitness
    :return: Fitnesses of all particles
    """
    fitnesses = np.zeros(particles.shape[0])
    NB_RUNS = 1
    for _ in range(NB_RUNS):
        # Launch all configurations
        Config.resetAll()
        solutions = []
        for _id, params in enumerate(particles):
            solution = Solution(_id, params)
            solutions.append(solution)
        # Run all in parallel
        Config.run()
        # Get fitness of each configuration
        for solution in solutions:
            fitness = 0
            if fitnessMetric == Fitness.GINI:
                fitness = solution.getGini()
            elif fitnessMetric == Fitness.GDP:
                fitness = solution.getGdp()
            elif fitnessMetric == Fitness.NB_TRANSACTIONS:
                fitness = solution.getNbTransactions()
            fitnesses[solution.id] += fitness
    fitnesses /= NB_RUNS  # Average multiple runs
    return fitnesses


def optimizeLibrary(fitnessMetric):
    """Optimize using the PySwarms library (more efficient)"""
    bounds = (np.zeros(Solution.NB_PARAMS), np.ones(Solution.NB_PARAMS))
    options = {'c1': 0.5, 'c2': 0.3, 'w': 0.9}
    Path(f'res/img/opti/{fitnessMetric}').mkdir(parents=True, exist_ok=True)
    for i in range(10):
        optimizer = ps.single.GlobalBestPSO(
            n_particles=50,
            dimensions=Solution.NB_PARAMS,
            options=options,
            bounds=bounds)
        cost, pos = optimizer.optimize(func, iters=50, fitnessMetric=fitnessMetric, verbose=False)
        print(cost, pos, flush=True)
        plot_cost_history(cost_history=optimizer.cost_history)
        plt.savefig(f'res/img/opti/{fitnessMetric}/{i}.png')


def optimizeOwnPSO(fitnessMetric):
    """Optimize using my own version of PSO (a bit less efficient)"""
    for i in range(10):
        pso = PSO(50, 50, 0.5, 0.3, fitnessMetric)
        print(pso.run())


def parse(text):
    """
    Parse results computed by the cluster into a list of list of params
    """
    results = []
    text = text.split("\n")
    for lineNumber, line in enumerate(text):
        cost, params = line.split(" [")
        params = params.replace("  ", " ")
        params = params.replace("[", "")
        params = params.replace("]", "")
        params = params.strip()
        params = params.split(" ")
        results.append([])
        for i in range(len(params)):
            results[lineNumber].append(float(params[i]))
    return results


def pairedStatisticalTest(text):
    """Perform a Wilcoxon rank-sums test for each pair of params"""
    lst = parse(text)
    pvaluesMatrix = np.zeros((len(lst), len(lst)))
    for i in range(len(lst)):
        for j in range(i+1):
            pvaluesMatrix[i][j] = round(ranksums(lst[i], lst[j])[1], 3)  # Keep p-value only
            pvaluesMatrix[j][i] = pvaluesMatrix[i][j]
    print(pvaluesMatrix)


def runStatisticalTests():
    """Run all statistical tests with the results obtained on the Cluster"""
    giniResults = """0.0 [0.78296694 0.99881254 0.99959293 0.20902644 0.33317086 0.20051856]
    0.0 [0.30144084 0.99860142 0.37065729 0.68155468 0.37714027 0.590301 ]
    0.0 [0.01772531 0.99829416 0.75250993 0.43391462 0.99807285 0.82774047]
    0.0 [0.32314067 0.99809475 0.25458039 0.10116164 0.99859338 0.06262362]
    0.0 [0.91511962 0.99904147 0.45782447 0.65013515 0.52505633 0.65631024]
    0.0 [0.97764115 0.99947836 0.80763019 0.94576495 0.02214306 0.53298088]
    0.0 [0.21536561 0.75664012 0.78444353 0.14184332 0.99852087 0.29912114]
    0.0 [0.61758812 0.9988726  0.33361776 0.65789626 0.65659407 0.63287918]
    0.0 [0.43700863 0.99980639 0.73081392 0.96514933 0.8359512  0.53215895]
    0.0 [0.53806149 0.99923635 0.76308956 0.55352696 0.44458342 0.46110084]"""

    gdpResults = """36 739 [0.043 0.729 0.376 0.769 0.309 0.036]
    37 152 [0.054 0.651 0.272 0.988 0.175 0.018]
    36 749 [0.059 0.945 0.528 0.021 0.337 0.034]
    37 691 [0.005 0.994 0.675 0.66 0.268 0.052]
    36 334 [0.0 0.846 0.537 0.833 0.387 0.014]
    36 453 [0.001 0.819 0.443 0.209 0.077 0.081]
    36 703 [0.043 0.91 0.505 0.285 0.13 0.006]
    35 130 [0.025 0.47 0.525 0.51 0.285 0.082]
    35 556 [0.198 0.944 0.37 0.957 0.423 0.028]
    36 339 [0.109 0.924 0.519 0.426 0.096 0.047]"""

    nbTransactionsResults = """-739.232 [0.45644013 0.82414527 0.48717931 0.55620089 0.02098815 0.95419875]
    -788.491 [0.74144255 0.87123517 0.17963736 0.46206969 0.06325276 0.99617088]
    -753.234 [0.70800585 0.90566502 0.93928871 0.88358684 0.04059233 0.99260746]
    -737.609 [0.41875696 0.69149552 0.13727464 0.55063112 0.00475353 0.82611749]
    -726.233 [0.0913617  0.92423802 0.90412399 0.17531116 0.04438932 0.71443348]
    -752.394 [0.41169195 0.75873572 0.55423928 0.6708929  0.00426397 0.91694267]
    -747.217 [0.60810094 0.97423309 0.58792603 0.74256649 0.0051371  0.95702863]
    -725.796 [0.30598029 0.6817043  0.55040464 0.79405084 0.06180396 0.96167136]
    -740.271 [0.96286245 0.85767502 0.32298138 0.47168047 0.01842042 0.99444171]
    -746.258 [6.71520271e-01 4.78867346e-01 7.33314493e-02 8.09039459e-01 5.81886721e-04 9.53616374e-01]"""

    print("Statistical test ====> Gini")
    pairedStatisticalTest(giniResults)
    print("Statistical test ====> GDP")
    pairedStatisticalTest(gdpResults)
    print("Statistical test ====> Number of transactions")
    pairedStatisticalTest(nbTransactionsResults)