import json
import shlex
import subprocess
from pathlib import Path

DIR_PARAMS = "params/"


class Config:
    """
    Handle Json config file (update, save, run)
    """

    def __init__(self, inputFilename, outputFilename):
        self.resetAll()
        self.outputFilename = outputFilename
        self.file = DIR_PARAMS + inputFilename
        with open(self.file, "r") as jsonFile:
            self.data = json.load(jsonFile)

    def __getitem__(self, item):
        return self.data[item]

    def set(self, values: dict):
        if "NB_STATES" in values:
            self.data["World"]["NB_STATES"] = values["NB_STATES"]
        if "NB_AGENTS" in values:
            self.data["World"]["NB_AGENTS"] = values["NB_AGENTS"]
        if "NB_TICKS" in values:
            self.data["World"]["NB_TICKS"] = values["NB_TICKS"]
        if "CLUSTER_SIZE" in values:
            self.data["Connections"]["CLUSTER_SIZE"] = values["CLUSTER_SIZE"]
        if "PROB_CONNECTION" in values:
            self.data["Connections"]["PROB_CONNECTION"] = float(values["PROB_CONNECTION"])
        if "MIN_VAT" in values:
            self.data["State"]["Tax"]["MIN_VAT"] = float(values["MIN_VAT"])
        if "MAX_VAT" in values:
            self.data["State"]["Tax"]["MAX_VAT"] = float(values["MAX_VAT"])
        if "MIN_LEVY" in values:
            self.data["State"]["Tax"]["MIN_LEVY"] = float(values["MIN_LEVY"])
        if "MAX_LEVY" in values:
            self.data["State"]["Tax"]["MAX_LEVY"] = float(values["MAX_LEVY"])
        if "MIN_TARIFF" in values:
            self.data["State"]["Tax"]["MIN_TARIFF"] = float(values["MIN_TARIFF"])
        if "MAX_TARIFF" in values:
            self.data["State"]["Tax"]["MAX_TARIFF"] = float(values["MAX_TARIFF"])
        if "MIN_WEALTH" in values:
            self.data["State"]["Tax"]["MIN_WEALTH_TAX_VALUE"] = float(values["MIN_WEALTH"])
        if "MAX_WEALTH" in values:
            self.data["State"]["Tax"]["MAX_WEALTH_TAX_VALUE"] = float(values["MAX_WEALTH"])

    def setNbStates(self, value: int):
        """Number of States in a World"""
        self.set({"NB_STATES": value})

    def setNbAgents(self, value: int):
        """Number of Agents in a World"""
        self.set({"NB_AGENTS": value})

    def setNbTicks(self, value: int):
        """Number of Ticks in a World"""
        self.set({"NB_TICKS": value})

    def setClusterSize(self, value: int):
        """Size of the (unique) Cluster of States in the World where
        every State in this Cluster is connected to all others"""
        self.set({"CLUSTER_SIZE": value})

    def setProbConnection(self, value: float):
        """Probability of connection between two States (which do
        not belong to the Cluster if present)"""
        assert 0 <= value <= 1
        self.set({"PROB_CONNECTION": value})

    def setMinVat(self, value: float):
        """Minimum value for the VAT"""
        assert 0 <= value <= 1
        self.set({"MIN_VAT": value})

    def setMaxVat(self, value: float):
        """Maximum value for the VAT"""
        assert 0 <= value <= 1
        self.set({"MAX_VAT": value})

    def setMinMaxVat(self, value1: float, value2: float):
        """Set min and max values for the VAT"""
        self.setMinVat(value1)
        self.setMaxVat(value2)

    def setVat(self, value: float):
        """Set value for the VAT"""
        self.setMinVat(value)
        self.setMaxVat(value)

    def setMinLevy(self, value: float):
        """Minimum value for the Levy"""
        assert 0 <= value <= 1
        self.set({"MIN_LEVY": value})

    def setMaxLevy(self, value: float):
        """Maximum value for the Levy"""
        assert 0 <= value <= 1
        self.set({"MAX_LEVY": value})

    def setMinMaxLevy(self, value1: float, value2: float):
        """Set min and max values for the Levy"""
        self.setMinLevy(value1)
        self.setMaxLevy(value2)

    def setLevy(self, value: float):
        """Set value for the Levy"""
        self.setMinLevy(value)
        self.setMaxLevy(value)

    def setMinTariff(self, value: float):
        """Minimum value for the Tariff"""
        assert 0 <= value <= 1
        self.set({"MIN_TARIFF": value})

    def setMaxTariff(self, value: float):
        """Maximum value for the Tariff"""
        assert 0 <= value <= 1
        self.set({"MAX_TARIFF": value})

    def setMinMaxTariff(self, value1: float, value2: float):
        """Set min and max values for the Tariff"""
        self.setMinTariff(value1)
        self.setMaxTariff(value2)

    def setTariff(self, value: float):
        """Set value for the Tariff"""
        self.setMinTariff(value)
        self.setMaxTariff(value)

    def setMinWealth(self, value: float):
        """Minimum value for the Wealth tax"""
        assert 0 <= value <= 1
        self.set({"MIN_WEALTH": value})

    def setMaxWealth(self, value: float):
        """Maximum value for the Wealth tax"""
        assert 0 <= value <= 1
        self.set({"MAX_WEALTH": value})

    def setMinMaxWealth(self, value1: float, value2: float):
        """Set min and max values for the Wealth tax"""
        self.setMinWealth(value1)
        self.setMaxWealth(value2)

    def setWealth(self, value: float):
        """Set value for the Wealth tax"""
        self.setMinWealth(value)
        self.setMaxWealth(value)

    def save(self):
        """
        Save modifications to json file
        """
        with open(DIR_PARAMS + self.outputFilename, "w") as jsonFile:
            json.dump(self.data, jsonFile, indent=4)
        self.addToAll()

    def addToAll(self):
        """
        Add current file to the all.txt file specifying the json files to be run
        """
        f = open(DIR_PARAMS + "all.txt", "a")
        f.write(self.outputFilename + "\n")
        f.close()

    @staticmethod
    def resetAll():
        open("params/all.txt", 'w').close()  # Empty file with all configs

    @staticmethod
    def run():
        """
        Run configuration
        """
        home = str(Path.home())
        cmd = f"{home}/.local/bin/apache-maven-3.8.1/bin/mvn exec:java -Dexec.mainClass='rgomesro.Main' -Dexec.cleanupDaemonThreads=false "
        args = shlex.split(cmd)
        result = subprocess.check_call(args)
        if result != 0:
            raise Exception("Error while running the Java simulation")