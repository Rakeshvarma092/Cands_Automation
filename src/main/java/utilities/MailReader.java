package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for reading emails from Gmail via IMAP.
 * Enhanced with thread safety, proper resource management, and robust parsing.
 */
public class MailReader {

    private static final Logger log = LogManager.getLogger(MailReader.class);
    private static final String HOST = "imap.gmail.com";
    private static final String MAIL_FOLDER_NAME = "INBOX";

    /**
     * Reads Gmail for an OTP based on subject and content patterns.
     *
     * @param userName             Gmail address
     * @param password             App-specific password
     * @param emailSubjectContent Pattern to find in the subject
     * @param emailContentPattern Pattern appearing before the OTP
     * @param lengthOfOTP          Length of the OTP to extract
     * @return Extracted OTP string, or null if not found
     */
    public String readGmail(String userName, String password, String emailSubjectContent, String emailContentPattern, int lengthOfOTP)
            throws InterruptedException, MessagingException, IOException {

        log.info("Starting Gmail read for user: {} (Searching for: {})", userName, emailSubjectContent);
        Thread.sleep(3000); // Give Gmail some time to receive the mail

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getInstance(props);

        Store store = null;
        Folder inbox = null;
        String otp = null;

        try {
            store = session.getStore();
            store.connect(HOST, userName, password);
            inbox = store.getFolder(MAIL_FOLDER_NAME);
            inbox.open(Folder.READ_ONLY);

            int messageCount = inbox.getMessageCount();
            int unreadMsgCount = inbox.getUnreadMessageCount();

            log.debug("Connected to Gmail. Total messages: {}, Unread: {}", messageCount, unreadMsgCount);

            // Searching from newest to oldest within unread range
            for (int i = messageCount; i > (messageCount - unreadMsgCount) && i > 0; i--) {
                Message message = inbox.getMessage(i);
                String subject = message.getSubject();

                if (subject != null && subject.contains(emailSubjectContent)) {
                    log.info("Found matching email subject: {}", subject);

                    String cleanedContent = extractCleanedContent(message);

                    int index = cleanedContent.indexOf(emailContentPattern);
                    if (index != -1) {
                        int start = index + emailContentPattern.length();
                        int end = Math.min(start + lengthOfOTP, cleanedContent.length());
                        otp = cleanedContent.substring(start, end).trim();

                        log.info("Successfully extracted OTP: {}", otp);
                        break;
                    } else {
                        log.warn("Pattern '{}' not found in email content", emailContentPattern);
                    }
                }
                // Mark as seen if not matching (preserving original behavior logic)
                message.setFlag(Flags.Flag.SEEN, true);
            }
        } catch (MessagingException | IOException e) {
            log.error("Failed to read Gmail: {}", e.getMessage());
            throw e;
        } finally {
            if (inbox != null && inbox.isOpen()) {
                inbox.close(false);
            }
            if (store != null) {
                store.close();
            }
            log.debug("Closed Gmail connection resources");
        }

        return otp;
    }

    /**
     * Reads email content and strips HTML-like tags (simple replacement).
     */
    private String extractCleanedContent(Message message) throws IOException, MessagingException {
        StringBuilder buffer = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(message.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Preserving the original logic of stripping basic HTML tags while making it cleaner
                String cleanedLine = line.trim()
                        .replaceAll("</?[^>]+>", "") // Better regex for tag stripping
                        .replace("&nbsp;", " ");
                buffer.append(cleanedLine);
            }
        }
        return buffer.toString();
    }
}