from analysis import analysis
from config import Config

if __name__ == '__main__':
    c = Config("small.json")
    c["World"]["NB_TICKS"] = 1000
    c.run()
    analysis()
