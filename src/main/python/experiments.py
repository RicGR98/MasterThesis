from pathlib import Path

from analysis import Analysis
from config import Config

DEFAULT_CONFIG_FILE = "templateExperiments.json"

ANALYZE = False
RUN = False


################################
# State parameters experiences #
################################
def exp0():
    """
    Experiment 0:
    What if the State collects no taxes ?
    """
    name1 = "exp/0_1"
    c1 = Config(DEFAULT_CONFIG_FILE, f"{name1}.json")
    c1.setNbAgents(2000)
    c1.setNbStates(10)
    c1.save()

    name2 = "exp/0_2"
    c2 = Config(DEFAULT_CONFIG_FILE, f"{name2}.json")
    c2.setNbAgents(2000)
    c2.setNbStates(10)
    c2.setVat(0)
    c2.setLevy(0)
    c2.setTariff(0)
    c2.setWealth(0)
    c2.save()

    if RUN:
        Config.run()  # Run both configs in parallel

    if not ANALYZE:
        return

    a1 = Analysis(name1)
    print(a1.DF_STATES)
    a1.DF_STATES = a1.DF_STATES[a1.DF_STATES["Allowance"] == "Fair"]
    a1.barChart("exp/0_1_1", a1.DF_STATES, "VAT", "Gdp", "NbTransactions")
    a1.barChart("exp/0_1_2", a1.DF_STATES, "VAT", "PopTotalMoney", "StateMoney")
    a1.barChart("exp/0_1_3", a1.DF_STATES, "VAT", "Gini")

    a2 = Analysis(name2)
    print(a2.DF_STATES)
    a2.DF_STATES = a2.DF_STATES[a2.DF_STATES["Allowance"] == "Fair"]
    a2.barChart("exp/0_2_1", a2.DF_STATES, "VAT", "Gdp", "NbTransactions")
    a2.barChart("exp/0_2_2", a2.DF_STATES, "VAT", "PopTotalMoney", "StateMoney")
    a2.barChart("exp/0_2_3", a2.DF_STATES, "VAT", "Gini")


def exp1():
    """
    Experiment 1:
    Influence of the VAT on multiple metrics
    """
    name = "exp/1"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setMinMaxVat(0, 1)
    c.save()
    if RUN:
        c.run()

    if not ANALYZE:
        return
    a = Analysis(name)
    print(a.DF_STATES)
    a.DF_STATES = a.DF_STATES[a.DF_STATES["Allowance"] == "Fair"]
    a.linePointsChart("exp/1_1", a.DF_STATES, "VAT", "Gdp", "NbTransactions")
    a.linePointsChart("exp/1_2", a.DF_STATES, "VAT", "PopTotalMoney", "StateMoney")
    a.linePointsChart("exp/1_3", a.DF_STATES, "VAT", "Gini")


def exp2():
    """
    Experiment 2:
    Influence of the Levy tax on multiple metrics
    """
    name = "exp/2"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setMinMaxLevy(0, 1)
    c.save()
    if RUN:
        c.run()

    if not ANALYZE:
        return
    a = Analysis(name)
    print(a.DF_STATES)
    a.DF_STATES = a.DF_STATES[a.DF_STATES["Allowance"] == "Fair"]
    a.linePointsChart("exp/2_1", a.DF_STATES, "Levy", "Gdp", "NbTransactions")
    a.linePointsChart("exp/2_2", a.DF_STATES, "Levy", "PopTotalMoney", "StateMoney")
    a.linePointsChart("exp/2_3", a.DF_STATES, "Levy", "Gini")


def exp3():
    """
    Experiment 3:
    Influence of the Wealth tax on multiple metrics
    Compare all taxes and see which is better for which metric
    """
    name = "exp/3"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setMinMaxWealth(0, 1)
    c.save()
    if RUN:
        c.run()

    if not ANALYZE:
        return
    a = Analysis(name)
    print(a.DF_STATES)
    a.DF_STATES = a.DF_STATES[a.DF_STATES["Allowance"] == "Fair"]
    a.linePointsChart("exp/3_1", a.DF_STATES, "WealthTax", "Gdp", "NbTransactions")
    a.linePointsChart("exp/3_2", a.DF_STATES, "WealthTax", "PopTotalMoney", "StateMoney")
    a.linePointsChart("exp/3_3", a.DF_STATES, "WealthTax", "Gini")


