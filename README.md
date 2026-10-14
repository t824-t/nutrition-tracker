# Nutrition Tracker（栄養管理アプリ）

## 1. アプリ概要

日々の食事・栄養摂取量・体重を記録し、健康管理をサポートするWebアプリケーションです。

食事ごとのカロリーやPFC（たんぱく質・脂質・炭水化物）を記録し、1日の摂取量と目標値を比較できます。また、体重の推移をグラフで確認できます。

## 2. 開発目的

日々の栄養管理をより簡単に継続できる仕組みを作ることを目的として開発しました。

JavaとSpring Bootを用いたWebアプリケーション開発を通じて、MVC構成、データベース操作、入力値検証、画面表示などの技術を実践的に学ぶことも目的としています。

## 3. 主な機能

- 食事の登録・一覧表示・詳細表示・編集・削除
- 日付ごとの食事記録管理
- 食事名による検索
- 新しい順・古い順の並び替え
- ページネーション
- 1日単位のカロリー・PFC集計
- 栄養目標の設定と進捗バー表示
- 体重の登録・履歴表示・削除
- 体重推移のグラフ表示
- 入力値のバリデーションとエラーメッセージ表示
- エラー画面の表示

## 4. 使用技術

| 分類 | 技術 |
|---|---|
| 言語 | Java 17 |
| フレームワーク | Spring Boot 3.5.16 |
| テンプレートエンジン | Thymeleaf |
| フロントエンド | HTML / Bootstrap 5 |
| グラフ | Chart.js |
| データベース | H2（ローカル開発） |
| 本番DB対応 | MySQL Connector/J |
| ORM | Spring Data JPA / Hibernate |
| バリデーション | Jakarta Validation |
| ビルドツール | Maven |
| バージョン管理 | Git / GitHub |
| 開発環境 | IntelliJ IDEA |

## 5. ローカルでの起動方法

### 前提条件

- Java 17以上
- Git

### 起動手順

リポジトリをクローンします。

```bash
git clone https://github.com/t824-t/nutrition-tracker.git
cd nutrition-tracker
```

Windowsの場合：

```powershell
.\mvnw.cmd spring-boot:run
```

起動後、ブラウザで以下にアクセスします。

http://localhost:8080

ローカル環境ではH2データベースを使用します。

## 6. AWS本番環境（構築予定）

AWS上へのデプロイを予定しています。

本番環境ではMySQLを利用し、接続情報は環境変数で管理する構成を想定しています。

使用予定の環境変数：

- `SPRING_PROFILES_ACTIVE=prod`
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

デプロイ完了後、AWS構成図と公開URLを追記します。

## 7. 今後の改善予定

- ユーザー認証・ログイン機能
- CI/CDによる自動デプロイ
- AWS上での運用・監視
- UI・UXの改善

## 8. 開発者

GitHub: [t824-t](https://github.com/t824-t)
