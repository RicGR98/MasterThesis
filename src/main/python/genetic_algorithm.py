import random
from typing import List
from pathlib import Path

from config import Config
from analysis import Analysis


class Solution:
    def __init__(self, _id):
        """
        Initialize random parameters
        :param _id: Id of the solution
        """
        self.NB_PARAMS = 6
        self.id = _id
        self.params: List[float] = [random.random() for _ in range(self.NB_PARAMS)]
        self.config: Config = Config("templateOptimization.json", f"opti/{self.id}.json")
        self.updateConfig(self.params)
        self.fitness: float = 0

    def __eq__(self, other):
        return self.fitness == other.fitness

    def __lt__(self, other):
        return self.fitness < other.fitness

    def __str__(self):
        return f"{self.id} -> {self.fitness}"

    def __repr__(self):
        return self.__str__()

    def updateConfig(self, params: List[float]):
        """
        Take a list of parameters that will be set to the config file
        :param params: Format: [VAT, Levy, Tariff, WealthTax, Unemployment, Black]
        """
        assert len(params) == self.NB_PARAMS
        for elem in params:
            assert 0 <= elem <= 1
        self.config.setVat(params[0])
        self.config.setLevy(params[1])
        self.config.setTariff(params[2])
        self.config.setWealth(params[3])
        self.config.setUnemployment(params[4])
        self.config.setBlack(params[5])
        self.config.save()

    def updateFitness(self):
        a = Analysis(f"opti/{self.id}")
        self.fitness = a.DF_STATES["Gini"][0]


class GeneticAlgorithm:
    def __init__(self):
        self.population: List[Solution] = []

    def initialize(self, popSize):
        """
        Initialize the population of Solutions randomly
        :param popSize: Population size
        """
        Config.resetAll()
        for i in range(popSize):
            self.population.append(Solution(i))

    def fitness(self):
        for solution in self.population:
            solution.updateFitness()

    def selection(self):
        pass

    def crossover(self):
        pass

    def mutation(self):
        pass

    def step(self):
        """
        Single step in the Genetic Algorithm
        """
        Config.run()
        self.fitness()
        self.selection()
        self.crossover()
        self.mutation()

    def run(self, popSize, nbSteps):
        """
        Run the Genetic Algorithm Optimization
        :param popSize: Size of the population (number of individuals/Configs)
        :param nbSteps: Number of steps the algorithm will perform
        """
        Config.resetAll()
        self.initialize(popSize)
        for i in range(nbSteps):
            self.step()
            print(sorted(self.population))
            print(f"Step {i} finished")
        Config.run()
        self.fitness()
        print(sorted(self.population))
        print(f"Optimization finished")


def geneticAlgorithm():
    Path("params/opti").mkdir(parents=True, exist_ok=True)
    Path("res/csv/agents/opti").mkdir(parents=True, exist_ok=True)
    Path("res/csv/states/opti").mkdir(parents=True, exist_ok=True)
    Path("res/csv/products/opti").mkdir(parents=True, exist_ok=True)
    Path("res/csv/ticks/opti").mkdir(parents=True, exist_ok=True)
    ga = GeneticAlgorithm()
    ga.run(3, 3)
