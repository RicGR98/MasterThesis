from copy import deepcopy
from typing import List, Dict
import random
from config import Config
from analysis import Analysis

VAT = "VAT"
LEVY = "LEVY"
TARIFF = "TARIFF"
WEALTH = "WEALTH"
UNEMPLOYMENT = "UNEMPLOYMENT"
BLACK = "BLACK"
VARIABLES_LIST: List[str] = [VAT, LEVY, TARIFF, WEALTH, UNEMPLOYMENT, BLACK]


class Solution:
    NB_PARAMS = 6

    def __init__(self, _id, params = None):
        self.id = _id
        self.params = params
        if self.params is None:
            self.params = [random.random() for _ in range(self.NB_PARAMS)]
        self.config: Config = Config("templateOptimization.json", f"opti/{self.id}.json")
        self.updateConfig(self.params)
        self.fitness = float("-inf")

    def updateConfig(self, params: List[float]):
        """
        Take a list of parameters that will be set to the config file
        :param params: Format: [VAT, Levy, Tariff, WealthTax, Unemployment, Black]
        """
        assert len(params) == Solution.NB_PARAMS
        self.params = params
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
        return -a.DF_STATES["NbTransactions"][0]  # Minus because we will try to minimize the fitness

    def getFitness(self):
        return self.fitness

    def updateFitness(self, fitness):
        self.fitness = fitness

    def __str__(self):
        return f"{self.fitness} {self.params}"

    def __repr__(self):
        return self.__str__()


class Particle:
    def __init__(self, id_: int, inertia: float, phi1: float, phi2: float):
        self.id = id_

        self.inertia: float = inertia
        self.phi1: float = phi1
        self.phi2: float = phi2
        self.velocities: Dict[str, float] = [0.0 for _ in range(Solution.NB_PARAMS)]

        self.current: Solution = Solution(id_)
        self.personalBest: Solution = deepcopy(self.current)
        self.globalBest: Solution = deepcopy(self.current)

        self.neighbours: List[Particle] = []

    def addNeighbour(self, neighbour: 'Particle'):
        self.neighbours.append(neighbour)

    def updateInertia(self, value: float):
        self.inertia = value

    def updateGlobalBest(self, bestParticle: 'Solution'):
        self.globalBest.updateConfig(bestParticle.params)

    def findGlobalBestParticle(self):
        if self.personalBest.getFitness() < self.globalBest.getFitness():
            self.updateGlobalBest(self.personalBest)
        for neighbour in self.neighbours:
            if neighbour.personalBest.getFitness() < self.globalBest.getFitness():
                self.updateGlobalBest(neighbour.personalBest)

    def move(self):
        self.findGlobalBestParticle()
        u1, u2 = random.random(), random.random()
        for i in range(Solution.NB_PARAMS):
            self.velocities[i] = self.inertia * self.velocities[i] \
                                        + self.phi1 * u1 * (self.personalBest.params[i] - self.current.params[i]) \
                                        + self.phi2 * u2 * (self.globalBest.params[i] - self.current.params[i])
            self.current.params[i] += self.velocities[i]
            if self.current.params[i] < 0:
                self.current.params[i] = 0
            elif self.current.params[i] > 1:
                self.current.params[i] = 1
        self.current.updateConfig(self.current.params)

    def evaluate(self):
        # Update personal best solution if it is the case
        if self.current.getFitness() < self.personalBest.getFitness():
            self.personalBest.updateConfig(self.current.params)
