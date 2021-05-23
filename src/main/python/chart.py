import matplotlib.pyplot as plt
import numpy as np


plt.style.use('dark_background')


class Chart:
    """
    Chart class to plot points, lines, ...
    """
    def __init__(self, title):
        self.title = title
        self.label_x = None
        self.label_y = None
        self.label_y2 = None
        self.colors = []
        self.fig, self.ax1 = plt.subplots()
        self.ax2 = None

    def set_axis_labels(self, x, y, y2=None):
        """
        Set the labels for the axis
        """
        self.label_x = x
        self.label_y = y
        if y2 is not None:
            self.ax2 = self.ax1.twinx()
            self.label_y2 = y2

    def plot(self, x, y, label=None, color=None, y2=False, smooth=False):
        assert self.label_x is not None
        if smooth:
            f = np.poly1d(np.polyfit(x, y, deg=2))
            x = np.linspace(np.amin(x), np.amax(x), 100)
            y = f(x)
        if not y2:
            self.ax1.plot(x, y, label=label, color=color)
        else:
            self.ax2.plot(x, y, label=label, color=color)
        if color not in self.colors:
            self.colors.append(color)

    def scatter(self, x, y, label=None, color=None, y2=False):
        assert self.label_x is not None
        if not y2:
            self.ax1.scatter(x, y, label=label, color=color)
        else:
            self.ax2.scatter(x, y, label=label, color=color)
        if color not in self.colors:
            self.colors.append(color)

    def bar(self, x, y, label=None, color=None, y2=False):
        assert self.label_x is not None
        if not y2:
            self.ax1.bar(x, y, label=label, color=color)
        else:
            self.ax2.bar(x, y, label=label, color=color)
        if color not in self.colors:
            self.colors.append(color)

    def show(self, filename=None):
        """
        Show (and save) the Chart
        :param filename: If not None, save the Chart as an image
        """
        assert self.label_x is not None
        self.fig.suptitle(self.title)
        self.ax1.set_xlabel(self.label_x)
        self.ax1.set_ylabel(self.label_y)

        if self.label_y2 is not None:  # Two y axis
            self.ax1.set_ylabel(self.label_y, color=self.colors[0])
            self.ax1.tick_params(axis='y', labelcolor=self.colors[0])
            self.ax2.set_ylabel(self.label_y2, color=self.colors[1])
            self.ax2.tick_params(axis='y', labelcolor=self.colors[1])

        self.fig.tight_layout()
        plt.legend()
        plt.show()
        if filename is not None:
            self.fig.savefig(filename)
