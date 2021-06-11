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
        self.outputFilename = outputFilename
        self.file = DIR_PARAMS + inputFilename
        with open(self.file, "r") as jsonFile:
            self.data = json.load(jsonFile)

    def __getitem__(self, item):
        return self.data[item]

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
