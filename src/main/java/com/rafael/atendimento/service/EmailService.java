package com.rafael.atendimento.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String fromEmail;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendResetPasswordEmail(String toEmail, String resetToken) {
		String resetLink = "http://localhost:4200/reset-password?token=" + resetToken; // Altere para a URL real

		String subject = "Recuperação de Senha";
		String content = "<p>Olá,</p>"
				+ "<p>Para redefinir sua senha, clique no link abaixo:</p>"
				+ "<p><a href=\"" + resetLink + "\">Redefinir senha</a></p>"
				+ "<p>Se você não solicitou essa alteração, ignore este e-mail.</p>";

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(fromEmail);
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(content, true);

			mailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException("Erro ao enviar e-mail", e);
		}
	}

	public void sendNotificationEmail(String toEmail, String subject, String message) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			helper.setFrom(fromEmail);
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(message, true); // Habilita HTML

			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage());
		}
	}
}
