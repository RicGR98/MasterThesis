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
    df['Money'] = df['Money']/df['PopulationSize']  # Money proportional to population size
    df['PopulationMoney'] = df['Money']/df['PopulationSize']  # Money proportional to population size
    return df


def vatInfluence():
    """
    Analyse the influence of the State's VAT on two metrics:
    1. The money of the State itself
    2. The average money of an agent of this State
    """
    df = sanitizedStates()
    df = df.sort_values('VAT')

    fig, ax1 = plt.subplots()
    ax1.set_xlabel("State's VAT")

    # Draw line 1: money of the State itself
    color = 'tab:red'
    ax1.set_ylabel("State's money", color=color)
    ax1.plot(df["VAT"], df["Money"], color=color)
    ax1.tick_params(axis='y', labelcolor=color)

    ax2 = ax1.twinx()  # instantiate a second axes that shares the same x-axis

    # Draw line 2: money of an average agent of this State
    color = 'tab:blue'
    ax2.set_ylabel("Average money of an agent", color=color)
    ax2.plot(df["VAT"], df["PopulationMoney"], color=color)
    ax2.tick_params(axis='y', labelcolor=color)

    fig.tight_layout()
    plt.show()


def lorenzCurve():
    df = pd.read_csv(CSV_AGENTS)
    X = df["Money"]

    X_lorenz = X.cumsum() / X.sum()
    fig, ax = plt.subplots()

    fig.suptitle('Wealth distribution')
    ax.set_xlabel("Fraction of population")
    ax.set_ylabel("Fraction of wealth")

    # scatter plot of Lorenz curve
    ax.scatter(np.arange(X_lorenz.size)/(X_lorenz.size-1), X_lorenz, color='blue', label="Lorenz curve")

    # line plot of equality
    ax.plot([0, 1], [0, 1], color='red', label="Perfect equality")

    plt.legend()
    plt.show()


def main():
    lorenzCurve()
    vatInfluence()


if __name__ == '__main__':
    main()
