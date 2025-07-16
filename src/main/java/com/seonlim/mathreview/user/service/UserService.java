package com.seonlim.mathreview.user.service;

import com.seonlim.mathreview.user.dto.MyPageAnswerData;
import com.seonlim.mathreview.user.dto.MyPageUserData;
import com.seonlim.mathreview.user.repository.AnswerRepository;
import com.seonlim.mathreview.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    public MyPageUserData getMyPageUserData(Long userId) {
        return userRepository.findById(userId)
                .map(MyPageUserData::from)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }

    public List<MyPageAnswerData> getMyPageAnswerData(Long userId) {
        return Optional.of(answerRepository.findAllByUserId(userId))
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream()
                        .map(MyPageAnswerData::from)
                        .toList())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No answers found for user with id: " + userId));
    }


}
