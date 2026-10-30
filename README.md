<div align="center">

# ☕ Java AI Engineer — Volume 5: Architecture & System Design
### *Enterprise Scale, Event-Driven AI, Multi-Tenancy & Case Studies*

[![Volume](https://img.shields.io/badge/Volume-5%20of%206-blue?style=for-the-badge)](https://javapuzzle.com)
[![Java](https://img.shields.io/badge/Java-21+-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org)
[![Kafka](https://img.shields.io/badge/Apache%20Kafka-Event--Driven-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)](https://kafka.apache.org)
[![Architecture](https://img.shields.io/badge/System%20Design-Enterprise-0052CC?style=for-the-badge)](https://javapuzzle.com)

**Part of the 6-volume [Java AI Engineer](https://javapuzzle.com) series by Java Puzzle**

</div>

---

## 📖 About This Volume

When Acme Retail decides to license the platform underneath its Autonomous Ops Agent to external retailers, Sarah must design an AI platform from a clean slate at genuine enterprise scale.

Volume 5 goes beyond coding individual features to architecting scalable, resilient AI-native systems. Crucially, **no chapter is conceptual-only**: every architectural concept is paired with a real, compilable Java mechanism proving the architectural tradeoffs mechanically:
- **Architectural Foundations**: Enforced layering rules, event-driven streaming with Apache Kafka, data contracts, and P50–P99 tail latency optimization.
- **Enterprise Scale**: Vector sharding across millions of embeddings, multi-tenant noisy-neighbor isolation, and disaster recovery.
- **Real-World Case Studies**: End-to-end designs for Global RAG Platforms, Multi-Agent Customer Support, Real-Time Fraud Detection, and Recommendation Engines.
- **Governance & Migration**: Architecture Decision Records (ADRs), capacity estimation, and migrating legacy monoliths to AI-native architectures.

---

## 🗺️ Chapter & Code Index

| Chapter | Title | Focus Area / Key Mechanism |
|:---:|---|---|
| **01** | `chapter-01` | **Architecting AI-Native Enterprise Systems** — Layering validation: Domain, Gateways, Infrastructure |
| **02** | `chapter-02` | **Event-Driven Architecture for AI Pipelines** — Kafka-backed asynchronous ingestion & inference events |
| **03** | `chapter-03` | **Data Architecture for AI** — Schema enforcement, schema evolution, and strict data contracts |
| **04** | `chapter-04` | **Designing for Latency: P50 to P99** — Tail latency budgets, hedged requests, speculative execution |
| **05** | `chapter-05` | **Scaling RAG to Millions of Documents** — Consistent hashing and sharded vector index routing in Java |
| **06** | `chapter-06` | **Multi-Tenant AI Architecture** — Tenant isolation, noisy neighbor throttling, rate caps |
| **07** | `chapter-07` | **Hybrid and On-Premise AI Deployment Models** — Air-gapped deployments, hybrid cloud routing |
| **08** | `chapter-08` | **Choosing Between Managed and Self-Hosted Models** — Cost-latency evaluation matrix and runtime switchboard |
| **09** | `chapter-09` | **Designing AI Systems for High Availability** — Active-active multi-region failover and consensus |
| **10** | `chapter-10` | **Disaster Recovery and Business Continuity** — Vector index snapshots, backup pipelines, cold-start replay |
| **11** | `chapter-11` | **The AI System Design Interview Framework** — 4-step framework for communicating AI architecture |
| **12** | `chapter-12` | **Case Study: Designing a Global RAG Platform** — Multi-region document distribution and localized embeddings |
| **13** | `chapter-13` | **Case Study: Multi-Agent Customer Support System** — Routing, specialist agents, escalation, and SLAs |
| **14** | `chapter-14` | **Case Study: Real-Time AI Fraud Detection Pipeline** — Streaming anomaly scoring under 50ms latency SLAs |
| **15** | `chapter-15` | **Case Study: Real-Time Recommendation Engine** — Vector retrieval + collaborative filtering hybrid ranking |
| **16** | `chapter-16` | **Tradeoff Analysis: Build vs. Buy for AI Components** — TCO calculation models implemented in Java |
| **17** | `chapter-17` | **Capacity Estimation for AI Systems** — Back-of-the-envelope estimation formulas converted to calculators |
| **18** | `chapter-18` | **Documenting Architecture Decisions at Scale** — Architecture Decision Records (ADRs) with automated checks |
| **19** | `chapter-19` | **Migrating a Legacy System to an AI-Native Architecture** — Strangler Fig pattern applied to legacy monoliths |
| **20** | `chapter-20` | **Target Architecture for Acme Retail (Capstone)** — The complete, multi-tenant enterprise target platform |

---

## 🛠️ Tech Stack & Prerequisites

- **Java**: 21 LTS or newer
- **Messaging**: Apache Kafka / Spring Cloud Stream
- **Architecture Validation**: ArchUnit, custom Java layer validators
- **Build Tool**: Maven 3.9+

## 🚀 Building & Running Chapter Code

```bash
mvn clean test
```

Step into any chapter to run tests:
```bash
cd chapter-01
mvn test
```

---

<div align="center">
<sub>© Java Puzzle. All rights reserved. Visit <a href="https://javapuzzle.com">javapuzzle.com</a> to explore the entire 6-volume series.</sub>
</div>
