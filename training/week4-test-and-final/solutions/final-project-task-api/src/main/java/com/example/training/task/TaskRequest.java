package com.example.training.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * 作成・更新リクエストのJSONを受け取る入れ物（DTO）。
 *
 * <p>用語メモ: <b>DTO（ディーティーオー）</b> … データを運ぶためだけの箱。
 * 画面やクライアントとのやり取り（JSON）専用にし、DB用の {@link Task} と分けておく。
 * こうすると「JSONの形」と「テーブルの形」を別々に変えられて安全。
 *
 * <p>各フィールドに付いている注釈（@NotBlank など）が入力チェックの条件。
 * Controller で {@code @Valid} を付けると、ここの条件に違反したリクエストは
 * 自動で 400 エラーになる。
 */
public class TaskRequest {

    /** タイトル: 必須・空白だけは不可・100文字以内。 */
    @NotBlank(message = "titleは必須です")
    @Size(max = 100, message = "titleは100文字以内で入力してください")
    private String title;

    /** 説明: 任意・500文字以内。 */
    @Size(max = 500, message = "descriptionは500文字以内で入力してください")
    private String description;

    /**
     * ステータス: 必須・"todo" / "doing" / "done" のいずれか。
     * 正規表現（Pattern）で許可する値を縛っている。
     */
    @NotBlank(message = "statusは必須です")
    @Pattern(regexp = "todo|doing|done", message = "statusは todo / doing / done のいずれかです")
    private String status;

    /** 締め切り日: 任意。形式は "yyyy-MM-dd"（JSONの文字列から自動変換）。 */
    private LocalDate dueDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
