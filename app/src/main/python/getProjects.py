from os.path import dirname, join
import numpy as np
import pandas as pd


file = join(dirname(__file__), "Exe-CompSci-Unclean.csv")
df = pd.read_csv(file)


def get_all_projects():
    return df.to_numpy()


def get_column_names():
    return df.columns.values.tolist()

