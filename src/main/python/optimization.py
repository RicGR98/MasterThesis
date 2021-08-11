import random
from typing import List

import matplotlib.pyplot as plt
import pyswarms as ps
import numpy as np
from pyswarms.utils.plotters import (plot_cost_history, plot_contour, plot_surface)

from config import Config
from analysis import Analysis


class Solution:
    NB_PARAMS = 6

    def __init__(self, _id, params):
        """
        Initialize random parameters
        :param _id: Id of the solution
        """
        self.id = _id
        self.config: Config = Config("templateOptimization.json", f"opti/{self.id}.json")
        self.updateConfig(params)

    def updateConfig(self, params: List[float]):
        """
        Take a list of parameters that will be set to the config file
        :param params: Format: [VAT, Levy, Tariff, WealthTax, Unemployment, Black]
        """
        assert len(params) == Solution.NB_PARAMS
        self.config.setVat(params[0])
        self.config.setLevy(params[1])
        self.config.setTariff(params[2])
        self.config.setWealth(params[3])
        self.config.setUnemployment(params[4])
        self.config.setBlack(params[5])
        self.config.save()

    def getGini(self):
        # We want to minimize this.
        a = Analysis(f"opti/{self.id}")
        return a.gini(a.DF_AGENTS)

    def getGdp(self):
        # We want to maximize this.
        a = Analysis(f"opti/{self.id}")
        return -a.DF_STATES["Gdp"][0]  # Minus because we will try to minimize the fitness

    def getNbTransactions(self):
        # We want to maximize this.
        a = Analysis(f"opti/{self.id}")
        return -a.DF_STATES["NbTransactions"][0] # Minus because we will try to minimize the fitness

    def getFitness(self):
        return self.getNbTransactions()


def func(particles):
    """
    Function to optimize.
    We run all configurations (given by the particles) in parallel.
    We do this NB_RUNS times and get the average to have a better precision.
    :param particles: Numpy array of particles, each containing all params
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
            fitnesses[solution.id] += solution.getFitness()
    fitnesses /= NB_RUNS  # Average multiple runs
    return fitnesses


def optimize():
    bounds = (np.zeros(Solution.NB_PARAMS), np.ones(Solution.NB_PARAMS))
    options = {'c1': 0.5, 'c2': 0.3, 'w': 0.9}
    optimizer = ps.single.GlobalBestPSO(
        n_particles=20,
        dimensions=Solution.NB_PARAMS,
        options=options,
        bounds=bounds)
    cost, pos = optimizer.optimize(func, iters=20)
    print(cost, pos)
    plot_cost_history(cost_history=optimizer.cost_history)
    plt.show()

