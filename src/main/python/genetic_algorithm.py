import random
from typing import List

from config import Config


class GeneticAlgorithm:
    def __init__(self):
        self.population: List[Config] = []

    def initialize(self, popSize):
        for i in range(popSize):
            c = Config("small.json", f"{i}.json")
            c.set({"VAT": random.random(),
                   "LEVY": random.random(),
                   "TARIFF": random.random(),
                   "WEALTH": random.random()
                   })
            c.save()
            self.population.append(c)
        Config.run()

    def run(self, popSize):
        self.initialize(popSize)


def geneticAlgorithm():
    ga = GeneticAlgorithm()
    ga.run(3)
