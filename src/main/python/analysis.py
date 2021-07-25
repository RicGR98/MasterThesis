import numpy as np
import pandas as pd

from chart import Chart
from config import Config

pd.set_option('display.max_columns', 1000)
# pd.set_option('display.max_rows', 500)
pd.set_option('display.width', 1000)

DIR_RES = "res/"
DIR_RES_IMG = DIR_RES + "img/"
DIR_RES_CSV = DIR_RES + "csv/"
CSV_AGENTS = DIR_RES_CSV + "agents/0.csv"
CSV_STATES = DIR_RES_CSV + "states/0.csv"
CSV_PRODUCTS = DIR_RES_CSV + "products/0.csv"

RESULT_TO_NAME = {
    None: None,
    "Money": "State's money",
    "VAT": "State's VAT",
    "Levy": "State's Levy",
    "Tariff": "State's Tariff",
    "WealthTax": "State's Wealth Tax Value",
    "Allowance": "State's Allowance type",
    "NbTransactions": "Number of transactions",
    "NbConnectedStates": "Number of connected States",
    "Gini": "State's Gini coefficient",
    "Gdp": "State's GDP",
    "Talent": "Agent's Talent",
    "Sales": "Agent's number of sales",
    "Stock": "Agent's products stock",
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
        # Set some fields proportional to population size
        df['Money'] = df['Money'] / df['PopSize']
        df['PopTotalMoney'] = df['PopTotalMoney'] / df['PopSize']
        df['NbTransactions'] = df['NbTransactions'] / df['PopSize']
        df['Gdp'] = df['Gdp'] / df['PopSize']
        # Map ConnectedStates = "4,37,21,10," to NbConnectedStates = 4:
        df["NbConnectedStates"] = df["ConnectedStates"].astype(str).map(lambda val: len(val.split(","))) - 1
        del df["ConnectedStates"]
        # Add Gini coefficient to each State
        df['Gini'] = df['Id'].apply(lambda id_: self.gini(self.DF_AGENTS_PRODUCTS[self.DF_AGENTS["State"] == id_]))
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

    @staticmethod
    def influenceOfParamOnResults(dataFrame: pd.DataFrame, param, result1, result2=None, plot=True, scatter=True):
        f"""
        Analyse the influence of {dataFrame}'s {param} on two metrics:
        1. {result1}
        2. {result2}
        """
        df = dataFrame.sort_values(param)
        print(df)
        chart = Chart(f"Influence of the {RESULT_TO_NAME[param]}")
        chart.set_axis_labels(f"{RESULT_TO_NAME[param]}", RESULT_TO_NAME[result1], RESULT_TO_NAME[result2])
        if scatter:
            chart.scatter(df[param], df[result1], color="red")
        if plot:
            chart.plot(df[param], df[result1], color="red", smooth=True)
        if result2 is not None:
            if scatter:
                chart.scatter(df[param], df[result2], color="blue", y2=True)
            if plot:
                chart.plot(df[param], df[result2], color="blue", y2=True, smooth=True)
        chart.show()

    @staticmethod
    def influenceOfParamBar(dataFrame: pd.DataFrame, param: str, result: str):
        f"""
        Analyse the influence of {dataFrame}'s {param} on {result} 
        """
        df = dataFrame.groupby(param)[result].mean()
        chart = Chart(f"Influence of the {RESULT_TO_NAME[param]}")
        chart.set_axis_labels(f"{RESULT_TO_NAME[param]}", RESULT_TO_NAME[result])
        chart.bar(df.index, df, color='blue')
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


def experiment1():
    """"""
    c = Config("small.json", f"exp1.json")
    c.set({
        "CLUSTER_SIZE": 7,
        "PROB_CONNECTION": 0.1
    })
    c.save()
    c.run()



def analyse():
    # a = Analysis()
    # a.influenceOfParamOnResults(a.DF_AGENTS_PRODUCTS, "Talent", "Sales", scatter=False)
    # # a.influenceOfParamOnResults(a.DF_STATES, "VAT", "Money", "NbTransactions")
    # # a.influenceOfParamOnResults(a.DF_STATES, "WealthTax", "Money", "Gini")
    # # a.influenceOfParamOnResults(a.DF_STATES, "VAT", "Gdp", "NbTransactions")
    # a.influenceOfParamBar(a.DF_STATES, "NbConnectedStates", "Gini")
    # a.influenceOfParamBar(a.DF_STATES, "Allowance", "Gini")
    experiment1()


if __name__ == '__main__':
    analyse()
