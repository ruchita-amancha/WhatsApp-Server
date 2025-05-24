package com.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.whatsapp.entity.Chat;
import com.whatsapp.entity.User;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

	 // Find all chats for a specific user
    @Query("Select c from Chat c join c.users u where u.id = :userId")
    public List<Chat> findChatByUserId(@Param("userId") Integer userId);

    // Find a single non-group chat between two users
    @Query("Select c from Chat c where c.isGroup=false and :user member of c.users and :reqUser member of c.users")
    public Chat findSingleChatByUserIds(@Param("user") User user, @Param("reqUser") User reqUser);
}
