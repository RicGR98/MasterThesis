#!/bin/bash
#
#SBATCH --job-name=MasterThesis
#SBATCH -o output.txt # STDOUT
#SBATCH -e error.txt # STDERR
#SBATCH --mail-type=ALL
#SBATCH --mail-user=ricardo.gomes.rodrigues@ulb.be     # Where to send mail
#SBATCH --mem-per-cpu=2gb                     # Job memory request
#SBATCH --ntasks=1
#SBATCH --time=48:00:00

mvn="$HOME/.local/bin/apache-maven-3.8.1/bin/mvn"


# Install maven if it is not already installed
if [ ! -f "$mvn" ]; then
    mkdir -p ~/.local/{bin,lib,src,include}
    wget https://downloads.apache.org/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz
    tar -xzf apache-maven-3.8.1-bin.tar.gz -C ~/.local/bin
    rm apache-maven-3.8.1-bin.tar.gz
fi

module load Python/3.7.4-GCCcore-8.3.0
module load Java/11

# Compile java
$mvn package

# Run java simulation
#$mvn exec:java -Dexec.mainClass="rgomesro.Main" -Dexec.cleanupDaemonThreads=false

# Run Python's main
python3 src/main/python/main.py

# Commands to copy file to cluster and launch the job:
# Ricardo ==>  rsync -avz run.sh pom.xml src params rgomesro@dragon2:
# Ricardo ==>  ssh dragon2
# Cluster ==>  sbatch run.sh
# Cluster ==>  squeue -u rgomesro
# Cluster ==>  scancel -u rgomesro
# Ricardo ==>  rsync -avz rgomesro@dragon2:res .
