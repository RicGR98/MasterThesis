from analysis import Analysis
from config import Config

DEFAULT_CONFIG_FILE = "small.json"


def exp1():
    """
    Experiment 1:
    Influence of the number of connected States on multiple metrics.
    In a Cluster, all States are connected to one another. It may not
    be the case for States connected randomly according to probConnection.
    E.g.:
        Cluster: A, B, C are all connected
        Non-Cluster: A connected to B, B connected to C, but C not connected to A
    """
    name = "exp1"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setProbConnection(0.3)
    c.setClusterSize(5)  # 5 connected states, thus number of connected States = 4
    c.save()
    c.run()
    a = Analysis(name)
    print(a.DF_STATES)
    a.barChart(a.DF_STATES, "NbConnectedStates", "PopTotalMoney", "NbTransactions")
    a.barChart(a.DF_STATES, "NbConnectedStates", "Gdp", "Gini")
    a.barChart(a.DF_STATES, "NbConnectedStates", "StateMoney")


def exp2():
    """
    Experiment 2:
    Influence of the Agent's talent on two metrics:
    - Sales (number of products it sells)
    - Purchases (number of products it purchases)
    """
    name = "exp2"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.save()
    c.run()
    a = Analysis(name)
    print(a.DF_AGENTS_PRODUCTS)
    a.linePointsChart(a.DF_AGENTS_PRODUCTS, "Talent", "Sales", "Purchases")


def exp3():
    """
    Experiment 3:
    Influence of the Unemployment rate on multiple metrics.
    """
    name = "exp3"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setMinMaxUnemployment(0, 1)
    c.save()
    c.run()
    a = Analysis(name)
    print(a.DF_AGENTS_PRODUCTS)
    a.linePointsChart(a.DF_STATES, "Unemployment", "PopTotalMoney", "NbTransactions")
    a.linePointsChart(a.DF_STATES, "Unemployment", "Gdp", "Gini")
    a.linePointsChart(a.DF_STATES, "Unemployment", "StateMoney")
    a.barChart(a.DF_AGENTS_PRODUCTS, "IsProducer", "Sales", "Purchases")


def runExperiments():
    # exp1()
    # exp2()
    exp3()

# a = Analysis()
# # a.influenceOfParamOnResults(a.DF_STATES, "VAT", "Money", "NbTransactions")
# # a.influenceOfParamOnResults(a.DF_STATES, "WealthTax", "Money", "Gini")
# # a.influenceOfParamOnResults(a.DF_STATES, "VAT", "Gdp", "NbTransactions")
# a.influenceOfParamBar(a.DF_STATES, "Allowance", "Gini")


if __name__ == '__main__':
    runExperiments()
