from typing import List

from config import Config


class GeneticAlgorithm:
    def __init__(self):
        self.population: List[Config] = []

    def initialize(self, popSize):
        for i in range(popSize):
            c = Config(f"small.json", f"{i}.json")
            # c["World"]["MIN_VAT"] = random.randint(10, 100)
            # c["World"]["MAX_VAT"] = random.randint(10, 100)
            c.save()
            self.population.append(c)
        Config.run()

    def run(self, popSize):
        self.initialize(popSize)


def geneticAlgorithm():
    ga = GeneticAlgorithm()
    ga.run(3)

