package com.example.training.member;

import com.example.training.common.NotFoundException;
import org.springframework.stereotype.Service;

/**
 * 利用者に関する判断・処理を担当する Service。
 *
 * <p>この題材では「利用者が実在するかを確かめて取得する」ことが主な役目。
 * 貸出（lending）の処理から「借り主が実在するか」を確認するために呼ばれる。
 */
@Service
public class MemberService {

    private final MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    /**
     * 1件取得。見つからなければ {@link NotFoundException}（→404）。
     */
    public Member findById(Long id) {
        Member member = memberMapper.findById(id);
        if (member == null) {
            throw new NotFoundException("利用者", id);
        }
        return member;
    }
}
