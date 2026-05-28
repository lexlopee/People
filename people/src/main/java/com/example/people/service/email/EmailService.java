package com.example.people.service.email;

import com.example.people.entity.user.UsuarioEntity;
import com.example.people.service.user.UsuarioService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired
    private UsuarioService usuarioService;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${app.logo.url:}")
    private String logoUrl;

    // Notifica a TODOS los administradores de una nueva solicitud
    public void notificarAdminNuevaSolicitud(String nombreSolicitante, String emailSolicitante,
                                             String titulo, String organizacion, Integer idSolicitud) {
        List<UsuarioEntity> admins = usuarioService.listarPorRol("administrador");
        String asunto = "[People] Nueva solicitud: " + titulo;

        String contenido = """
            <h2 style="color:#1a1a2e;margin:0 0 16px;">Nueva solicitud pendiente</h2>
            <p style="color:#4a4a6a;margin:0 0 20px;">Una creadora ha enviado una nueva solicitud que requiere tu revisión.</p>
            <table style="width:100%%;border-collapse:collapse;">
              <tr><td style="padding:8px 0;color:#9ca3af;width:140px;">Solicitante</td><td style="padding:8px 0;color:#1a1a2e;font-weight:600;">%s</td></tr>
              <tr><td style="padding:8px 0;color:#9ca3af;">Email</td><td style="padding:8px 0;color:#1a1a2e;">%s</td></tr>
              <tr><td style="padding:8px 0;color:#9ca3af;">Organización</td><td style="padding:8px 0;color:#1a1a2e;">%s</td></tr>
              <tr><td style="padding:8px 0;color:#9ca3af;">Solicitud</td><td style="padding:8px 0;color:#1a1a2e;font-weight:600;">%s</td></tr>
              <tr><td style="padding:8px 0;color:#9ca3af;">ID</td><td style="padding:8px 0;color:#1a1a2e;">#%d</td></tr>
            </table>
            <div style="text-align:center;margin:28px 0 8px;">
              <a href="http://localhost:4200/admin" style="background:#6b3b8f;color:#fff;padding:13px 32px;border-radius:999px;text-decoration:none;font-weight:700;display:inline-block;">Revisar en el panel</a>
            </div>
            """.formatted(nombreSolicitante, emailSolicitante, organizacion, titulo, idSolicitud);

        String html = plantilla(contenido);

        for (UsuarioEntity admin : admins) {
            if (admin.getEmail() != null && admin.getFechaBaja() == null) {
                enviarHtml(admin.getEmail(), asunto, html);
            }
        }
    }

    public void notificarAprobacion(String emailDestino, String nombre, String titulo) {
        String asunto = "[People] Tu solicitud ha sido aprobada ✅";
        String contenido = """
            <div style="text-align:center;">
              <div style="font-size:56px;margin-bottom:8px;">🎉</div>
              <h2 style="color:#1a1a2e;margin:0 0 12px;">¡Solicitud aprobada!</h2>
              <p style="color:#4a4a6a;line-height:1.6;margin:0 0 8px;">Hola <strong>%s</strong>,</p>
              <p style="color:#4a4a6a;line-height:1.6;margin:0 0 20px;">Tu solicitud <strong>"%s"</strong> ha sido aprobada por el equipo de People y ya está publicada en la plataforma.</p>
              <a href="http://localhost:4200/categoria" style="background:#6b3b8f;color:#fff;padding:13px 32px;border-radius:999px;text-decoration:none;font-weight:700;display:inline-block;">Ver en la plataforma</a>
            </div>
            """.formatted(nombre, titulo);
        enviarHtml(emailDestino, asunto, plantilla(contenido));
    }

    public void notificarRechazo(String emailDestino, String nombre, String titulo, String motivo) {
        String asunto = "[People] Sobre tu solicitud";
        String contenido = """
            <h2 style="color:#1a1a2e;margin:0 0 12px;">Tu solicitud no ha sido aprobada</h2>
            <p style="color:#4a4a6a;line-height:1.6;margin:0 0 8px;">Hola <strong>%s</strong>,</p>
            <p style="color:#4a4a6a;line-height:1.6;margin:0 0 16px;">Lamentamos informarte que tu solicitud <strong>"%s"</strong> no ha sido aprobada en este momento.</p>
            <div style="background:#fef2f2;border-left:4px solid #ef4444;padding:14px 18px;border-radius:6px;margin:0 0 16px;">
              <p style="color:#991b1b;margin:0;"><strong>Motivo:</strong> %s</p>
            </div>
            <p style="color:#4a4a6a;line-height:1.6;margin:0;">Puedes revisar los detalles y volver a intentarlo cuando quieras.</p>
            """.formatted(nombre, titulo, motivo != null ? motivo : "No especificado");
        enviarHtml(emailDestino, asunto, plantilla(contenido));
    }

    // Plantilla HTML con cabecera del logo de People
    private String plantilla(String contenido) {
        String logo = (logoUrl != null && !logoUrl.isEmpty())
                ? "<img src=\"" + logoUrl + "\" alt=\"People\" style=\"height:70px;width:auto;\" />"
                : "<h1 style=\"color:#fff;margin:0;font-size:28px;\">People</h1>";

        return """
            <div style="font-family:Arial,Helvetica,sans-serif;background:#f4eef7;padding:30px 16px;">
              <div style="max-width:580px;margin:0 auto;background:#fff;border-radius:18px;overflow:hidden;box-shadow:0 4px 20px rgba(107,59,143,0.1);">
                <div style="background:linear-gradient(135deg,#6b3b8f,#8b5fa8);padding:28px;text-align:center;">
                  %s
                  <p style="color:rgba(255,255,255,0.85);margin:8px 0 0;font-size:13px;">Acompañando a la mujer maltratada</p>
                </div>
                <div style="padding:32px 28px;">
                  %s
                </div>
                <div style="background:#faf7fc;padding:18px;text-align:center;border-top:1px solid #f0e8f5;">
                  <p style="color:#9ca3af;font-size:12px;margin:0;">Este correo se ha generado automáticamente. No respondas a este mensaje.</p>
                  <p style="color:#c4b5d0;font-size:12px;margin:6px 0 0;">© 2026 People · 4TheWomen</p>
                </div>
              </div>
            </div>
            """.formatted(logo, contenido);
    }

    private void enviarHtml(String destino, String asunto, String html) {
        if (mailSender == null) {
            System.out.println("[EmailService] mailSender no configurado. Email para " + destino + " no enviado.");
            return;
        }
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(destino);
            helper.setSubject(asunto);
            helper.setText(html, true);
            mailSender.send(mensaje);
            System.out.println("[EmailService] Email enviado a " + destino);
        } catch (Exception e) {
            System.err.println("[EmailService] Error enviando a " + destino + ": " + e.getMessage());
        }
    }
}