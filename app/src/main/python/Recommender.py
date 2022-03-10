from os.path import dirname, join
import pandas as pd
import numpy as np
# from collections import Counter
import nltk
# from nltk.corpus import stopwords
nltk.download('stopwords')
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA


def vectorize(text, maxx_features):

    vectorizer = TfidfVectorizer(max_features=maxx_features)
    X = vectorizer.fit_transform(text)

    return X


def get_target_cluster(data_frame, index):

    return data_frame.iloc[index]['cluster']


def get_project_suggestions_index(cluster_num, data_frame):

    return data_frame.index[data_frame['cluster'] == cluster_num]


def obtain_suggestions(index, additional_projects):
    file = join(dirname(__file__), "Exe-CompSci-Unclean.csv")
    df2 = pd.read_csv(file)

    for sublist in additional_projects:
        data = {"Email": sublist.get(0),
                "Name": sublist.get(1),
                "Project title": sublist.get(2),
                "Brief description": sublist.get(3),
                "Any other useful information": sublist.get(4)}
        df2 = df2.append(data, ignore_index=True)

    rows = df2.iloc[[index]].values.tolist()

    return rows


def recommender(user_entry, additional_data): # ideally would take K, dataset, and user entry

    # load base data
    col_user_entry = 'user_entry'
    file = join(dirname(__file__), "ExeCompSciDataClean.csv")
    df = pd.read_csv(file)
    df[col_user_entry] = np.nan

    # add additional projects to data
    for row in additional_data:
        data = {'data': row, 'user_entry': np.nan}
        df = df.append(data, ignore_index=True)

    # Add user entry to data
    df2 = {'data' : user_entry}
    df = df.append(df2, ignore_index = True)
    index = len(df) - 1
    df.at[index, col_user_entry] = 1

    # Vectorized the data
    text = df['data'].values
    max_features = 2**12
    X = vectorize(text, max_features)

    # pca reduction
    pca = PCA(n_components=0.4, random_state=42)
    X_reduced = pca.fit_transform(X.toarray())

    # generify k - i.e. create function which could be used for different datasets
    k = 18
    kmeans = KMeans(n_clusters=k, random_state=42)
    y_pred = kmeans.fit_predict(X_reduced)
    df['cluster'] = y_pred

    user_cluster = get_target_cluster(df, index)
    similar_entries_index = get_project_suggestions_index(user_cluster, df)
    similar_entries_index = similar_entries_index[:-1]

    # obtain similar entries
    # thomas mitchell

    return similar_entries_index





















