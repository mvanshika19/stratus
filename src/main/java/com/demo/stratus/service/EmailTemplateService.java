package com.demo.stratus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final EmailService emailService;

    public void sendWelcomeEmail(String to, String firstName) {
        log.info("Preparing welcome email for: {}", to);
        String subject = "Welcome to Stratus — You're cleared for takeoff!";
        String body = buildWelcomeEmailBody(firstName);
        emailService.sendHtmlEmail(to, subject, body);
    }

    public void sendFlightDelayAlert(String to, String flightNumber,
            String origin, String destination,
            String delayInfo) {
        log.info("Preparing flight delay alert for: {} | Flight: {}", to, flightNumber);
        String subject = "Flight Alert: " + flightNumber + " is delayed";
        String body = buildFlightDelayEmailBody(flightNumber, origin, destination, delayInfo);
        emailService.sendHtmlEmail(to, subject, body);
    }

   private String buildWelcomeEmailBody(String firstName) { 
    return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Welcome to Stratus</title>
            </head>
            <body style="margin:0; padding:0; background-color:#f9fafb; font-family:'Helvetica Neue', Helvetica, Arial, sans-serif;">
                <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f9fafb; padding: 40px 0;">
                    <tr>
                        <td align="center">
                            <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:16px; overflow:hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.05);">
                                
                                <tr>
                                    <td style="padding: 40px 48px 20px; text-align:left;">
                                        <div style="font-size:24px; color:#1a4a7a; font-weight:800; letter-spacing:-0.5px;">Stratus</div>
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding: 0 48px 60px;">
                                        <h1 style="margin:0 0 20px; font-size:32px; font-weight:700; color:#111827; line-height:1.2;">
                                            Welcome aboard, %s!
                                        </h1>
                                        
                                        <p style="margin:0 0 24px; font-size:18px; color:#4b5563; line-height:1.6;">
                                            We're so glad you're here. Travel can be a lot to handle, but we're here to make things just a little bit easier for you.
                                        </p>
                                        
                                        <p style="margin:0 0 24px; font-size:18px; color:#4b5563; line-height:1.6;">
                                            At its heart, Stratus is here to help you track your flights and keep your plans organized in one place.
                                        </p>

                                        <p style="margin:0; font-size:18px; color:#4b5563; line-height:1.6;">
                                            Thanks for letting us be a part of your journey. Safe travels!
                                        </p>
                                        
                                        <p style="margin:32px 0 0; font-size:18px; font-weight:600; color:#1a4a7a;">
                                            — The Stratus Team
                                        </p>
                                    </td>
                                </tr>

                                <tr>
                                    <td style="background-color:#f3f4f6; padding: 24px 48px; text-align:center;">
                                        <p style="margin:0; font-size:13px; color:#9ca3af;">
                                            © 2026 Stratus. We're happy to have you on board.
                                        </p>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(firstName);
}

    private String buildFlightDelayEmailBody(String flightNumber, String origin,
            String destination, String delayInfo) {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Flight Delay Alert</title>
                </head>
                <body style="margin:0; padding:0; background-color:#f0f4f8; font-family:'Helvetica Neue', Helvetica, Arial, sans-serif;">
                    <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f0f4f8; padding: 40px 0;">
                        <tr>
                            <td align="center">
                                <table width="600" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:12px; overflow:hidden; box-shadow: 0 4px 24px rgba(0,0,0,0.08);">

                                    <!-- Header -->
                                    <tr>
                                        <td style="background: linear-gradient(135deg, #0f2944 0%%, #1a4a7a 100%%); padding: 40px 48px; text-align:center;">
                                            <div style="font-size:36px; color:#ffffff; font-weight:700; letter-spacing:4px;">✈ STRATUS</div>
                                            <div style="font-size:13px; color:#a8c4e0; margin-top:6px; letter-spacing:2px;">FLIGHT ALERT</div>
                                        </td>
                                    </tr>

                                    <!-- Alert Body -->
                                    <tr>
                                        <td style="padding: 48px;">
                                            <h1 style="margin:0 0 16px; font-size:24px; font-weight:700; color:#c53030;">⚠ Flight Delay Detected</h1>
                                            <p style="margin:0 0 24px; font-size:16px; color:#4a5568; line-height:1.7;">
                                                Your tracked flight has been updated:
                                            </p>
                                            <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f7faff; border-radius:8px; padding:24px;">
                                                <tr>
                                                    <td style="font-size:15px; color:#0f2944; font-weight:600;">Flight</td>
                                                    <td style="font-size:15px; color:#4a5568;">%s</td>
                                                </tr>
                                                <tr><td style="height:12px;"></td></tr>
                                                <tr>
                                                    <td style="font-size:15px; color:#0f2944; font-weight:600;">Route</td>
                                                    <td style="font-size:15px; color:#4a5568;">%s → %s</td>
                                                </tr>
                                                <tr><td style="height:12px;"></td></tr>
                                                <tr>
                                                    <td style="font-size:15px; color:#0f2944; font-weight:600;">Update</td>
                                                    <td style="font-size:15px; color:#c53030;">%s</td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>

                                    <!-- Footer -->
                                    <tr>
                                        <td style="background:#f7faff; padding: 24px 48px; text-align:center; border-top: 1px solid #e2e8f0;">
                                            <p style="margin:0; font-size:12px; color:#a0aec0;">
                                                © 2026 Stratus Flight Intelligence. All rights reserved.
                                            </p>
                                        </td>
                                    </tr>

                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """
                .formatted(flightNumber, origin, destination, delayInfo);
    }
}