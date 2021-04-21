import numpy as np
import pandas as pd

from src.main.python.chart import Chart

pd.set_option('display.max_columns', 500)
pd.set_option('display.width', 1000)

DIR_RES = "res/"
DIR_RES_IMG = DIR_RES + "img/"
DIR_RES_CSV = DIR_RES + "csv/"
CSV_AGENTS = DIR_RES_CSV + "agents.csv"
CSV_STATES = DIR_RES_CSV + "states.csv"
CSV_PRODUCTS = DIR_RES_CSV + "products.csv"

DF_AGENTS = pd.read_csv(CSV_AGENTS)
DF_PRODUCTS = pd.read_csv(CSV_PRODUCTS)
DF_AGENTS_PRODUCTS: pd.DataFrame = pd.merge(DF_AGENTS, DF_PRODUCTS, how='left', left_on='Id', right_on='Producer')
del DF_AGENTS_PRODUCTS['Id_y']
del DF_AGENTS_PRODUCTS['Producer']
print(DF_AGENTS_PRODUCTS)


def sanitizedStates() -> pd.DataFrame:
    """
    Return dataframe whose values are independent from the population size
    (to be able to compare State with different population sizes)
    """
    df: pd.DataFrame = pd.read_csv(CSV_STATES)
    df['Money'] = df['Money']/df['PopSize']  # Money proportional to population size
    df['PopTotalMoney'] = df['PopTotalMoney']/df['PopSize']  # Money proportional to population size
    df['PopTotalSoldProducts'] = df['PopTotalSoldProducts']/df['PopSize']  # Money proportional to population size
    return df


def vatInfluence():
    """
    Analyse the influence of the State's VAT on two metrics:
    1. The money of the State itself
    2. The average number of product sold by an agent of this State
    """
    df = sanitizedStates()
    df = df.sort_values('VAT')

    chart = Chart("Influence of a State's VAT", 2)
    chart.set_axis_labels("State's VAT", "State's money", "Average number of product sold by an agent")
    chart.plot(df["VAT"], df["Money"], color="red")
    chart.plot_y2(df["VAT"], df["PopTotalSoldProducts"], color="blue")
    chart.show()


def gini():
    """
    :return Gini coefficient of Agents
    """
    X = DF_AGENTS_PRODUCTS.sort_values('Money')["Money"]
    n = X.size
    coef_ = 2. / n
    const_ = (n + 1.) / n
    weighted_sum = sum([(i+1)*yi for i, yi in enumerate(X)])
    return round(coef_*weighted_sum/(X.sum()) - const_, 3)


def agentsWealthDistribution():
    """
    Analyze the wealth distribution among Agents with
    the Gini coefficient and the Lorenz curve
    """
    X = DF_AGENTS_PRODUCTS["Money"]
    X = X.sort_values()
    lorenz_x = np.linspace(0.0, 1.0, X.size)
    lorenz_y = X.cumsum() / X.sum()

    chart = Chart('Wealth distribution (Gini coeff: ' + str(gini()) + ")")
    chart.set_axis_labels("Fraction of population", "Fraction of wealth")
    chart.plot([0, 1], [0, 1], color='red', label="Perfect equality")
    chart.plot(lorenz_x, lorenz_y, color='blue', label="Lorenz curve")
    chart.show()


def main():
    agentsWealthDistribution()
    vatInfluence()


if __name__ == '__main__':
    main()