def exp4():
    """
    Experiment 4:
    Influence of the Unemployment rate on multiple metrics.
    """
    name = "exp/4"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setMinMaxUnemployment(0, 1)
    c.save()
    if RUN:
        c.run()

    if not ANALYZE:
        return
    a = Analysis(name)
    print(a.DF_STATES)
    print(a.DF_AGENTS_PRODUCTS)
    a.DF_STATES = a.DF_STATES[a.DF_STATES["Allowance"] == "Fair"]
    a.linePointsChart("exp/4_1", a.DF_STATES, "Unemployment", "Gdp", "NbTransactions")
    a.linePointsChart("exp/4_2", a.DF_STATES, "Unemployment", "PopTotalMoney", "StateMoney")
    a.linePointsChart("exp/4_3", a.DF_STATES, "Unemployment", "Gini")
    a.barChart("exp/4_4", a.DF_AGENTS_PRODUCTS, "IsProducer", "Sales", "Purchases")


def exp5():
    """
    Experiment 5:
    Influence of the Black economy share on multiple metrics.
    """
    name = "exp/5"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setMinMaxBlack(0, 1)
    c.save()
    if RUN:
        c.run()

    if not ANALYZE:
        return
    a = Analysis(name)
    print(a.DF_STATES)
    a.DF_STATES = a.DF_STATES[a.DF_STATES["Allowance"] == "Fair"]
    a.linePointsChart("exp/5_1", a.DF_STATES, "Black", "Gdp", "NbTransactions")
    a.linePointsChart("exp/5_2", a.DF_STATES, "Black", "PopTotalMoney", "StateMoney")
    a.linePointsChart("exp/5_3", a.DF_STATES, "Black", "Gini")


def exp6():
    """
    Experiment 6:
    Influence of the Allowance type on multiple metrics.
    """
    name = "exp/6"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.save()
    if RUN:
        c.run()

    if not ANALYZE:
        return
    a = Analysis(name)
    print(a.DF_STATES)
    a.barChart("exp/6_1", a.DF_STATES, "Allowance", "Gdp", "NbTransactions")
    a.barChart("exp/6_2", a.DF_STATES, "Allowance", "PopTotalMoney", "StateMoney")
    a.barChart("exp/6_3", a.DF_STATES, "Allowance", "Gini")


