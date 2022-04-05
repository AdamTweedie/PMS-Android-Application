import pandas as pd
import numpy as np
from collections import Counter
import nltk
from nltk.corpus import stopwords
nltk.download('stopwords')
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA
import random


def get_target_cluster(data_frame, index):

    return data_frame.iloc[index]['cluster']


def get_project_suggestions_index(cluster_num, data_frame):

    return data_frame.index[data_frame['cluster'] == cluster_num]


def obtain_suggestions(index, project_data):

    c = ['email', 'name', 'project title', 'project description', 'other info']
    df = pd.DataFrame(data=project_data, columns=c)
    df2 = pd.DataFrame(data = df)

    rows = df2.iloc[[index]].values.tolist()

    return rows


def vectorize(text, maxx_features):

    vectorizer = TfidfVectorizer(max_features=maxx_features)
    X = vectorizer.fit_transform(text)

    return X


def clean_data(project_data):

    # create df with columns required
    dirty_df = pd.DataFrame(data = project_data)
    dirty_df['project'] = dirty_df['project title'] + ' ' + dirty_df['project description']
    project = dirty_df['project'].copy()
    d = {'d': project}
    df_unclean = pd.DataFrame(data = d)

    stop_words = stopwords.words('english')
    df_nsw = []

    stopwords_dict = Counter(stop_words)

    for row in df_unclean['d']:
        row = ' '.join([word for word in row.split() if word not in stopwords_dict])
        df_nsw.append(row)


    d = {'data': df_nsw}
    df = pd.DataFrame(data=d)

    return df


def clean_user_entry(entry):
    stop_words = stopwords.words('english')
    stopwords_dict = Counter(stop_words)

    return ' '.join([word for word in entry.split() if word not in stopwords_dict])


def recommender_new(user_entry, list_data):

    # covert data into pandas dataframe in real recommender
    c = ['email', 'name', 'project title', 'project description', 'other info']
    df_dirty = pd.DataFrame(data=list_data, columns=c)
    df = pd.DataFrame(data = clean_data(df_dirty))

    # add user entry column
    col_user_entry = 'user_entry'
    df[col_user_entry] = np.nan

    # Add user entry to data
    df2 = {'data' : clean_user_entry(user_entry)}
    df = df.append(df2, ignore_index = True)
    index = len(df) - 1
    df.at[index, col_user_entry] = 1

    # vectorize the data
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

    # obtain similar entry index list
    user_cluster = get_target_cluster(df, index)
    similar_entries_index = get_project_suggestions_index(user_cluster, df)
    similar_entries_index = similar_entries_index[:-1]

    # obtain sample of similar entries
    if len(similar_entries_index) > 5:
        suggestions_index = random.sample(list(similar_entries_index), 5)
    else:
        suggestions_index = list(similar_entries_index)

    # obtain less similar entries
    if user_cluster >= 1 and user_cluster < df['cluster'].max():
        item1 = random.choice(tuple(get_project_suggestions_index(user_cluster-1, df)))
        item2 = random.choice(tuple(get_project_suggestions_index(user_cluster+1, df)))
        suggestions_index.append(item1)
        suggestions_index.append(item2)
    elif user_cluster < df['cluster'].max()-2:
        item1 = random.choice(tuple(get_project_suggestions_index(user_cluster+1, df)))
        item2 = random.choice(tuple(get_project_suggestions_index(user_cluster+2, df)))
        suggestions_index.append(item1)
        suggestions_index.append(item2)
    elif user_cluster == df['cluster'].max():
        item1 = random.choice(tuple(get_project_suggestions_index(user_cluster-1, df)))
        item2 = random.choice(tuple(get_project_suggestions_index(user_cluster-2, df)))
        suggestions_index.append(item1)
        suggestions_index.append(item2)

    return suggestions_index

