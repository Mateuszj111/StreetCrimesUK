from typing import List, Dict

import motor.motor_asyncio

from server.models import CrimeSchema, CrimesShareModel, CrimeLocationModel

MONGO_DETAILS: str = "mongodb://spark:spark@mongo:27017"

client = motor.motor_asyncio.AsyncIOMotorClient(MONGO_DETAILS)

database = client.admin

crimes_collection = database.get_collection("final")

crimes_share_collection = database.get_collection("crimesShare")

crimes_share_location_collection = database.get_collection("crimesByLocation")

crimes_outcome_collection = database.get_collection("crimesByOutcome")


async def retrieve_crime(crime_id: str) -> List[CrimeSchema]:
    crimes: List[CrimeSchema] = []
    async for crime in crimes_collection.find({"crimeID": crime_id}):
        del crime['_id']
        crimes.append(crime)
    return crimes


async def retrieve_crime_shares() -> List[CrimesShareModel]:
    crime_shares: List[CrimesShareModel] = []
    async for cs in crimes_share_collection.find():
        del cs['_id']
        crime_shares.append(cs)
    return crime_shares


async def retrieve_crime_location() -> List[CrimeLocationModel]:
    crime_location_counts: List[CrimeLocationModel] = []
    async for cl in crimes_share_location_collection.find():
        del cl['_id']
        crime_location_counts.append(cl)
    return crime_location_counts


async def retrieve_crime_outcome() -> List[Dict]:
    crime_outcome_counts: List[Dict] = []
    async for cl in crimes_outcome_collection.find():
        del cl['_id']
        crime_outcome_counts.append(cl)
    return crime_outcome_counts
