package com.rafael.atendimento.infra.security;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSchedule {
    private final UserService userService;

    @Scheduled(cron = "0 0 0 * * ?") // Executa diariamente à meia-noite; @Scheduled(cron = "0 */3 * * * ?") Executa em 3 minutos 
    public void atualizarUsuarios() {
        List<User> usuariosSuspensos = userService.buscarUsuariosSuspensos();
        for (User usuario : usuariosSuspensos) {
            userService.verificarSuspensao(usuario.getId());
        }
    }
}
