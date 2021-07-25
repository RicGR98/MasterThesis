from analysis import Analysis
from config import Config

DEFAULT_CONFIG_FILE = "small.json"


def experiment1():
    """"""
    filename = "exp1"
    c = Config(DEFAULT_CONFIG_FILE, f"{filename}.json")
    c.setNbTicks(1000)
    c.setMinMaxVat(0, 1)
    c.save()
    c.run()
    a = Analysis(filename)
    a.influenceOfParamOnResults(a.DF_STATES, "VAT", "Money", "NbTransactions")


def runExperiments():
    experiment1()

# a = Analysis()
# a.influenceOfParamOnResults(a.DF_AGENTS_PRODUCTS, "Talent", "Sales", scatter=False)
# # a.influenceOfParamOnResults(a.DF_STATES, "VAT", "Money", "NbTransactions")
# # a.influenceOfParamOnResults(a.DF_STATES, "WealthTax", "Money", "Gini")
# # a.influenceOfParamOnResults(a.DF_STATES, "VAT", "Gdp", "NbTransactions")
# a.influenceOfParamBar(a.DF_STATES, "NbConnectedStates", "Gini")
# a.influenceOfParamBar(a.DF_STATES, "Allowance", "Gini")


if __name__ == '__main__':
    runExperiments()
