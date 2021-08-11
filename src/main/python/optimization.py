import random
from typing import List

import pyswarms as ps
import numpy as np

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

    def getGdp(self):
        # We want to maximize this.
        a = Analysis(f"opti/{self.id}")
        return -a.DF_STATES["Gdp"][0]  # Minus because we will try to minimize the fitness

    def getGini(self):
        # We want to minimize this.
        a = Analysis(f"opti/{self.id}")
        return a.gini(a.DF_AGENTS)

    def getFitness(self):
        return self.getGdp()


def func(particles):
    # Launch all configurations
    Config.resetAll()
    solutions: List[Solution] = []
    for i, params in enumerate(particles):
        s = Solution(i, params)
        solutions.append(s)
    Config.run()  # Run all in parallel
    # Get fitness of each configuration
    res = np.zeros(particles.shape[0])
    for s in solutions:
        res[s.id] = s.getFitness()
    print("\nGini:", res)
    return res


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

