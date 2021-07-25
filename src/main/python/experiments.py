from analysis import Analysis
from config import Config

DEFAULT_CONFIG_FILE = "small.json"


def experiment1():
    """"""
    filename = "exp1"
    c = Config(DEFAULT_CONFIG_FILE, f"{filename}.json")
    c.setNbTicks(1000)
    c.set({"MIN_VAT": 0.0, "MAX_VAT": 0.99})
    c.save()
    c.run()
    a = Analysis(filename)
    a.influenceOfParamOnResults(a.DF_STATES, "VAT", "Money", "NbTransactions")


def runExperiments():
    experiment1()


if __name__ == '__main__':
    runExperiments()
