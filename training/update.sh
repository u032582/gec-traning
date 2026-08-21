#!/usr/bin/env bash
#
# 教材を更新する — 先生が直した教材を、あなたの手元に取り込むだけ。
#
# 「送信」が あなた → 先生 の向きなら、こちらは 先生 → あなた の向きです。
# あなたが書いたコードには手を触れません。取り込むのは教材（training/ の中身）だけです。
# Git を知らなくても使えるように、難しい操作は全部この中に隠してあります。
# （この「更新」の正体＝Git の fetch / merge は、週4でちゃんと学びます。）
#
# 使い方:
#   bash training/update.sh
#
set -euo pipefail

# どこから呼ばれても動くように、リポジトリのルートへ移動する
cd "$(git rev-parse --show-toplevel)"

DEFAULT_BRANCH="master"

# --- 1. 書きかけを先に守る ---
# 未送信の変更を抱えたまま取り込むと、万一ぶつかったときに新人が
# 「自分の書いたものが消えたのでは」と不安になる。だから順番を固定する:
# 「送信してから更新する」。これだけで、書きかけを失う筋道が無くなる。
if ! git diff --quiet || ! git diff --cached --quiet || [ -n "$(git ls-files --others --exclude-standard)" ]; then
  echo "📝 まだ送信していない変更があります。"
  echo ""
  echo "   先に「📤 成果を送信」を押して（またはターミナルで bash training/send.sh を実行して）"
  echo "   から、もう一度この更新をしてください。"
  echo ""
  echo "   ※ 順番を守るだけです。あなたの書きかけが消えることはありません。"
  exit 1
fi

# --- 2. 先生の直しを取りに行く ---
echo "📥 先生の直しを取りに行っています..."
before="$(git rev-parse HEAD)"
if ! git fetch -q origin "$DEFAULT_BRANCH"; then
  echo ""
  echo "⚠️ 取りに行くのに失敗しました。多くはネット接続か、ログイン（認証）の問題です。"
  echo "   エラーの全文をコピーして、AIのAskモードか講師に相談してください（質問テンプレを使うと◎）。"
  exit 1
fi

# --- 3. 新しいものが無ければ、そこで終わり ---
if git merge-base --is-ancestor "origin/$DEFAULT_BRANCH" HEAD; then
  echo ""
  echo "✅ すでに最新です。取り込むものはありませんでした。そのまま課題に戻ってOKです。"
  exit 0
fi

# --- 4. 自分の手元に取り込む ---
# 出力は下の「何が新しくなったか」で整えて見せるので、git 自身の出力は抑える
if ! git merge -q --no-edit "origin/$DEFAULT_BRANCH" >/dev/null 2>&1; then
  conflicts="$(git diff --name-only --diff-filter=U || true)"
  # ぶつかった（コンフリクト）ときは、中途半端な状態で放り出さず、
  # 取り込む前の状態にそっと戻す。新人が独力で直すべき場面ではないため。
  git merge --abort || true
  echo ""
  echo "⚠️ 教材の直しと、あなたの手元の変更がぶつかりました（コンフリクト）。"
  echo "   安全のため、取り込む前の状態に戻してあります。あなたのコードは無事です。"
  echo ""
  echo "   ぶつかったファイル:"
  while IFS= read -r f; do
    [ -n "$f" ] && echo "     - $f"
  done <<< "$conflicts"
  echo ""
  echo "   このまま講師に連絡してください（上のファイル名をそのまま伝えればOKです）。"
  echo "   ※ 教材そのもの（training/ の中の README や課題ファイル）を書き換えていると起きます。"
  echo "     自分のコードは work/あなたのハンドル/ の下に書く、が原則です。"
  exit 1
fi

# --- 5. 何が新しくなったのかを見せる ---
updates="$(git log --oneline --no-merges "$before..HEAD")"
changed="$(git diff --stat "$before" HEAD)"

echo ""
echo "🎉 教材を最新にしました！ 入ってきた直しはこれです:"
echo ""
echo "$updates"
echo ""
echo "変更のあったファイル:"
echo "$changed"
echo ""
echo "気になる直しがあれば、そのファイルを開いて読んでみてください。"
