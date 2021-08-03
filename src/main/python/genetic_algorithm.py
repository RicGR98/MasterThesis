import random
from typing import List
from pathlib import Path

from config import Config


class GeneticAlgorithm:
    def __init__(self):
        self.NB_PARAMS: int = 6
        self.population: List[Config] = []
        self.popSize: int = 0

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

    def initialize(self):
        """
        Initialize the population of Configs randomly
        """
        Config.resetAll()
        for i in range(self.popSize):
            config = Config("templateOptimization.json", f"opti/{i}.json")
            params = [random.random() for _ in range(self.NB_PARAMS)]
            config = self.updateConfig(config, params)
            self.population.append(config)

    def fitness(self, config: Config):
        print(config.outputFilename)

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
        Config.resetAll()
        self.fitness(self.population[0])
        self.selection()
        self.crossover()
        self.mutation()
        Config.run()

    def run(self, popSize, nbSteps):
        """
        Run the Genetic Algorithm Optimization
        :param popSize: Size of the population (number of individuals/Configs)
        :param nbSteps: Number of steps the algorithm will perform
        """
        self.popSize = popSize
        self.initialize()
        Config.run()
        for _ in range(nbSteps):
            self.step()


def geneticAlgorithm():
    Path("params/opti").mkdir(parents=True, exist_ok=True)
    Path("res/csv/agents/opti").mkdir(parents=True, exist_ok=True)
    Path("res/csv/states/opti").mkdir(parents=True, exist_ok=True)
    Path("res/csv/products/opti").mkdir(parents=True, exist_ok=True)
    Path("res/csv/ticks/opti").mkdir(parents=True, exist_ok=True)
    ga = GeneticAlgorithm()
    ga.run(5, 1)
