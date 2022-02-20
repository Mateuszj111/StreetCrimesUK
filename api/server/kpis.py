from typing import List, Dict

from fastapi import APIRouter, HTTPException

from server.database import retrieve_crime_shares, retrieve_crime_location, retrieve_crime_outcome
from server.models import CrimesShareModel, CrimeLocationModel

router = APIRouter()


@router.get("/crimeShares", response_description="Get Crime Type Shares", response_model=List[CrimesShareModel])
async def get_crime_shares():
    crime_shares: List[CrimesShareModel] = await retrieve_crime_shares()
    if crime_shares:
        return crime_shares
    raise HTTPException(status_code=404, detail=f"Crime Shares Data not found")


@router.get("/crimeByLocation", response_description="Get Crime by location", response_model=List[CrimeLocationModel])
async def get_crime_by_location():
    output: List[CrimeLocationModel] = await retrieve_crime_location()
    if output:
        return output
    raise HTTPException(status_code=404, detail=f"Crime by Location not found")


@router.get("/crimeByOutcome", response_description="Get Crime by outcome", response_model=List[Dict])
async def get_crime_by_outcome():
    output: List[Dict] = await retrieve_crime_outcome()
    if output:
        return output
    raise HTTPException(status_code=404, detail=f"Crime by Outcome not found")