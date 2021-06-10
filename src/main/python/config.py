import json
import shlex
import subprocess
from pathlib import Path


DIR_PARAMS = "params/"


class Config:
    """
    Handle Json config file (update, save, run)
    """
    def __init__(self, filename: str):
        self.filename = filename
        self.file = DIR_PARAMS + self.filename
        with open(self.file, "r") as jsonFile:
            self.data = json.load(jsonFile)

    def __getitem__(self, item):
        return self.data[item]

    def save(self):
        """
        Save modifications to json file
        """
        with open(self.file, "w") as jsonFile:
            json.dump(self.data, jsonFile, indent=4)

    def addToAll(self):
        """
        Add current file to the all.txt file specifying the json files to be run
        """
        f = open(DIR_PARAMS + "all.txt", "w")
        f.write(self.filename)
        f.close()

    def run(self):
        """
        Run configuration
        """
        self.save()
        self.addToAll()
        home = str(Path.home())
        cmd = f"{home}/.local/bin/apache-maven-3.8.1/bin/mvn exec:java -Dexec.mainClass='rgomesro.Main' -Dexec.cleanupDaemonThreads=false "
        args = shlex.split(cmd)
        result = subprocess.check_call(args)
        if result != 0:
            raise Exception("Error while running the Java simulation")
