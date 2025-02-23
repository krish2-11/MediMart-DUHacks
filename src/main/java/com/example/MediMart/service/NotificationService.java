package com.example.MediMart.service;

import com.example.MediMart.model.Inventory;
import com.example.MediMart.repository.InventoryRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine; // Thymeleaf template engine

    @Scheduled(cron = "0 0 9 * * ?") // Runs at 9 AM every day
    public void checkAndSendNotifications() {
        List<Inventory> inventoryList = inventoryRepository.findAll();
        Date today = new Date();

        for (Inventory inventory : inventoryList) {
            long daysToExpiry = (Date.from(inventory.getExpDate().atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() - today.getTime()) / (1000 * 60 * 60 * 24);

            // ✅ If medicine is ALREADY EXPIRED and no notification was sent
            if (daysToExpiry < 0 && !inventory.isExpiryNotified7Days() && !inventory.isExpiryNotified1Day()) {
                sendEmail(
                        inventory.getEmail(),
                        "🚨 Medicine Expired – Immediate Attention Required!",
                        inventory.getMedicine().getName(),
                        inventory.getExpDate().toString(),
                        "expired" // Notification type
                );

                // Set both expiry flags to true (prevents repeated emails)
                inventory.setExpiryNotified7Days(true);
                inventory.setExpiryNotified1Day(true);
            }

            // ✅ If 7 days remaining and notification not sent yet
            if (daysToExpiry <= 7 && !inventory.isExpiryNotified7Days()) {
                sendEmail(
                        inventory.getEmail(),
                        "⏳ Your Medicine Will Expire in 7 Days!",
                        inventory.getMedicine().getName(),
                        inventory.getExpDate().toString(),
                        "7days" // Notification type
                );

                inventory.setExpiryNotified7Days(true);
            }

            // ✅ If 1 day remaining and notification not sent yet
            if (daysToExpiry <= 1 && !inventory.isExpiryNotified1Day()) {
                sendEmail(
                        inventory.getEmail(),
                        "⚠ Urgent: Your Medicine Expires Tomorrow!",
                        inventory.getMedicine().getName(),
                        inventory.getExpDate().toString(),
                        "1day" // Notification type
                );

                inventory.setExpiryNotified1Day(true);
            }

            // ✅ If stock is low and notification not sent yet
            if (inventory.getQuantity() < inventory.getReorderLevel() && !inventory.isLowStockNotified()) {
                sendEmail(
                        inventory.getEmail(),
                        "📉 Low Stock Warning – Restock Your Medicine Now!",
                        inventory.getMedicine().getName(),
                        inventory.getExpDate().toString(),
                        "lowStock" // Notification type
                );

                inventory.setLowStockNotified(true);
            }

            inventoryRepository.save(inventory); // Save changes to DB
        }
    }

    private void sendEmail(String to, String subject, String medicineName, String expiryDate, String notificationType) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariable("medicineName", medicineName);
            context.setVariable("expiryDate", expiryDate);
            context.setVariable("notificationType", notificationType); // Pass notification type to template

            String htmlContent = templateEngine.process("expiry-notification", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("HTML Email sent: " + subject + " to " + to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}