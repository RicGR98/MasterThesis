import random
from typing import List
from pathlib import Path

from config import Config
from analysis import Analysis


class Solution:
    NB_PARAMS = 6

    def __init__(self, _id):
        """
        Initialize random parameters
        :param _id: Id of the solution
        """
        self.id = _id
        self.params: List[float] = [random.random() for _ in range(Solution.NB_PARAMS)]
        self.config: Config = Config("templateOptimization.json", f"opti/{self.id}.json")
        self.updateConfig(self.params)
        self.fitness: float = 0

    def __eq__(self, other):
        return self.fitness == other.fitness

    def __lt__(self, other):
        return self.fitness < other.fitness

    def __str__(self):
        params = ""
        for i in range(len(self.params)):
            params += str(round(self.params[i], 2))
            if i != len(self.params)-1:
                params += ", "
        return f"{self.id} -> {round(self.fitness, 5)} [{params}]"

    def __repr__(self):
        return self.__str__()

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

    def updateFitness(self):
        a = Analysis(f"opti/{self.id}")
        self.fitness = a.DF_STATES["Gdp"][0]


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

    def run(self):
        """
        Do multiple runs with the same configs and compute an average
        fitness for each Configuration
        """
        print("Run")
        NB_RUNS = 5
        avgFitnesses = [0 for _ in range(len(self.population))]
        for _ in range(NB_RUNS):
            Config.run()
            fitnesses = self.fitness()
            for i in range(len(self.population)):
                avgFitnesses[i] += fitnesses[i]
        for i in range(len(self.population)):
            self.population[i].fitness = avgFitnesses[i]/NB_RUNS

    def fitness(self) -> List[float]:
        """
        Update the fitness value of all Solutions in the population
        :return: List containing the fitness of each Solution
        """
        for solution in self.population:
            solution.updateFitness()
        return [solution.fitness for solution in self.population]

    def selection(self) -> List[Solution]:
        """
        Select the solutions that will become the parents for the
        next generation according to their fitness (smaller = better)
        :return: List of Solutions (parents) that will go under crossover
        """
        print("Selection")
        sortedPopulation: List[Solution] = sorted(self.population)  # From smallest fitness to biggest
        parents = [sortedPopulation[0], sortedPopulation[1]]  # Always keep the best two (elitism)

        while len(parents) <= len(self.population)//3:
            potentialParent = self.population[random.randint(0, len(self.population)-1)]
            prob = random.uniform(0, sortedPopulation[-1].fitness)  # Limits of fitness score to accept or not
            if prob >= potentialParent.fitness:
                parents.append(potentialParent)  # Accepted

        return parents

    def crossover(self, parents: List[Solution]):
        """
        Uniform crossover. We take, at random, two parents from the list
        of parents (p1 and p2) and for each Solution component, the
        offspring will either copy p1 or p2.
        :param parents: List of Solutions with the best fitnesses
        """
        print("Crossover")
        popSize = len(self.population)
        self.population.clear()
        for _id in range(popSize):  # Make sure we have the same population size as before
            parent1: Solution = random.choice(parents)
            parent2: Solution = random.choice(parents)
            params = []
            for paramIndex in range(Solution.NB_PARAMS):  # For each solution component
                if random.random() > 0.5:
                    params.append(parent1.params[paramIndex])
                else:
                    params.append(parent2.params[paramIndex])
            offspring = Solution(_id)
            offspring.updateConfig(params)
            self.population.append(offspring)

    def mutation(self):
        """
        Mutate, with a certain probability, the new offsprings.
        """
        print("Mutation")
        mutationProbability = 0.2
        for solution in self.population:
            if random.random() < mutationProbability:
                component = random.randint(0, len(solution.params)-1)
                solution.params[component] = random.random()

    def step(self):
        """
        Single step in the Genetic Algorithm
        """
        self.run()
        print(sorted(self.population))
        parents = self.selection()
        self.crossover(parents)
        self.mutation()

    def launch(self, popSize, nbSteps):
        """
        Run the Genetic Algorithm Optimization
        :param popSize: Size of the population (number of individuals/Configs)
        :param nbSteps: Number of steps the algorithm will perform
        """
        Config.resetAll()
        self.initialize(popSize)
        for i in range(nbSteps):
            self.step()
            print(f"Step {i} finished")
        self.run()
        print(sorted(self.population))
        print(f"Optimization finished")


def geneticAlgorithm():
    Path("params/opti").mkdir(parents=True, exist_ok=True)
    Path("res/csv/agents/opti").mkdir(parents=True, exist_ok=True)
    Path("res/csv/states/opti").mkdir(parents=True, exist_ok=True)
    Path("res/csv/products/opti").mkdir(parents=True, exist_ok=True)
    Path("res/csv/ticks/opti").mkdir(parents=True, exist_ok=True)
    ga = GeneticAlgorithm()
    ga.launch(popSize=10, nbSteps=3)
