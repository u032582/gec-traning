package com.example.training.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 顧客登録のリクエスト用DTO（外から受け取る入力の形）。
 *
 * <p>返す用（CustomerResponse）とは別に作る。入力には id が無く、出力には id があるなど、
 * 「入力の形」と「出力の形」は違うことが多いため、分けておくと安全で読みやすい。
 *
 * <p>各項目に付いた注釈（@NotBlank など）が入力チェックのルール。
 * Controllerで @Valid を付けると、このルールに沿って自動検査される。
 * import が jakarta.* である点に注意（Spring Boot 3系は javax ではなく jakarta）。
 */
public record CustomerCreateRequest(

        @NotBlank(message = "名前は必須です")
        @Size(max = 100, message = "名前は100文字以内で入力してください")
        String name,

        @NotBlank(message = "メールアドレスは必須です")
        @Email(message = "メールアドレスの形式が正しくありません")
        String email
) {}
