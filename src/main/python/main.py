import shlex
import subprocess
from pathlib import Path

from analysis import main

if __name__ == '__main__':
    home = str(Path.home())
    cmd = f"{home}/.local/bin/apache-maven-3.8.1/bin/mvn exec:java -Dexec.mainClass='rgomesro.Main' -Dexec.cleanupDaemonThreads=false "
    args = shlex.split(cmd)

    # Call Java Simulation and wait for results (in .csv files)
    result = subprocess.check_call(args)
    if result == 0:
        main()
