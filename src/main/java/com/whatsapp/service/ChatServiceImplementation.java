package com.whatsapp.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whatsapp.entity.Chat;
import com.whatsapp.entity.User;
import com.whatsapp.exception.ChatException;
import com.whatsapp.exception.UserException;
import com.whatsapp.repository.ChatRepository;

@Service
public class ChatServiceImplementation implements ChatService {

	@Autowired
	private ChatRepository chatRepository;

	@Autowired
	private UserService userService;

	@Override
	public Chat createChat(User reqUser, Integer userId2) throws UserException {
		User user = userService.findUserById(userId2);

		Chat isChatExists = chatRepository.findSingleChatByUserIds(user, reqUser);

		if (isChatExists != null) {
			return isChatExists;
		}

		Chat chat = new Chat();
		chat.setCreatedBy(reqUser);
		chat.getUsers().add(user);
		chat.getUsers().add(reqUser);
		chat.setGroup(false);
		chat=chatRepository.save(chat);
		return chat;
	}

	@Override
	public Chat findChatById(Integer chatId) throws ChatException {
		Optional<Chat> chat = chatRepository.findById(chatId);

		if (chat.isPresent()) {
			return chat.get();
		}
		throw new ChatException("Chat not found with id " + chatId);
	}

	@Override
	public List<Chat> findAllChatByUserId(Integer userId) throws UserException {
		User user = userService.findUserById(userId);
		List<Chat> chats = chatRepository.findChatByUserId(userId);

		return chats;
	}

	@Override
	public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException {
		Chat group = new Chat();
		group.setGroup(true);
		group.setChat_image(req.getChat_image());
		group.setChat_name(req.getChat_name());
		group.setCreatedBy(reqUser);
		group.getAdmins().add(reqUser);

		Set<Integer> uniqueUserIds = new HashSet<>(req.getUserIds());

		// Add admin (reqUser) if not already in the list
		if (!uniqueUserIds.contains(reqUser.getId())) {
			uniqueUserIds.add(reqUser.getId());
		}

		for (Integer userId : uniqueUserIds) {
			User user = userService.findUserById(userId);
			group.getUsers().add(user);
		}

		return chatRepository.save(group);
	}

	@Override
	public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException {
		Optional<Chat> opt = chatRepository.findById(chatId);

		User user = userService.findUserById(chatId);

		if (opt.isPresent()) {
			Chat chat = opt.get();
			if (chat.getAdmins().contains(reqUser)) {
				chat.getUsers().add(user);
				return chatRepository.save(chat);
			} else {
				throw new UserException("You dont have access to add user");
			}
		}
		throw new ChatException("Chat not found with id " + chatId);
	}

	@Override
	public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws UserException, ChatException {
		Optional<Chat> opt = chatRepository.findById(chatId);

		if (opt.isPresent()) {
			Chat chat = opt.get();
			if (chat.getUsers().contains(reqUser)) {
				chat.setChat_name(groupName);
				return chatRepository.save(chat);
			} else {
				throw new UserException("You are not member of this group");
			}
		}

		throw new ChatException("Chat not found with id " + chatId);
	}

	@Override
	public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
		Optional<Chat> opt = chatRepository.findById(chatId);

		User user = userService.findUserById(chatId);

		if (opt.isPresent()) {
			Chat chat = opt.get();
			if (chat.getAdmins().contains(reqUser)) {
				chat.getUsers().remove(user);
				return chatRepository.save(chat);
			} else if (chat.getUsers().contains(reqUser)) {
				if (user.getId().equals(reqUser.getId())) {
					chat.getUsers().remove(reqUser);
					return chatRepository.save(chat);
				}
			}

			throw new UserException("You dont have access to remove user");

		}
		throw new ChatException("Chat not found with id " + chatId);
	}

	@Override
	public void deleteChat(Integer chatId, Integer userId) throws UserException, ChatException {
		Optional<Chat> opt=chatRepository.findById(chatId);
		if(opt.isPresent()) {
			Chat chat=opt.get();
			chatRepository.deleteById(chat.getId());
		}

	}

}
