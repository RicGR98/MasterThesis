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
    print(a.DF_STATES)
    print(a.DF_AGENTS_PRODUCTS)
    a.linePointsChart(a.DF_STATES, "Unemployment", "PopTotalMoney", "NbTransactions")
    a.linePointsChart(a.DF_STATES, "Unemployment", "Gdp", "Gini")
    a.linePointsChart(a.DF_STATES, "Unemployment", "StateMoney")
    a.barChart(a.DF_AGENTS_PRODUCTS, "IsProducer", "Sales", "Purchases")


def exp4():
    """
    Experiment 4:
    Study of how a non-producer Agent can make purchases
    """
    name1 = "exp4_1"
    name2 = "exp4_2"
    c1 = Config(DEFAULT_CONFIG_FILE, f"{name1}.json")
    c1.setNbStates(1)
    c1.setNbAgents(1000)
    c1.setNbTicks(1000)
    c1.setUnemployment(0.3)
    c1.save()

    c2 = Config(f"{name1}.json", f"{name2}.json")
    c2.setNbTicks(c1.getNbTicks() * 10)
    c2.save()

    Config.run()  # Run both configs in parallel

    a1 = Analysis(name1)
    a1.barChart(a1.DF_AGENTS_PRODUCTS, "IsProducer", "Purchases")

    a2 = Analysis(name2)
    a2.barChart(a2.DF_AGENTS_PRODUCTS, "IsProducer", "Purchases")


def exp5():
    """
    Experiment 5:
    Influence of the SellingPrice on two metrics.
    """
    name = "exp5"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.save()
    c.run()
    a = Analysis(name)
    print(a.DF_AGENTS_PRODUCTS)
    a.linePointsChart(a.DF_AGENTS_PRODUCTS, "SellingPrice", "Stock", "Sales")


def exp6():
    """
    Experiment 6:
    Influence of the Initial money of an Agent.
    """
    name = "exp6"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setMinMaxInitMoney(0, 10000)
    c.save()
    c.run()
    a = Analysis(name)
    print(a.DF_AGENTS_PRODUCTS)
    a.linePointsChart(a.DF_AGENTS_PRODUCTS, "AgentInitMoney", "Sales", "Purchases")
    a.linePointsChart(a.DF_AGENTS_PRODUCTS, "AgentInitMoney", "Stock")


def exp7():
    """
    Experiment 7:
    Influence of the Black economy share on multiple metrics.
    """
    name = "exp7"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setMinMaxBlack(0, 1)
    c.save()
    c.run()
    a = Analysis(name)
    print(a.DF_STATES)
    a.linePointsChart(a.DF_STATES, "Black", "PopTotalMoney", "NbTransactions")
    a.linePointsChart(a.DF_STATES, "Black", "Gdp", "Gini")
    a.linePointsChart(a.DF_STATES, "Black", "StateMoney")


def exp8():
    """
    Experiment 8:
    Evolution of multiple metrics with time (ticks)
    Re-run this experiment but now, in Product, change the production price to
    something positive and notice the huge difference
    """
    name = "exp8"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setNbTicks(5000)
    c.save()
    c.run()
    a = Analysis(name)
    print(a.DF_TICKS)
    print(a.DF_STATES)
    a.linePointsChart(a.DF_TICKS, "Tick", "WorldNbTransactions", "WorldGdp")
    a.pointsChart(a.DF_TICKS, "Tick", "WorldStatesMoney", "WorldAgentsMoney")


def exp9():
    """
    Experiment 9:
    Influence of the Allowance type on multiple metrics.
    """
    name = "exp9"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.save()
    c.run()
    a = Analysis(name)
    print(a.DF_STATES)
    a.barChart(a.DF_STATES, "Allowance", "Gdp", "NbTransactions")
    a.barChart(a.DF_STATES, "Allowance", "PopTotalMoney", "Gini")


def exp10():
    """
    Experiment 10:
    Influence of the choosing method (cheapest, random, weighted random)
    of a Product from the list of matching Products
    """
    name1 = "exp10_1"
    c1 = Config(DEFAULT_CONFIG_FILE, f"{name1}.json")
    c1.setProductChoiceCheapest()
    c1.save()

    name2 = "exp10_2"
    c2 = Config(DEFAULT_CONFIG_FILE, f"{name2}.json")
    c2.setProductChoiceRandom()
    c2.save()

    name3 = "exp10_3"
    c3 = Config(DEFAULT_CONFIG_FILE, f"{name3}.json")
    c3.setProductChoiceWeightedRandom()
    c3.save()

    Config.run()  # Run all configs in parallel

    a1 = Analysis(name1)
    a1.linePointsChart(a1.DF_AGENTS_PRODUCTS, "SellingPrice", "Stock", "Sales")

    a2 = Analysis(name2)
    a2.linePointsChart(a2.DF_AGENTS_PRODUCTS, "SellingPrice", "Stock", "Sales")

    a3 = Analysis(name3)
    a3.linePointsChart(a3.DF_AGENTS_PRODUCTS, "SellingPrice", "Stock", "Sales")


def exp11():
    """
    Experiment 11:
    Influence of the VAT on multiple metrics
    """
    name = "exp11"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setMinMaxVat(0, 1)
    c.save()
    c.run()
    a = Analysis(name)
    print(a.DF_STATES)
    a.linePointsChart(a.DF_STATES, "VAT", "PopTotalMoney", "NbTransactions")
    a.linePointsChart(a.DF_STATES, "VAT", "Gdp", "Gini")
    a.linePointsChart(a.DF_STATES, "VAT", "StateMoney")


def exp12():
    """
    Experiment 12:
    Influence of the Levy tax on multiple metrics
    """
    name = "exp12"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setMinMaxLevy(0, 1)
    c.save()
    c.run()
    a = Analysis(name)
    print(a.DF_STATES)
    a.linePointsChart(a.DF_STATES, "Levy", "PopTotalMoney", "NbTransactions")
    a.linePointsChart(a.DF_STATES, "Levy", "Gdp", "Gini")
    a.linePointsChart(a.DF_STATES, "Levy", "StateMoney")


def runExperiments():
    experiments = [
        # exp1,
        # exp2,
        # exp3,
        # exp4,
        # exp5,
        # exp6,
        # exp7,
        # exp8,
        # exp9,
        # exp10,
        # exp11,
        exp12,
    ]
    for exp in experiments:
        Config.resetAll()
        exp()


if __name__ == '__main__':
    runExperiments()
