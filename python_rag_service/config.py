"""配置文件"""
from flask_cors import CORS

def setup_cors(app):
    """设置CORS"""
    CORS(app)
