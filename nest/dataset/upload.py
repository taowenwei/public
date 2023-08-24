from pymongo import MongoClient
import csv
import certifi
import os

collectionName = 'logistics'
client = MongoClient(os.environ['DBURL'], tlsCAFile=certifi.where())
db = client['WenweiTest']
list_of_collections = db.list_collection_names()
if collectionName in list_of_collections:
    db.drop_collection(collectionName)
collection = db[collectionName]

with open('dataset/SupplyChainLogistics.csv', encoding='utf-8-sig') as file:
    reader = csv.DictReader(file)
    for row in reader:
        collection.insert_one(row)


client.close()