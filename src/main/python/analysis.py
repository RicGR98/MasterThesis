import matplotlib.pyplot as plt
import pandas as pd

plt.style.use('dark_background')

DIR_RES = "res/"
AGENTS_CSV = DIR_RES + "agents.csv"
STATES_CSV = DIR_RES + "states.csv"


def agents():
    df: pd.DataFrame = pd.read_csv(AGENTS_CSV)
    df = df.groupby(['State'])['Money'].sum()
    print(df)
    df.plot.bar(x="Id", y="Money")
    plt.show()


def states():
    df: pd.DataFrame = pd.read_csv(STATES_CSV)
    df = df.sort_values('VAT')
    print(df)
    df.plot(x="VAT", y="Money")
    plt.show()


def main():
    agents()
    states()


if __name__ == '__main__':
    main()
