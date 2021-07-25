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
            self.data["Connections"]["PROB_CONNECTION"] = values["PROB_CONNECTION"]
        if "VAT" in values:
            self.data["State"]["Tax"]["MIN_VAT"] = values["VAT"]
            self.data["State"]["Tax"]["MAX_VAT"] = values["VAT"]
        if "LEVY" in values:
            self.data["State"]["Tax"]["MIN_LEVY"] = values["LEVY"]
            self.data["State"]["Tax"]["MAX_LEVY"] = values["LEVY"]
        if "TARIFF" in values:
            self.data["State"]["Tax"]["MIN_TARIFF"] = values["TARIFF"]
            self.data["State"]["Tax"]["MAX_TARIFF"] = values["TARIFF"]
        if "WEALTH" in values:
            self.data["State"]["Tax"]["MIN_WEALTH_TAX_VALUE"] = values["WEALTH"]
            self.data["State"]["Tax"]["MAX_WEALTH_TAX_VALUE"] = values["WEALTH"]

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
