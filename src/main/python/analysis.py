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

resultToName = {
    "VAT": "State's VAT",
    "Money": "State's money",
    "NbTransactions": "Number of transactions",
    "AllowanceValue": "State's allowance",
    "GiniCoeff": "State's Gini coefficient",
}


class Analysis:
    def __init__(self):
        self.DF_AGENTS = pd.read_csv(CSV_AGENTS)
        self.DF_PRODUCTS = pd.read_csv(CSV_PRODUCTS)
        self.DF_AGENTS_PRODUCTS: pd.DataFrame = pd.merge(self.DF_AGENTS, self.DF_PRODUCTS, on='Id')
        self.DF_STATES = self.getStatesDF()

    def getStatesDF(self) -> pd.DataFrame:
        """
        Return dataframe with some new columns for easier analysis later
        """
        df: pd.DataFrame = pd.read_csv(CSV_STATES)
        # Money/PopTotalMoney/PopTotalSoldProducts/NbTransactions proportional to population size
        df['Money'] = df['Money'] / df['PopSize']
        df['PopTotalMoney'] = df['PopTotalMoney'] / df['PopSize']
        df['NbTransactions'] = df['NbTransactions'] / df['PopSize']
        # Map ConnectedStates = "4,37,21,10," to NbConnectedStates = 4:
        df["NbConnectedStates"] = df["ConnectedStates"].astype(str).map(lambda val: len(val.split(","))) - 1
        # Add Gini coefficient to each State
        df['GiniCoeff'] = df['Id'].apply(lambda id_: self.gini(self.DF_AGENTS_PRODUCTS[self.DF_AGENTS["State"] == id_]))
        return df

    @staticmethod
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

    def influenceOfParamOnResults(self, param, result1, result2):
        f"""
        Analyse the influence of the State's {param} on two metrics:
        1. {result1}
        2. {result2}
        """
        df = self.DF_STATES.sort_values(param)
        print(df)
        chart = Chart(f"Influence of the {resultToName[param]}", 2)
        chart.set_axis_labels(f"{resultToName[param]}", resultToName[result1], resultToName[result2])
        chart.scatter(df[param], df[result1], color="red")
        chart.plot(df[param], df[result1], color="red", smooth=True)
        chart.scatter(df[param], df[result2], color="blue", y2=True)
        chart.plot(df[param], df[result2], color="blue", y2=True, smooth=True)
        chart.show()

    def agentsWealthDistribution(self, df):
        """
        Analyze the wealth distribution among Agents with
        the Gini coefficient and the Lorenz curve
        """
        X = df["Money"]
        X = X.sort_values()
        lorenz_x = np.linspace(0.0, 1.0, X.size)
        lorenz_y = X.cumsum() / X.sum()

        chart = Chart('Wealth distribution (Gini coeff: ' + str(self.gini(self.DF_AGENTS_PRODUCTS)) + ")")
        chart.set_axis_labels("Fraction of population", "Fraction of wealth")
        chart.plot([0, 1], [0, 1], color='red', label="Perfect equality")
        chart.plot(lorenz_x, lorenz_y, color='blue', label="Lorenz curve")
        chart.show()

    def connectedStatesInfluence(self):
        """
        Influence of the number of connected States to one State
        """
        df = self.DF_STATES.groupby("NbConnectedStates")["Money"].mean()
        chart = Chart("Influence of a State's number of connections")
        chart.set_axis_labels("Number of connected States", "State's money")
        chart.bar(df.index, df, color='blue')
        chart.show()


def main():
    a = Analysis()
    a.influenceOfParamOnResults("AllowanceValue", "Money", "GiniCoeff")


def paramsTweaking():
    p = Params("small.json")
    p["World"]["NB_STATES"] = 200
    p.save()


if __name__ == '__main__':
    main()
