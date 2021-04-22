import json

DIR_PARAMS = "params/"


class Params:
    """
    Update params of the json file and save them
    """
    def __init__(self, filename: str):
        self.filename = DIR_PARAMS + filename
        with open(self.filename, "r") as jsonFile:
            self.data = json.load(jsonFile)

    def __getitem__(self, item):
        return self.data[item]

    def save(self):
        """
        Save modifications to json file
        """
        with open(self.filename, "w") as jsonFile:
            json.dump(self.data, jsonFile, indent=4)
