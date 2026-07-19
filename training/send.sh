#!/usr/bin/env bash
#
# 成果を送信する — きょう書いたぶんを、先生が見られる場所（あなたのブランチ）へ送るだけ。
#
# これは「提出」でも「採点」でもありません。日報と同じ、毎日の“見せる”習慣です。
# Git を知らなくても使えるように、難しい操作は全部この中に隠してあります。
# （この「送信」の正体＝Git の commit / push は、週4でちゃんと学びます。）
#
# 使い方:
#   bash training/send.sh            … きょうのぶんを送信する
#   bash training/send.sh <ハンドル> … 初回だけ。自分の短い名前を登録（例: bash training/send.sh tanaka）
#
set -euo pipefail

# どこから呼ばれても動くように、リポジトリのルートへ移動する
cd "$(git rev-parse --show-toplevel)"

DEFAULT_BRANCH="master"

# --- 1. 自分のハンドル（英小文字の短い名前）を用意する（初回だけ） ---
handle="$(git config training.handle 2>/dev/null || true)"
arg_handle="${1:-}"
[ -n "$arg_handle" ] && handle="$arg_handle"

if [ -z "$handle" ]; then
  if [ -t 0 ]; then
    echo "はじめての送信ですね。まず、あなたの『ハンドル』を決めます。"
    echo "（半角英小文字の短い名前。ローマ字でOK。例: tanaka, yuki, luka01）"
    printf "ハンドル: "
    read -r handle
  else
    echo "⚠️ まだハンドルが未登録です。次のように一度だけ登録してください:"
    echo "    bash training/send.sh あなたの名前   （例: bash training/send.sh tanaka）"
    exit 1
  fi
fi

# ハンドルの形をチェック（ブランチ名・フォルダ名に使うので、安全な文字だけに限る）
if ! printf '%s' "$handle" | grep -Eq '^[a-z][a-z0-9_-]*$'; then
  echo "⚠️ ハンドルは英小文字で始め、英数字・ハイフン(-)・アンダースコア(_)だけにしてください。"
  echo "   OK例: tanaka / yuki01 / luka-b     NG例: 田中 / Tanaka / yuki tanaka"
  exit 1
fi
git config training.handle "$handle"

# --- 2. 自分のブランチにいることを保証する ---
# 週1〜3: まだ自分でブランチを切らない → あなた専用の work/<ハンドル> に乗せる。
# 週4〜 : 自分で feature ブランチを切っていれば、そのブランチのまま送る。
current="$(git branch --show-current 2>/dev/null || true)"
if [ -z "$current" ] || [ "$current" = "$DEFAULT_BRANCH" ]; then
  branch="work/$handle"
  if git show-ref --verify --quiet "refs/heads/$branch"; then
    git switch "$branch"
  else
    git switch -c "$branch"
    echo "🌱 あなた専用のブランチ『$branch』を作りました。"
  fi
else
  branch="$current"
fi

# --- 3. きょうのぶんをまとめて記録する ---
git add -A
if git diff --cached --quiet; then
  echo "（前回の送信から、変更されたファイルはありませんでした）"
else
  msg="送信: $(date '+%Y-%m-%d %H:%M')"
  git commit -m "$msg" >/dev/null
  echo "📝 記録しました： $msg"
fi

# --- 4. 先生が見られる場所へ送る ---
echo "📤 送信しています..."
if ! git push -u origin "$branch"; then
  echo ""
  echo "⚠️ 送信（push）に失敗しました。多くはネット接続か、ログイン（認証）の問題です。"
  echo "   エラーの全文をコピーして、AIのAskモードか講師に相談してください（質問テンプレを使うと◎）。"
  exit 1
fi

# 見に行けるURLを表示（HTTPS/SSH どちらのリモートでも整形する）
url="$(git remote get-url origin 2>/dev/null | sed -E 's#^git@github\.com:#https://github.com/#; s#\.git$##')"
echo ""
echo "🎉 送信できました！ 先生はこのURLで、きょうのあなたの成果を見られます:"
echo "    ${url}/tree/${branch}"
echo ""
echo "きょうもおつかれさまでした。日報もわすれずに！"
