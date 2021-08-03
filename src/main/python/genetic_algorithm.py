import random
from typing import List

from config import Config


class GeneticAlgorithm:
    def __init__(self):
        self.NB_PARAMS: int = 6
        self.population: List[Config] = []

    def updateConfig(self, config: Config, params: List[float]) -> Config:
        """
        Take a list of parameters that will be set to the config file
        :param config: Configuration to update with the new params
        :param params: Format: [VAT, Levy, Tariff, WealthTax, Unemployment, Black]
        """
        assert len(params) == self.NB_PARAMS
        for elem in params:
            assert 0 <= elem <= 1
        config.setVat(params[0])
        config.setLevy(params[1])
        config.setTariff(params[2])
        config.setWealth(params[3])
        config.setUnemployment(params[4])
        config.setBlack(params[5])
        config.save()
        return config

    def initialize(self, popSize):
        """
        Initialize the population of Configs randomly
        """
        Config.resetAll()
        for i in range(popSize):
            c = Config("templateOptimization.json", f"{i}.json")
            params = [random.random() for _ in range(self.NB_PARAMS)]
            c = self.updateConfig(c, params)
            self.population.append(c)
        Config.run()

    def crossover(self):
        pass

    def mutation(self):
        pass

    def step(self):
        Config.resetAll()

    def run(self, popSize, nbSteps):
        """
        Run the Genetic Algorithm Optimization
        :param popSize: Size of the population (number of individuals/Configs)
        :param nbSteps: Number of steps the algorithm will perform
        """
        self.initialize(popSize)
        for _ in range(nbSteps):
            self.step()


def geneticAlgorithm():
    ga = GeneticAlgorithm()
    ga.run(3, 10)
