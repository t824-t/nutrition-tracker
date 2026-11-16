
# Nutrition Tracker｜栄養管理Webアプリ

Java / Spring Bootを使用して個人開発した、食事・栄養・体重を一元管理できるWebアプリケーションです。

**[アプリを試す](https://taikaku-nutrition.duckdns.org/) ｜ [GitHub](https://github.com/t824-t/nutrition-tracker)**

## 概要・開発背景

日々の栄養管理を手軽に継続できるよう、食事記録から栄養バランスの可視化までを一つのアプリで行える仕組みを開発しました。

機能の実装だけでなく、ユーザー認証、データ管理、AWSへのデプロイまで一貫して取り組みました。

## 主な機能

- **食事管理**：食事の登録・編集・削除、検索、日別の栄養集計
- **栄養目標管理**：カロリー・PFCの目標設定と達成状況の可視化
- **体重管理**：体重の記録と推移グラフ
- **ユーザー管理**：ログイン認証とユーザーごとのデータ管理

## 使用技術

| 分類 | 技術 |
|---|---|
| バックエンド | Java 17 / Spring Boot / Spring Data JPA |
| フロントエンド | HTML / Thymeleaf / Bootstrap / Chart.js |
| 認証・セキュリティ | Spring Security / BCrypt |
| データベース | H2 |
| インフラ | AWS EC2 / nginx / HTTPS |
| 開発・管理 | IntelliJ IDEA / Git / GitHub / Maven |

## 開発で工夫した点

**1. 保守性を意識した設計**

MVC構成を採用し、Controller・Service・Repositoryの役割を分離しました。

**2. セキュリティを考慮した実装**

Spring Securityによる認証とBCryptによるパスワード保護を実装。ユーザーごとにデータを分離し、他ユーザーの記録にアクセスできない構成としました。

**3. AWSでの公開・運用**

AWS EC2にデプロイし、nginxによるリバースプロキシ、HTTPS通信、systemdによる自動起動を設定しました。

## 今後の改善

- CI/CDによるデプロイの自動化
- RDSへのデータベース移行
- 自動テスト・監視機能の充実

---

**開発者：** [t824-t](https://github.com/t824-t)
