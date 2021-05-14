# Master thesis

### By Ricardo Gomes Rodrigues, 000443812, ULB.

This project contains all the source code, 
configuration files, used papers, and the report for 
my Master Thesis.

# Requirements
- Linux or MacOS
- wget command
- Java 11 or higher
- Python 3.4 or higher
- Pandas
- Numpy
- Matplotlib
- (IntelliJ IDEA by JetBrains)

# Running

## Running the simulation

### From terminal

#### Local machine:

    bash run.sh local

#### On the CECI cluster:

    bash run.sh

### From IntelliJ

Simply open the project and run the Main class

## Running the analysis
Make sure the simulation has been run and that
the `res/csv/` directory exists.

### From terminal
    python3 analysis.py

### From IntelliJ
Simply open the project and run `analysis.py` file. 
