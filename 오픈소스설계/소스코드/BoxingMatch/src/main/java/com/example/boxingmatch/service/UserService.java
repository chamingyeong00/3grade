package com.example.boxingmatch.service;

import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.dto.UserDto;
import com.example.boxingmatch.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;



@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private HttpSession session;

    private final UserRepository userRepository;

    @Transactional
    public User registerUser(UserDto userDto) {

        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .gender(userDto.getGender())
                .region(userDto.getRegion())
                .skilllevelCondition(userDto.getSkilllevelCondition())
                .build();

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public void updateUser(Long userId, UserDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }


    public User getCurrentUser() {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return null;

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("로그인된 사용자를 찾을 수 없습니다."));
    }
}
