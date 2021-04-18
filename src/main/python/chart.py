import matplotlib.pyplot as plt

plt.style.use('dark_background')


class Chart:
    """
    Chart class to plot points, lines, ...
    """
    def __init__(self, title: str, nb_y_axis=1):
        """
        :param title: Title of the chart
        :param nb_y_axis: Number of y axis (1 or 2)
        """
        self.title = title
        self.label_x = None
        self.label_y = None
        self.label_y2 = None
        self.colors = []
        self.fig, self.ax1 = plt.subplots()
        self.ax2 = None
        assert (nb_y_axis in {1, 2})
        if nb_y_axis == 2:
            self.ax2 = self.ax1.twinx()

    def set_axis_labels(self, x, y, y2=None):
        """
        Set the labels for the axis
        """
        assert ((self.ax2 is not None) == (y2 is not None))
        self.label_x = x
        self.label_y = y
        self.label_y2 = y2

    def __plot(self, y_axis, x, y, label=None, color=None):
        """
        Plot a line on the Chart
        :param y_axis: Y axis in which to plot
        :param x: X data
        :param y: Y data
        :param label: label of the plotted line
        :param color: color of the plotted line
        """
        y_axis.plot(x, y, label=label, color=color)
        self.colors.append(color)

    def plot(self, x, y, label=None, color=None):
        self.__plot(self.ax1, x, y, label=label, color=color)

    def plot_y2(self, x, y, label=None, color=None):
        self.__plot(self.ax2, x, y, label=label, color=color)

    def __scatter(self, y_axis, x, y, label=None, color=None):
        """
        Scatter points on the Chart
        :param y_axis: Y axis in which to scatter the points
        :param x: X data
        :param y: Y data
        :param label: label of the scattered points
        :param color: color of the scattered points
        """
        y_axis.scatter(x, y, label=label, color=color)
        self.colors.append(color)

    def scatter(self, x, y, label=None, color=None):
        self.__scatter(self.ax1, x, y, label=label, color=color)

    def scatter_y2(self, x, y, label=None, color=None):
        self.__scatter(self.ax2, x, y, label=label, color=color)

    def show(self, filename=None):
        """
        Show (and save) the Chart
        :param filename: If not None, save the Chart as an image
        """
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
