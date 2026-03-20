from fastapi import FastAPI

app = FastAPI()

@app.get("/")
def read_root():
    return {"message": "青年技术人才知识库与智能问答系统 API"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
