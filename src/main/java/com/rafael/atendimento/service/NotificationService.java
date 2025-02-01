package com.rafael.atendimento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	
	@Autowired
    private EmailService emailService;
	
	public void notifyUserNewRequest(String toEmail, String requestTitle) {
        String subject = "Novo Pedido Criado";
        String message = "<p>Olá,</p>"
                + "<p>Um novo pedido <strong>" + requestTitle + "</strong> foi criado com sucesso!</p>"
                + "<p>Verifique os dados do pedido pelo sistema.</p>";
        emailService.sendNotificationEmail(toEmail, subject, message);
    }

    public void notifyRequestDenied(String toEmail, String requestTitle, String reason) {
        String subject = "Pedido Negado";
        String message = "<p>Olá,</p>"
                + "<p>Infelizmente, seu pedido <strong>" + requestTitle + "</strong> foi negado.</p>"
                + "<p>Motivo: " + reason + "</p>";
        emailService.sendNotificationEmail(toEmail, subject, message);
    }

    public void notifyRequestCompleted(String toEmail, String requestTitle) {
        String subject = "Atendimento Concluído";
        String message = "<p>Olá,</p>"
                + "<p>O atendimento referente ao pedido <strong>" + requestTitle + "</strong> foi concluído.</p>"
                + "<p>Obrigado por utilizar nosso sistema!</p>";
        emailService.sendNotificationEmail(toEmail, subject, message);
    }

    public void notifyUserBecameMonitor(String toEmail, String courseName) {
        String subject = "Parabéns! Agora você é monitor";
        String message = "<p>Olá,</p>"
                + "<p>Você agora é monitor da turma <strong>" + courseName + "</strong>!</p>"
                + "<p>Fique atento às suas responsabilidades e bom trabalho!</p>";
        emailService.sendNotificationEmail(toEmail, subject, message);
    }
    
    public void notifyUserRemoveMonitor(String toEmail, String courseName) {
        String subject = "Agora você não é mais monitor";
        String message = "<p>Olá,</p>"
                + "<p>Seu cargo de monitor da turma <strong>" + courseName + "</strong> foi removido!</p>"
                + "<p>Fique atento às suas responsabilidades e bom trabalho!</p>";
        emailService.sendNotificationEmail(toEmail, subject, message);
    }
    
    public void notifyUserSuspended(String toEmail, String reason, int durationDays) {
        String subject = "Conta Suspensa";
        String message = "<p>Olá,</p>"
                + "<p>Sua conta foi suspensa por <strong>" + durationDays + " dias</strong> devido ao seguinte motivo:</p>"
                + "<p><em>" + reason + "</em></p>"
                + "<p>Durante esse período, você não poderá acessar o sistema.</p>"
                + "<p>Se tiver dúvidas, entre em contato com o suporte.</p>";

        emailService.sendNotificationEmail(toEmail, subject, message);
    }

    public void notifyUserSuspensionEnded(String toEmail) {
        String subject = "Suspensão Expirada";
        String message = "<p>Olá,</p>"
                + "<p>O período de suspensão da sua conta expirou e agora você pode acessar o sistema normalmente.</p>"
                + "<p>Se precisar de ajuda, entre em contato com o suporte.</p>";

        emailService.sendNotificationEmail(toEmail, subject, message);
    }
}
