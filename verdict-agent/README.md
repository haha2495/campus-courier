# VerdictAgent

证据权重纠纷判责 Agent。Python + FastAPI + SQLite/MySQL + Redis 可选。

## 结构

```
app/config.py        阈值、权重系数、DB/Redis/DeepSeek 配置
app/models.py        Pydantic 模型
app/keywords.py      关键词库、时间/金额/订单号正则
app/extractor.py     证据要素抽取（规则 + DeepSeek 双通道）
app/scoring.py       单条证据打分
app/arbitration.py   同类型双方证据冲突仲裁
app/confidence.py    置信度计算与三档路由
app/responsibility.py 责任比例、责任平滑、赔付金额
app/store.py         SQLAlchemy 持久化，SQLite 默认，可切 MySQL
app/cache.py         Redis 幂等锁，未配置降级内存
app/llm.py           DeepSeek 客户端
app/prompts.py       System Prompt 与 Few-shot
app/api.py           REST 接口
```

## 运行

```bash
python -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
python run.py
```

接口文档 `/docs`。

## 接口

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/adjudicate` | 提交纠纷与证据，返回判责结果 |
| POST | `/review` | 人工复核，记录采纳或修正 |
| GET | `/disputes/{id}` | 查询已判责记录 |
| GET | `/stats` | 总量、自动率、转人工率、采纳率 |
| GET | `/health` | 引擎与缓存后端 |

## 评测

```bash
python scripts/seed_cases.py
python scripts/evaluate.py
```

## 配置

`.env.example` 里的变量，或直接设环境变量：

```
VERDICT_DB_URL=sqlite:///./verdict.db
REDIS_URL=
DEEPSEEK_API_KEY=
```
