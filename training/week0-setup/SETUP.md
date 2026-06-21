# 週0前半: 環境構築 — 開発できるPCを用意する（Windows + WSL2 前提）

> 📕 **この研修で最初にやるのが、この環境構築です。** 研修ドキュメントは、IDE **Antigravity** の **MPE拡張**（Markdownをきれいに表示する機能）で読む想定なので、まず環境を整え、このリポジトリを `git clone` する必要があります。手順の最後（[ステップ5](#ステップ5-リポジトリをcloneしてmpeでドキュメントを表示する)）で clone と MPE 表示まで行います。
>
> ⚠️ ここには「ターミナル」「ファイルパス」「OS」などの言葉が出てきますが、**いま全部を理解する必要はありません**。まずは手順どおりに手を動かして環境を作ることが目標です。言葉の意味は、環境が整ったあとに読む [week00-it-basics/README.md](../week00-it-basics/README.md)（IT基礎の座学）でじっくり解説します。「あれはこういう意味だったのか」と後でつながる作りにしているので、ここでは**作業を進めることを優先**してください。

> 用語メモ: **環境構築（かんきょうこうちく）** … プログラムを書いて動かすために、必要な道具をPCに入れて設定すること。

## この研修の環境（必ず最初に読む）

この研修は **Windows PC + WSL2（ダブリューエスエルツー）** の構成で進めます。

> 用語メモ: **WSL2** … Windowsの中でLinux（リナックス。サーバーでよく使われるOS）を動かす仕組み。正式名称は Windows Subsystem for Linux。**現場のサーバーはほぼLinux**なので、最初からLinux上で開発に慣れておくと現場でそのまま通用します。

**役割分担（これを頭に入れておくと迷いません）:**

| 道具 | どこに入れるか | 理由 |
|---|---|---|
| ソースコード | **WSL2(Linux)側** | ビルドが速く、現場と同じ環境。改行コードの事故も防げる |
| JDK（Java本体）| **WSL2(Linux)側** | コードと同じ側に置く |
| Git | **WSL2(Linux)側** | 同上 |
| PostgreSQL（DB）| **WSL2(Linux)側** | Javaから `localhost` でそのまま繋がる |
| IDE（Antigravity）| **Windows側** | 画面操作はWindowsが快適。WSL2上のコードを開いて使う |

つまり「**書く・動かす・DBはぜんぶLinux側**、**画面（IDE）だけWindows側**」という構成です。

## この週の到達目標
- WSL2(Linux)上で **Javaプログラムをビルド・実行できる**
- **Git** で変更を記録できる
- **PostgreSQL**（データベース）に接続できる
- WindowsのIDEから **WSL2上のコードを開ける**
- このファイル末尾の **動作確認チェックリスト** がすべて✅になる

## AI利用区分
🚫 コードを書かせるのは禁止。**家庭教師用途のみ**（例: 「このエラーの意味を教えて」）。

---

## はじめに: つまずいたときの心構え

環境構築は、**全工程で一番ハマりやすい**ところです。バージョン違いやインストール失敗で、誰でも一度は詰まります。それは普通のことなので焦らないでください。

各ステップに **「確認コマンド」** を付けています。コマンドを叩いて、書いてある「期待される出力」と近い結果が出れば、そのステップはOKです。出ない場合は「つまずきポイント」を見てください。

> 用語メモ: **ターミナル** … 文字でPCに命令する画面。この研修では、WSL2を起動した後の **Ubuntu（ウブントゥ）のターミナル** を使います（次のステップで開けるようになります）。以下「**WSL2のターミナル**」と書いたら、このUbuntu画面のことです。
> 用語メモ: **コマンド** … ターミナルに打ち込む命令文。`$` で始まる行はコマンドの例で、`$` 自体は打ちません（その後ろを打ちます）。
> 用語メモ: **sudo（スードゥ）** … 「管理者の権限でこのコマンドを実行する」という意味。Linuxで何かをインストールするときによく使います。初回はパスワード（後で作るLinuxユーザーのパスワード）を聞かれます。

---

## ステップ0: WSL2 を入れて Ubuntu を使えるようにする

これが土台です。ここができれば後は楽になります。

### やること
1. Windowsの検索バーで **「PowerShell」** を探し、**右クリック →「管理者として実行」** で開きます。
2. 次のコマンドを1行打って実行します（これだけでWSL2 + Ubuntu がまとめて入ります）。
```powershell
wsl --install
```
3. 指示に従ってPCを **再起動** します。
4. 再起動後、自動で Ubuntu のセットアップ画面（黒い画面）が立ち上がります。立ち上がらない場合はWindowsの検索バーで **「Ubuntu」** を探して起動します。
5. 初回起動で **Linuxのユーザー名とパスワード** を作るよう求められます。
   - ユーザー名: 半角英小文字（例: `shinjin`）。
   - パスワード: **必ずメモ**してください。`sudo` のときに使います（入力中は画面に何も表示されませんが打ててはいます）。

> ⚠️ WindowsのユーザーパスワードとLinuxのパスワードは**別物**です。混同しないように。

### 確認コマンド
Ubuntuのターミナルで次を実行します。
```bash
uname -a
whoami
```
**期待される出力**（Linuxであること、自分のユーザー名が出ればOK）:
```
Linux ... (中略) ... GNU/Linux
shinjin   ← 自分で決めたユーザー名
```

### パッケージ一覧を最新にしておく（最初に一度だけ）
これからソフトを入れるための準備です。Ubuntuのターミナルで:
```bash
sudo apt update
```
> 用語メモ: **apt（アプト）** … Ubuntuでソフトを入れるための道具。`apt update` は「入れられるソフトの最新リストを取りに行く」コマンドです。

### つまずきポイント
<details>
<summary>「wsl は認識されていません」と出る / wsl --install が失敗する</summary>

Windowsが古いとwslコマンドが無いことがあります。Windows Updateで最新にしてください。それでもダメなら、AIに「Windows10 / 11 でWSL2を手動で有効化する手順」を聞くか、講師に質問テンプレートで相談してください。
</details>

<details>
<summary>Ubuntuの初回セットアップ画面が出てこない</summary>

Windowsの検索バーで「Ubuntu」を探して起動すると、初回セットアップ（ユーザー名・パスワード作成）が始まります。
</details>

---

## ステップ1: JDK（Java本体）を入れる ＝ WSL2(Linux)側

> 用語メモ: **JDK（ジェイディーケー）** … Javaを書いて動かすための本体一式（Java Development Kit）。

### やること
**WSL2のターミナル**で、**JDK 21**（LTS版＝長く安定して使えるバージョン）を入れます。Ubuntu標準のaptで入る Temurin/OpenJDK 系を使います。
```bash
sudo apt update
sudo apt install -y openjdk-21-jdk
```

### 確認コマンド
```bash
java -version
javac -version
```
**期待される出力**（`21` で始まっていれば、細かい数字は違っても構いません）:
```
openjdk version "21.0.x" ...
javac 21.0.x
```

### つまずきポイント
<details>
<summary>「openjdk-21-jdk が見つからない / Unable to locate package」</summary>

`sudo apt update` を先に実行していないか、Ubuntuのバージョンが古い可能性があります。まず `sudo apt update` を実行してから再度入れてください。それでも無い場合は `sudo apt install -y openjdk-21-jdk-headless` を試すか、AIに「Ubuntu に OpenJDK 21 を apt で入れる方法」を聞いてください。
</details>

<details>
<summary>java -version は21なのに javac が違うバージョン / 出ない</summary>

複数のJavaが入っていると起きます。`sudo update-alternatives --config java` と `sudo update-alternatives --config javac` で21を選べます。AIに「update-alternatives でJavaのバージョンを切り替える方法」を聞くと早いです。
</details>

---

## ステップ2: Git を入れる ＝ WSL2(Linux)側

> 用語メモ: **Git（ギット）** … コードの変更履歴を記録する道具。「いつ・誰が・どこを変えたか」を残し、間違えても前に戻せます。現場では必須です。

### やること
**WSL2のターミナル**で:
```bash
sudo apt install -y git
```
続いて、**自分の名前とメールを設定**します（誰が変更したかを記録するため）。
```bash
git config --global user.name "あなたの名前"
git config --global user.email "あなたのメール@example.com"
```

### 確認コマンド
```bash
git --version
git config --global user.name
git config --global user.email
```
**期待される出力**:
```
git version 2.x.x
（設定した名前）
（設定したメール）
```

### つまずきポイント
<details>
<summary>名前やメールが空で返ってくる</summary>

`git config --global user.name "..."` の設定が効いていません。クォート `"` の付け忘れなどタイプミスがないか確認して、もう一度実行してください。
</details>

---

## ステップ3: IDE（Antigravity）を入れる ＝ Windows側

> 用語メモ: **IDE（アイディーイー）** … コードを書く専用の高機能エディタ。補完やエラー表示、実行ボタンが付いています。この研修では **Antigravity（アンチグラビティ）** を使います。
> 用語メモ: **Antigravity** … AI機能を内蔵したIDE。ふつうのエディタとしても使え、さらにAIに質問したりコードを書かせたりできます。**この「AI機能」の使い方には研修ルールがあります**（後述。週1〜2はAIに書かせない）。

この研修では **IDEだけWindows側**に入れ、**WSL2上のコードを開いて**使います。

### やること
1. Windowsのブラウザで `Antigravity IDE download` と検索し、公式サイトからWindows版をダウンロード。
2. **Windowsに**インストールして起動。最初の起動でサインインや初期設定を求められたら、画面の指示に従います（無料で始められます）。
3. Antigravityが起動できればOK。最初のプロジェクトを開くのは週1で行います。

### WSL2上のコードを開けるようにする（重要）
AntigravityはVS Code系のIDEなので、**WSL連携の拡張機能**を入れると、WSL2上のフォルダを直接開いて編集できます。
1. Antigravityの拡張機能（Extensions）から **「WSL」** を検索してインストール。
2. 左下の緑色の `><` のようなボタン（リモート接続）から **「Connect to WSL」** を選ぶと、WSL2(Ubuntu)に接続した状態でIDEが開きます。
3. その状態で「フォルダを開く」→ `/home/（ユーザー名）/` を開けばOKです。
- 週1以降、コードはこの **WSL2側（`/home/ユーザー名/`）** に作り、Antigravityで開いて編集 → ビルド・実行は **WSL2のターミナル**（Antigravity内のターミナルでも可）で行う、という流れになります。

> 💡 なぜコードをWSL2側に置くのか: Windows側(`C:\...`)にコードを置いてWSL2からビルドすると、ファイルアクセスが非常に遅くなります。**コードはLinux側のホーム（`/home/ユーザー名/`）に置く**のが鉄則です。これは現場でも同じ考え方です。

### ⚠️ 週1〜2は「AI機能をOFF」にして使う（最重要）
Antigravityは便利なAI機能を持っていますが、**週1〜2（基礎フェーズ）ではコードを書かせる使い方は禁止**です（理由は[研修トップのAI利用ルール](../README.md#2-ai利用ルール最重要必ず守る)を参照）。基礎期は次の設定で使ってください。

- 🚫 **AIエージェント（Agent）モード** … 使わない。
- 🚫 **インラインのコード補完**（タイピング中に続きを勝手に提案する機能）… **OFFにする**。これがONだと、自分で考える前に答えが出てしまい、基礎力が付きません。設定（Settings）で補完系の機能をオフにしてください。やり方が分からなければAIに「（IDE名）でAIのコード補完をオフにする方法」を聞いてOKです。
- ✅ **Ask（チャットで質問）モード** … **使ってよい**。これは「家庭教師」用途（エラーの意味を聞く・自分のコードのレビュー・概念説明）に当たり、週1〜2でも許可されています。Askモードは質問に答えるだけで、あなたのコードを勝手に書き換えません。

週3〜4（応用フェーズ）になったら、AIエージェントも補完もONにして、AI活用を解禁します（ただし「生成コードを自分の言葉で説明できること」が条件）。

### つまずきポイント
<details>
<summary>WSL2のフォルダ（/home/ユーザー名）が開けない</summary>

WSL連携拡張が入っていない／WSL2が起動していないと開けません。拡張機能で「WSL」を入れ、一度WSL2のターミナル（Ubuntu）を起動した状態で、左下の `><` から「Connect to WSL」を選んでください。
</details>

<details>
<summary>AntigravityがJDKを見つけてくれない</summary>

WSL2側に入れたJDKをIDEに教える必要がある場合があります。WSLに接続した状態でプロジェクトを開いていれば、WSL2側のJDK21が使われます。うまくいかない場合はAIに「（IDE名）でWSL2上のJDK21をプロジェクトに設定する手順」を聞いてください。週1の最初に詳しく扱います。
</details>

<details>
<summary>IntelliJ / Eclipse を使いたい</summary>

この研修の標準IDEはAntigravityです。慣れている人がIntelliJ等を使うこと自体は妨げませんが、手順書はAntigravity前提で書かれています。IntelliJ/Eclipseを使う場合も「IDEはWindows側・コードとビルドはWSL2側」の原則は同じです。
</details>

---

## ステップ4: PostgreSQL（データベース）を入れる ＝ WSL2(Linux)側

> 用語メモ: **PostgreSQL（ポストグレスキューエル、略してポスグレ）** … データを保存・検索するためのデータベース。週2以降で使います。

### やること（WSL2のターミナルで）
1. PostgreSQL本体を入れます。
```bash
sudo apt install -y postgresql postgresql-contrib
```
2. PostgreSQLを **起動** します（Linuxではサービスを自分で起動します）。
```bash
sudo service postgresql start
```
3. 起動できたか確認します。
```bash
sudo service postgresql status
```
`online` や `active` のような表示が出ればOKです。

> ⚠️ **WSL2はPCを再起動するとPostgreSQLが止まります。** 作業を始めるときは毎回 `sudo service postgresql start` を実行してください（これは正常な動作です）。

### 管理ユーザー postgres のパスワードを決める
PostgreSQLには最初から `postgres` という管理ユーザーがいます。Javaから接続するために、このユーザーのパスワードを設定します。

1. まず管理ユーザーとしてPostgreSQLに入ります。
```bash
sudo -u postgres psql
```
`postgres=#` というプロンプトに変わったら、入れています。

2. パスワードを設定します（`postgres` の部分は研修では分かりやすくこのままでも構いません。本番では絶対にこんな簡単なものにしないこと）。
```sql
ALTER USER postgres PASSWORD 'postgres';
```
`ALTER ROLE` と表示されれば成功です。**このパスワードは必ずメモ**してください。

3. 続けて、**週2で使う練習用データベース** を作っておきます。同じ `postgres=#` の画面で:
```sql
CREATE DATABASE training;
```
`CREATE DATABASE` と出ればOK。終わったら抜けます。
```sql
\q
```

> 用語メモ: **psql（ピーエスキューエル）** … PostgreSQLにコマンドで命令するための道具。`\q` で終了します。
> 用語メモ: **データベースを作る（CREATE DATABASE）** … データをしまう「引き出し」を1つ用意するイメージ。

### 確認コマンド（パスワードで接続できるか試す）
Javaは「ホスト名・ポート・ユーザー・パスワード」で接続します。同じ方法で繋がるか確認します。WSL2のターミナルで:
```bash
psql -h localhost -U postgres -d training
```
パスワードを聞かれたら、先ほど設定したパスワード（例: `postgres`）を入力します。
**期待される出力**（接続成功するとこのプロンプトになる）:
```
training=#
```
試しにバージョンを確認します。
```sql
SELECT version();
```
PostgreSQL 16.x ... のように表示されればOKです。確認できたら `\q` で終了します。

> 💡 `-h localhost` を付けるのが大事です。これを付けると、Javaと同じ「パスワードで繋ぐ方式」での接続を確認できます。付けないとLinuxのユーザー権限で繋がってしまい、Javaからの接続確認になりません。

### つまずきポイント
<details>
<summary>「psql: error ... Connection refused」「could not connect」</summary>

PostgreSQLが起動していません。`sudo service postgresql start` を実行してから、もう一度試してください。WSL2は再起動のたびに止まるので、ここは何度も出会うエラーです。
</details>

<details>
<summary>「password authentication failed for user "postgres"」</summary>

メモしたパスワードと違います。`sudo -u postgres psql` で入り直し、`ALTER USER postgres PASSWORD 'postgres';` でもう一度設定し直してください（研修段階なのでデータは消えても問題ありません）。
</details>

<details>
<summary>`sudo -u postgres psql` 自体が「could not change directory」などの警告を出す</summary>

警告が出ても `postgres=#` のプロンプトに入れていれば問題ありません。気にせず進めてください。
</details>

<details>
<summary>PostgreSQLのバージョンが16でない（15や17など）</summary>

aptで入るバージョンはUbuntuの版で多少前後します。**15〜17 のいずれかなら、この研修ではそのまま進めて構いません**（SQLの内容は共通です）。
</details>

---

## ステップ5: リポジトリをcloneして、MPEでドキュメントを表示する

ここまでで道具がそろいました。最後に、**この研修ドキュメント自体を、読みやすい形で表示できるようにします。** この研修の資料は、Antigravity の **MPE拡張**（Markdownをきれいに整形して表示する機能）で読む想定です。

> 用語メモ: **リポジトリ** … プロジェクトのファイル一式と、その変更履歴をまとめて管理する入れ物（Gitで管理する単位）。
> 用語メモ: **clone（クローン）** … リモート（ネット上）にあるリポジトリを、自分のPCに丸ごとコピーして持ってくること。
> 用語メモ: **Markdown（マークダウン）** … 文章を見出し・箇条書き・表などの構造付きで書くための、軽い記法。この研修資料（`.md`ファイル）はMarkdownで書かれています。
> 用語メモ: **MPE（Markdown Preview Enhanced）** … `.md`ファイルを、見出しや表が整った「読み物」の見た目で表示してくれる拡張機能。

### 1. リポジトリを clone する（WSL2側に）
**WSL2のターミナル**で、コードと同じく**WSL2側のホーム**に持ってきます。
```bash
cd ~
git clone （研修リポジトリのURL）
```
> ⚠️ `（研修リポジトリのURL）` の部分は、講師から共有されるURLに置き換えてください。`git clone https://github.com/～.git` のような形です。
> 💡 cloneすると、ホームの下に研修フォルダができます（例: `~/gec-traning`）。**必ずWSL2側（`/home/ユーザー名/`の下）に置く**こと。Windows側に置くと遅くなります（理由はステップ3の💡参照）。

### 2. Antigravityでそのフォルダを開く（WSL接続状態で）
1. Antigravityを起動し、左下の `><`（リモート接続）から **「Connect to WSL」** を選ぶ。
2. 「フォルダを開く」で、cloneした研修フォルダ（例: `/home/ユーザー名/gec-traning`）を開く。

### 3. MPE拡張を入れる
1. Antigravityの拡張機能（Extensions）で **「Markdown Preview Enhanced」** を検索してインストール。
2. 念のため、WSL側にも有効化が必要な場合は「Install in WSL」を選ぶ（ボタンが出たら）。

### 4. ドキュメントをMPEで表示する
1. 研修フォルダの中の `training/README.md` を開く。
2. ファイルを右クリック →「Markdown Preview Enhanced: Open Preview」を選ぶ（またはコマンドパレットで `Markdown Preview Enhanced` と打つ）。
3. 右側に、見出しや表が整ったきれいな表示（プレビュー）が出ればOK。**これ以降の研修は、この表示で読み進めます。**

### つまずきポイント
<details>
<summary>git clone で「Permission denied」「認証」関連のエラーが出る</summary>

非公開リポジトリの場合、アクセス権やログインが必要です。講師から共有された方法（HTTPSのURL＋トークン、またはSSHの設定）に従ってください。まずはエラー全文をAIのAskモードに貼って「このgitのエラーの意味と対処を初心者向けに教えて」と聞き、それでも分からなければ質問テンプレートで講師に相談を。
</details>

<details>
<summary>MPEのプレビューが表示されない / 拡張が見つからない</summary>

WSL接続状態で拡張を入れているか確認してください（ローカルだけに入れてもWSL側のファイルには効かないことがあります）。拡張のページに「Install in WSL: Ubuntu」ボタンが出ていたら押します。それでもダメなら、いったんAntigravityを再起動してから再度プレビューを試してください。
</details>

---

## 動作確認チェックリスト（これが全部✅で週0クリア）

**WSL2のターミナル**で上から順にコマンドを叩き、期待される結果になることを自分で確認してください。**一人で判定できます**。

- [ ] WSL2が入り、Ubuntuのターミナルで `whoami` に自分のユーザー名が出る
- [ ] `java -version` → `21.0.x` と表示される（WSL2側）
- [ ] `javac -version` → `javac 21.0.x` と表示される（WSL2側）
- [ ] `git --version` → `git version 2.x.x` と表示される（WSL2側）
- [ ] `git config --global user.name` → 自分の名前が表示される
- [ ] `git config --global user.email` → 自分のメールが表示される
- [ ] Antigravity（Windows側）が起動し、WSL連携でWSL2上の `/home/（ユーザー名）` を開ける
- [ ] `sudo service postgresql start` でPostgreSQLを起動できる
- [ ] `psql -h localhost -U postgres -d training` でパスワード接続でき、`SELECT version();` で PostgreSQL が表示される
- [ ] 練習用DB `training` を作成済み
- [ ] 研修リポジトリを `git clone` し、WSL2側のホームに置けた
- [ ] AntigravityでcloneしたフォルダをWSL接続で開き、MPE拡張で `training/README.md` をプレビュー表示できた

全部✅になったら、環境構築は完了です。次は [week00-it-basics/README.md](../week00-it-basics/README.md)（IT基礎の座学）に進み、ここで出てきた言葉の意味と仕組みを理解しましょう。

---

## 動作確認（最終）: Javaが本当に動くか試す

最後に、Javaプログラムを1本だけ書いて実行してみます。これができれば週1にスムーズに入れます。**WSL2側**で行います。

1. WSL2のターミナルで、ホームに練習用フォルダを作って移動します。
```bash
mkdir -p ~/hello && cd ~/hello
```
2. `Hello.java` を作ります。Antigravity（WSL接続状態）で `/home/ユーザー名/hello` を開いて作ってもよいですし、ターミナルで次のように作っても構いません。
```bash
cat > Hello.java <<'EOF'
public class Hello {
    public static void main(String[] args) {
        System.out.println("環境構築おつかれさまでした");
    }
}
EOF
```
3. ビルドして実行します。
```bash
javac Hello.java
java Hello
```
**期待される出力**:
```
環境構築おつかれさまでした
```

> 💡 Antigravityで実行する場合は、WSL接続した状態でこのフォルダを開き、`Hello.java` の `main` 横に出る実行ボタン（▶）から実行できます（JDKはWSL2側の21が使われます）。どちらの方法でもこの文字が出れば、あなたの開発環境は完全に整っています。

このメッセージが出たら、環境構築は完了です。次は [week00-it-basics/README.md](../week00-it-basics/README.md)（IT基礎の座学）へ進み、ここまでで触れた言葉と仕組みをじっくり理解しましょう。

> 💡 週1に入る前の「最初の成功体験」として、[week1-java-basics/exercises/00-first-run.md](../week1-java-basics/exercises/00-first-run.md)（自分の名前を画面に出す）も用意しています。IT基礎のあと、週1の最初に取り組むとスムーズです。

---

## 付録: 毎日の作業を始めるときのルーティン（覚えておくと楽）

WSL2は再起動するとDBが止まるなど、毎回の「起動の儀式」があります。慣れるまでこの順で。

1. Ubuntu（WSL2のターミナル）を起動する。
2. PostgreSQLを使う日は起動する: `sudo service postgresql start`
3. Antigravity（Windows側）を起動し、WSL連携でWSL2上のプロジェクトを開く。
4. ビルド・実行・`psql`・`git` は **WSL2のターミナル** で行う。

> このルーティン自体が、現場で毎朝サーバーやサービスの状態を確認する作業の練習になっています。
