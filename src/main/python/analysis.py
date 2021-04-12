import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

plt.style.use('dark_background')

DIR_RES = "res/"
CSV_AGENTS = DIR_RES + "agents.csv"
CSV_STATES = DIR_RES + "states.csv"


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

    fig, ax1 = plt.subplots()
    fig.suptitle("Influence of a State's VAT")
    ax1.set_xlabel("State's VAT")

    # Draw line 1: money of the State itself
    color = 'tab:red'
    ax1.set_ylabel("State's money", color=color)
    ax1.plot(df["VAT"], df["Money"], color=color)
    ax1.tick_params(axis='y', labelcolor=color)

    ax2 = ax1.twinx()  # instantiate a second axes that shares the same x-axis

    # Draw line 2: average number of product sold by an agent
    color = 'tab:blue'
    ax2.set_ylabel("Average number of product sold by an agent", color=color)
    ax2.plot(df["VAT"], df["PopTotalSoldProducts"], color=color)
    ax2.tick_params(axis='y', labelcolor=color)

    fig.tight_layout()
    plt.show()


def gini():
    df = pd.read_csv(CSV_AGENTS)
    X = df.sort_values('Money')["Money"]
    n = X.size
    coef_ = 2. / n
    const_ = (n + 1.) / n
    weighted_sum = sum([(i+1)*yi for i, yi in enumerate(X)])
    return round(coef_*weighted_sum/(X.sum()) - const_, 3)


def agentsWealthDistribution():
    df = pd.read_csv(CSV_AGENTS)
    X = df["Money"]

    X_lorenz = X.cumsum() / X.sum()
    fig, ax = plt.subplots()

    fig.suptitle('Wealth distribution (Gini coeff: ' + str(gini()) + ")")
    ax.set_xlabel("Fraction of population")
    ax.set_ylabel("Fraction of wealth")

    # Scatter plot of Lorenz curve
    ax.scatter(np.arange(X_lorenz.size)/(X_lorenz.size-1), X_lorenz, color='blue', label="Lorenz curve")

    # Line plot of equality
    ax.plot([0, 1], [0, 1], color='red', label="Perfect equality")

    plt.legend()
    plt.show()


def main():
    agentsWealthDistribution()
    vatInfluence()


if __name__ == '__main__':
    main()
