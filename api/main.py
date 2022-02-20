from fastapi import FastAPI
from starlette.responses import RedirectResponse

from server.crimes import router as crimes_router
from server.kpis import router as kpis_router

app = FastAPI()
app.include_router(crimes_router, tags=["Crimes"], prefix="/crimes")
app.include_router(kpis_router, tags=["KPIs"], prefix="/kpis")


@app.get("/")
async def docs_redirect():
    return RedirectResponse(url='/docs')
