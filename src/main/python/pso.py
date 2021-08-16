from particle import *
import numpy as np


from enum import Enum


class Fitness(Enum):
    GINI = "Gini"
    GDP = "GDP"
    NB_TRANSACTIONS = "NbTransactions"


class PSO:
    def __init__(self, nbParticles: int, nbIter: int, phi1: float, phi2: float, fitnessMetric):
        self.inertia = 1    # Start with 1 and linearly decrease
        self.phi1 = phi1
        self.phi2 = phi2
        self.nbParticles = nbParticles
        self.nbIter = nbIter

        self.fitnessMetric = fitnessMetric

        self.particles: List[Particle] = []
        self.bestParticle: Particle = None
        self.globalBest: Solution = None

        self.createSwarm()

    def createSwarm(self):
        Config.resetAll()
        for i in range(self.nbParticles):
            p = Particle(i, self.inertia, self.phi1, self.phi2)
            self.particles.append(p)
        self.setNeighbourhood()
        self.bestParticle = self.particles[0]
        self.globalBest = self.bestParticle.personalBest

    def setNeighbourhood(self):
        """Fully connected"""
        for i in range(self.nbParticles):
            for j in range(self.nbParticles):
                if i != j:
                    self.particles[i].addNeighbour(self.particles[j])

    def updateGlobalBest(self, bestParticle: Particle):
        self.bestParticle = bestParticle
        self.globalBest.updateConfig(bestParticle.personalBest.params)
        self.globalBest.fitness = bestParticle.personalBest.getFitness()

    def moveSwarm(self):
        NB_RUNS = 1
        fitnesses = np.zeros(self.nbParticles)
        for _ in range(NB_RUNS):
            Config.run()
            for p in self.particles:
                fitness = 0
                if self.fitnessMetric == Fitness.GINI:
                    fitness = p.personalBest.getGini()
                elif self.fitnessMetric == Fitness.GDP:
                    fitness = p.personalBest.getGdp()
                elif self.fitnessMetric == Fitness.NB_TRANSACTIONS:
                    fitness = p.personalBest.getNbTransactions()
                fitnesses[p.id] += fitness
        fitnesses /= NB_RUNS
        for p in self.particles:
            p.personalBest.updateFitness(fitnesses[p.id])
            p.evaluate()
            if p.personalBest.getFitness() < self.globalBest.getFitness():
                self.updateGlobalBest(p)
        Config.resetAll()
        for p in self.particles:
            p.updateInertia(self.inertia)
            p.move()

    def run(self) -> Solution:
        for _ in range(self.nbIter):
            self.inertia -= 1/self.nbIter
            self.moveSwarm()
        return self.globalBest
