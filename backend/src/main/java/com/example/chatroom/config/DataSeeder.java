package com.example.chatroom.config;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.chatroom.entity.Chatroom;
import com.example.chatroom.entity.Message;
import com.example.chatroom.entity.Utilisateur;
import com.example.chatroom.repository.ChatroomRepository;
import com.example.chatroom.repository.MessageRepository;
import com.example.chatroom.repository.UtilisateurRepository;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initData(UtilisateurRepository userRepo, ChatroomRepository chatRepo, MessageRepository msgRepo) {
        return args -> {

            // 1. Insertion d'utilisateurs si absent
            Utilisateur admin = userRepo.findByLogin("admin");
            Utilisateur user1 = userRepo.findByLogin("bob");
            Utilisateur user2 = userRepo.findByLogin("charlie");

            if (admin == null && user1 == null && user2 == null) {
                admin = userRepo.save(new Utilisateur("Alice", "Admin", "alice@chat.com", "admin", "admin123"));
                user1 = userRepo.save(new Utilisateur("Bob", "User", "bob@chat.com", "bob", "passbob"));
                user2 = userRepo.save(new Utilisateur("Charlie", "User", "charlie@chat.com", "charlie", "passcharlie"));
                System.out.println("✔ Utilisateurs créés.");
            } else {
                System.out.println("ℹ Utilisateurs déjà existants.");
                if (admin == null) admin = userRepo.findByLogin("admin");
                if (user1 == null) user1 = userRepo.findByLogin("bob");
                if (user2 == null) user2 = userRepo.findByLogin("charlie");
            }

            // 2. Insertion de chatroom si absente
            if (chatRepo.count() == 0) {
                Chatroom chatroom = new Chatroom();
                chatroom.setName("Général");
                chatroom.setDescription("Chatroom principale");
                chatroom.setAdminId(admin);
                chatroom.setAuthorizedUsers(Arrays.asList(admin, user1));
                chatroom.setAskingUsers(List.of(user2));
                chatroom = chatRepo.save(chatroom);
                System.out.println("✔ Chatroom créée.");

                // 3. Insertion de messages si absents
                Message msg1 = new Message("Salut tout le monde 👋", LocalDateTime.now(), admin, null, chatroom);
                Message msg2 = new Message("Salut Alice !", LocalDateTime.now(), user1, msg1, chatroom);
                msgRepo.saveAll(List.of(msg1, msg2));

                System.out.println("✔ Messages insérés.");
            } else {
                System.out.println("ℹ Chatroom et messages déjà présents.");
            }
        };
    }
}
