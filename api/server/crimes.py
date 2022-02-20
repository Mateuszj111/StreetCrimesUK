from typing import List

from fastapi import APIRouter, HTTPException

from server.database import retrieve_crime
from server.models import CrimeSchema

router = APIRouter()


@router.get("/{crime_id}", response_description="Get crime data of given ID", response_model=List[CrimeSchema])
async def get_crime_data(crime_id: str):
    crimes: List[CrimeSchema] = await retrieve_crime(crime_id)
    if crimes:
        return crimes
    raise HTTPException(status_code=404, detail=f"Crime entries for crimeID: {crime_id} not found")
