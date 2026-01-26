package main.com.pos.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class Telegram {


    public static void telegramSend(String message) {
        Properties properties = new Properties();

        try (InputStream input = Telegram.class.getClassLoader().getResourceAsStream("main/com/pos/resources/telegram.properties")) {

            if (input == null) {
                System.out.println(Color.RED + "💔 Sorry, unable to find telegram.properties" + Color.RESET);
                return;
            }  properties.load(input);
            TelegramBotSender(message, properties.getProperty("telegram.BotToken"), properties.getProperty("telegram.ChatId"));

        } catch (Exception e) { System.out.println(Color.RED + "Error: " + e.getMessage() + Color.RESET); }
    }

    public static void TelegramBotSender(String message, String BOT_TOKEN, String CHAT_ID) {
        String urlString = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";

        try {
            @SuppressWarnings("deprecation")
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String payload = "chat_id=" + CHAT_ID + "&text=" + message;

            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) System.out.println(Color.GREEN + "💬 Message sent successfully!" + Color.RESET);
            else System.out.println(Color.RED + "🪫 Failed to send message. Response code: " + responseCode + Color.RESET);
            
            connection.disconnect();
        } catch (IOException e) {
            System.out.println(Color.RED + "🛑 An error occurred while sending the message." + Color.RESET);
        }
    }

    
}