def exp7():
    """
    Experiment 7:
    Influence of the number of connected States on multiple metrics.
    """
    name = "exp/7"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setProbConnection(0.3)
    c.setClusterSize(c.getNbStates()//20)  # 5 connected states, thus number of connected States = 4
    c.save()
    if RUN:
        c.run()

    if not ANALYZE:
        return
    a = Analysis(name)
    print(a.DF_STATES)
    a.barChart("exp/7_1", a.DF_STATES, "NbConnectedStates", "Gdp", "NbTransactions")
    a.barChart("exp/7_2", a.DF_STATES, "NbConnectedStates", "PopTotalMoney", "StateMoney")
    a.barChart("exp/7_3", a.DF_STATES, "NbConnectedStates", "Gini")


def exp8():
    """
    Experiment 8:
    Evolution of multiple metrics with time (ticks)
    Re-run this experiment but now, in Product, change the production price to
    something positive and notice the huge difference
    """
    name = "exp/8"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setNbTicks(c.getNbTicks()*2)
    c.save()
    if RUN:
        c.run()

    if not ANALYZE:
        return
    a = Analysis(name)
    print(a.DF_TICKS)
    print(a.DF_STATES)
    a.linePointsChart("exp/8_1", a.DF_TICKS, "Tick", "WorldNbTransactions", "WorldGdp")
    a.linePointsChart("exp/8_2", a.DF_TICKS, "Tick", "WorldStatesMoney", "WorldAgentsMoney")


################################
# Agent parameters experiences #
################################

def exp9():
    """
    Experiment 9:
    Influence of the Agent's talent on two metrics:
    - Sales (number of products it sells)
    - Purchases (number of products it purchases)
    """
    name = "exp/9"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.save()
    if RUN:
        c.run()

    if not ANALYZE:
        return
    a = Analysis(name)
    print(a.DF_AGENTS_PRODUCTS)
    a.linePointsChart(name, a.DF_AGENTS_PRODUCTS, "Talent", "Sales", "Purchases")


def exp10():
    """
    Experiment 10:
    Influence of the Initial money of an Agent.
    """
    name = "exp/10"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.setMinMaxInitMoney(0, 10000)
    c.save()
    if RUN:
        c.run()

    if not ANALYZE:
        return
    a = Analysis(name)
    print(a.DF_AGENTS_PRODUCTS)
    a.linePointsChart("exp/10_1", a.DF_AGENTS_PRODUCTS, "AgentInitMoney", "Sales", "Purchases")
    a.linePointsChart("exp/10_2", a.DF_AGENTS_PRODUCTS, "AgentInitMoney", "Stock")


def exp11():
    """
    Experiment 11:
    Study of how a non-producer Agent can make purchases
    """
    name1 = "exp/11_1"
    c1 = Config(DEFAULT_CONFIG_FILE, f"{name1}.json")
    c1.setNbStates(1)
    c1.setNbAgents(1000)
    c1.setNbTicks(1000)
    c1.setUnemployment(0.3)
    c1.save()

    name2 = "exp/11_2"
    c2 = Config(f"{name1}.json", f"{name2}.json")
    c2.setNbTicks(c1.getNbTicks() * 10)
    c2.save()

    if RUN:
        Config.run()  # Run both configs in parallel

    if not ANALYZE:
        return
    a1 = Analysis(name1)
    a1.barChart(name1, a1.DF_AGENTS_PRODUCTS, "IsProducer", "Purchases")

    a2 = Analysis(name2)
    a2.barChart(name2, a2.DF_AGENTS_PRODUCTS, "IsProducer", "Purchases")


def exp12():
    """
    Experiment 12:
    Visualization of the distribution of Wealth among Agents
    """
    name = "exp/12"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.save()
    if RUN:
        c.run()

    if not ANALYZE:
        return
    a = Analysis(name)
    flatState = a.DF_STATES[a.DF_STATES["Allowance"] == "Flat"].iloc[0]["Id"]
    fairState = a.DF_STATES[a.DF_STATES["Allowance"] == "Fair"].iloc[0]["Id"]
    flatStateAgents = a.DF_AGENTS_PRODUCTS[a.DF_AGENTS_PRODUCTS["State"] == flatState]
    fairStateAgents = a.DF_AGENTS_PRODUCTS[a.DF_AGENTS_PRODUCTS["State"] == fairState]
    a.agentsWealthDistribution("exp/12_1", flatStateAgents)
    a.agentsWealthDistribution("exp/12_2", fairStateAgents)


##################################
# Product parameters experiences #
##################################

def exp13():
    """
    Experiment 13:
    Influence of the SellingPrice on two metrics.
    """
    name = "exp/13"
    c = Config(DEFAULT_CONFIG_FILE, f"{name}.json")
    c.save()
    if RUN:
        c.run()

    if not ANALYZE:
        return
    a = Analysis(name)
    print(a.DF_AGENTS_PRODUCTS)
    a.linePointsChart(name, a.DF_AGENTS_PRODUCTS, "SellingPrice", "Stock", "Sales")


def exp14():
    """
    Experiment 14:
    Influence of the choosing method (cheapest, random, weighted random)
    of a Product from the list of matching Products
    """
    name1 = "exp/14_1"
    c1 = Config(DEFAULT_CONFIG_FILE, f"{name1}.json")
    c1.setProductChoiceCheapest()
    c1.save()

    name2 = "exp/14_2"
    c2 = Config(DEFAULT_CONFIG_FILE, f"{name2}.json")
    c2.setProductChoiceRandom()
    c2.save()

    name3 = "exp/14_3"
    c3 = Config(DEFAULT_CONFIG_FILE, f"{name3}.json")
    c3.setProductChoiceWeightedRandom()
    c3.save()

    if RUN:
        Config.run()  # Run all 3 configs in parallel

    if not ANALYZE:
        return
    a1 = Analysis(name1)
    a1.linePointsChart(name1, a1.DF_AGENTS_PRODUCTS, "SellingPrice", "Stock", "Sales")

    a2 = Analysis(name2)
    a2.linePointsChart(name2, a2.DF_AGENTS_PRODUCTS, "SellingPrice", "Stock", "Sales")

    a3 = Analysis(name3)
    a3.linePointsChart(name3, a3.DF_AGENTS_PRODUCTS, "SellingPrice", "Stock", "Sales")


#######################
# Run all experiments #
#########################

EXPERIMENTS = [
    exp0,
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
    # exp12,
    # exp13,
    # exp14,
]


def runExperimentsParallel():
    Path("params/exp").mkdir(parents=True, exist_ok=True)
    Path("res/csv/agents/exp").mkdir(parents=True, exist_ok=True)
    Path("res/csv/states/exp").mkdir(parents=True, exist_ok=True)
    Path("res/csv/products/exp").mkdir(parents=True, exist_ok=True)
    Path("res/csv/ticks/exp").mkdir(parents=True, exist_ok=True)
    Config.resetAll()
    for exp in EXPERIMENTS:
        exp()
    Config.run()


def plotGraphs():
    Path("res/img/exp").mkdir(parents=True, exist_ok=True)
    Config.resetAll()
    global ANALYZE
    ANALYZE = True
    for exp in EXPERIMENTS:
        exp()
