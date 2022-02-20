from typing import List, Optional

from pydantic import BaseModel, Field


class CrimeSchema(BaseModel):
    crimeID: str = Field(...)
    latitude: Optional[float] = Field(None, ge=-90.0, le=90.0)
    longitude: Optional[float] = Field(None, ge=-180.0, le=180.0)
    districtName: str = Field(...)
    crimeType: str = Field(...)
    outcomeType: List[str] = Field(default_factory=list)

    class Config:
        schema_extra = {
            "example": {
                "crimeID": "0000cc58fc76a63b8b0381d884285c656c79cb60ae7e9a830a574a0a5fbcb77c",
                "latitude": 51.305572,
                "longitude": -2.497027,
                "districtName": "avon-and-somerset",
                "crimeType": "Vehicle crime",
                "outcomeType": ["Status update unavailable"],
            }
        }


class CrimesShareModel(BaseModel):
    crimeType: str = Field(...)
    count: int = Field(..., ge=0)
    fraction: float = Field(..., ge=0.0, le=1.0)

    class Config:
        schema_extra = {
            "example": {
                "crimeType": "Bicycle theft",
                "count": 96227,
                "fraction": 0.015519014570717256
            }
        }


class CrimeLocationModel(BaseModel):
    crimeType: str = Field(...)
    districtName: str = Field(...)
    count: int = Field(..., ge=0)

    class Config:
        schema_extra = {
            "example": {
                "crimeType": "Vehicle crime",
                "districtName": "surrey",
                "fraction": 8907
            }
        }