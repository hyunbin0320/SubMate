package com.submate.backend.member;

import com.submate.backend.member.auth.JwtProvider;
import com.submate.backend.member.dto.LoginRequest;
import com.submate.backend.member.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public Long signup(SignupRequest request) {

        if (memberRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encodedPassword =
                passwordEncoder.encode(request.password());

        Member member = new Member(
                request.email(),
                encodedPassword
        );

        Member savedMember = memberRepository.save(member);

        return savedMember.getMemberId();
    }

    @Transactional(readOnly = true)
    public String login(LoginRequest request) {

        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.")
                );

        if (!passwordEncoder.matches(
                request.password(),
                member.getPassword()
        )) {
            throw new IllegalArgumentException(
                    "이메일 또는 비밀번호가 올바르지 않습니다."
            );
        }

        return jwtProvider.createToken(
                member.getEmail(),
                member.getRole()
        );
    }

    @Transactional(readOnly = true)
public Member getMyInfo(String email) {
    return memberRepository.findByEmail(email)
            .orElseThrow(() ->
                    new IllegalArgumentException("회원 정보를 찾을 수 없습니다.")
            );
    }
}