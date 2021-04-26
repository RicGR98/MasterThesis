import numpy as np
import pandas as pd

from src.main.python.chart import Chart
from src.main.python.params import Params

pd.set_option('display.max_columns', 500)
# pd.set_option('display.max_rows', 500)
pd.set_option('display.width', 1000)

DIR_RES = "res/"
DIR_RES_IMG = DIR_RES + "img/"
DIR_RES_CSV = DIR_RES + "csv/"
CSV_AGENTS = DIR_RES_CSV + "agents0.csv"
CSV_STATES = DIR_RES_CSV + "states0.csv"
CSV_PRODUCTS = DIR_RES_CSV + "products0.csv"


def getStatesDF() -> pd.DataFrame:
    """
    Return dataframe with some new columns for easier analysis later
    """
    df: pd.DataFrame = pd.read_csv(CSV_STATES)
    # Money/PopTotalMoney/PopTotalSoldProducts proportional to population size
    df['Money'] = df['Money'] / df['PopSize']
    df['PopTotalMoney'] = df['PopTotalMoney'] / df['PopSize']
    df['PopTotalSoldProducts'] = df['PopTotalSoldProducts'] / df['PopSize']
    # Map ConnectedStates = "4,37,21,10," to NbConnectedStates = 4:
    df["NbConnectedStates"] = df["ConnectedStates"].astype(str).map(lambda val: len(val.split(","))) - 1
    return df


DF_AGENTS = pd.read_csv(CSV_AGENTS)
DF_PRODUCTS = pd.read_csv(CSV_PRODUCTS)
DF_AGENTS_PRODUCTS: pd.DataFrame = pd.merge(DF_AGENTS, DF_PRODUCTS, on='Id')
DF_STATES = getStatesDF()


def vatInfluence():
    """
    Analyse the influence of the State's VAT on two metrics:
    1. The money of the State itself
    2. The average money of an agent of this State
    """
    df = DF_STATES.sort_values('VAT')
    print(df)
    chart = Chart("Influence of a State's VAT", 2)
    chart.set_axis_labels("State's VAT", "State's money", "Average money of an Agent")
    chart.scatter(df["VAT"], df["Money"], color="red")
    chart.scatter(df["VAT"], df["PopTotalMoney"], color="blue", y2=True)
    chart.plot(df["VAT"], df["Money"], color="red", smooth=True)
    chart.plot(df["VAT"], df["PopTotalMoney"], color="blue", y2=True, smooth=True)
    chart.show()


def gini(df: pd.DataFrame):
    """
    :return Gini coefficient of Agents
    """
    X = df.sort_values('Money')["Money"]
    n = X.size
    coeff = 2. / n
    const_ = (n + 1.) / n
    weighted_sum = sum([(i + 1) * yi for i, yi in enumerate(X)])
    return round(coeff * weighted_sum / (X.sum()) - const_, 3)


def agentsWealthDistribution():
    """
    Analyze the wealth distribution among Agents with
    the Gini coefficient and the Lorenz curve
    """
    X = DF_AGENTS_PRODUCTS["Money"]
    X = X.sort_values()
    lorenz_x = np.linspace(0.0, 1.0, X.size)
    lorenz_y = X.cumsum() / X.sum()

    chart = Chart('Wealth distribution (Gini coeff: ' + str(gini(DF_AGENTS_PRODUCTS)) + ")")
    chart.set_axis_labels("Fraction of population", "Fraction of wealth")
    chart.plot([0, 1], [0, 1], color='red', label="Perfect equality")
    chart.plot(lorenz_x, lorenz_y, color='blue', label="Lorenz curve")
    chart.show()


def connectedStatesInfluence():
    """
    Influence of the number of connected States to one State
    """
    df = DF_STATES.groupby("NbConnectedStates")["Money"].mean()
    chart = Chart("Influence of a State's number of connections")
    chart.set_axis_labels("Number of connected States", "State's money")
    chart.bar(df.index, df, color='blue')
    chart.show()


def main():
    agentsWealthDistribution()
    vatInfluence()
    connectedStatesInfluence()
    # paramsTweaking()


def paramsTweaking():
    p = Params("medium.json")
    p["World"]["NB_STATES"] = 200
    p.save()


if __name__ == '__main__':
    main()
