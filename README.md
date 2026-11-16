
# Nutrition Tracker｜栄養管理Webアプリ

Java / Spring Bootを使用して個人開発した、食事・栄養・体重を一元管理できるWebアプリケーションです。

**[アプリを試す](https://taikaku-nutrition.duckdns.org/) ｜ [GitHub](https://github.com/t824-t/nutrition-tracker)**

## 概要・開発背景

日々の栄養管理を手軽に継続できるよう、食事記録から栄養バランスの可視化までを一つのアプリで行える仕組みを開発しました。

機能の実装だけでなく、ユーザー認証、データ管理、AWSへのデプロイまで一貫して取り組みました。

## 主な機能

- **食事管理**：食事の登録・編集・削除、検索、日別の栄養集計
- **栄養目標管理**：カロリー・PFCの目標設定と達成状況の可視化
- **体重管理**：体重の記録と日付順の推移グラフ
- **ダッシュボード**：栄養情報や体重推移の可視化
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

さらに、CSRF対策、セッションCookieのSecure・HttpOnly・SameSite属性の設定、本番環境でのH2コンソール無効化などを実施しました。

**3. AWSでの公開・運用**

AWS EC2にデプロイし、nginxによるリバースプロキシ、HTTPS通信、systemdによる自動起動を設定しました。

## テスト

Spring Bootの自動テストを導入し、画面表示や認証・セキュリティ関連の動作を検証しています。

## 今後の改善

- CI/CDによるデプロイの自動化
- RDSへのデータベース移行
- 自動テストの拡充・監視機能の充実

---

**開発者：** [t824-t](https://github.com/t824-t)
